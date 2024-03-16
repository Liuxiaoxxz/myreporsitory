package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource IShopTypeService typeService;
    public Result getList() {
        String key = "shopType:";
        String typeJson = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(typeJson)){
            List<ShopType> list = JSONUtil.toList(typeJson,ShopType.class);
            return Result.ok(list);
        }
        List<ShopType> list = typeService.query().orderByAsc("sort").list();
        typeJson = JSONUtil.toJsonStr(list);
        return Result.ok(list);
    }
}
