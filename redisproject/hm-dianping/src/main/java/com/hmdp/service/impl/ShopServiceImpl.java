package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryById(Long id) {
        //缓存穿透

        //缓存击穿
        //Shop shop = queryWithMutex(id);
        Shop shop = querryWithLogicalExpire(id);
        if(shop == null){
            return Result.fail("店铺不存在");
        }
        return  Result.ok(shop);
    }

    public Shop queryWithMutex(Long id){
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        if(StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            return shop;
        }
        if(shopJson != null){
            return null;
        }
        String lockkey = LOCK_SHOP_KEY + id;
        //获取互斥锁
        Shop shop = null;
        try {
            boolean isLock = trylock(lockkey);
            if(!isLock){
                Thread.sleep(50);
                return queryWithMutex(id);
            }
            //不存在，根据ID查询数据库
            shop = getById(id);

            if(shop == null){
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
                return null;
            }
            stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockkey);
        }

        return shop;
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if(id == null){
            return Result.fail("店铺id不能为空");
        }
        updateById(shop);
        stringRedisTemplate.delete(CACHE_SHOP_KEY+id);
        return Result.ok();
    }
    public Shop queryWithPassThrough(Long id){
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        if(StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            return shop;
        }
        if(shopJson != null){
            return null;
        }
        //不存在，根据ID查询数据库
        Shop shop = getById(id);

        if(shop == null){
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop),CACHE_SHOP_TTL, TimeUnit.MINUTES);

        return shop;
    }
    private boolean trylock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }

    private void saveShop2Redis(Long id,Long expireSec){
        //从数据库查询店铺数据
        Shop shop = getById(id);
        //封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSec));

        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY+id,JSONUtil.toJsonStr(redisData));

    }
    private static final ExecutorService CACHE_REBUILD_EXCUTOR = Executors.newFixedThreadPool(10);
    public Shop querryWithLogicalExpire(Long id){
        String key = CACHE_SHOP_KEY + id;
        //从Redis查缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //这里为空代表不存在该热点key
        if(StrUtil.isNotBlank(shopJson)){
            return null;
        }
        //命中先把JSON反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return shop;
        }
        //过期
        String lockkey = LOCK_SHOP_KEY + id;
        //获取互斥锁
        boolean isLock = trylock(lockkey);
        if(isLock){

                CACHE_REBUILD_EXCUTOR.submit(() -> {
                    try{
                    this.saveShop2Redis(id,20L);
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        unlock(lockkey);
                    }
                });
        }

        return shop;
    }
}
