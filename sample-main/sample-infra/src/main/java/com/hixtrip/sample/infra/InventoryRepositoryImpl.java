package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 获取当前库存
    public Integer getInventory(String skuId) {
        return Integer.parseInt(redisTemplate.opsForValue().get("stock:" + skuId).toString());
    }

    // 修改库存
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        String stockKey = "stock:" + skuId;
        String withholdingQuantityKey = "withhold:stock" + skuId;

        //现有库存
        String stockStr = redisTemplate.opsForValue().get(stockKey).toString();
        //预占库存
        String holdStockStr = redisTemplate.opsForValue().get(withholdingQuantityKey).toString();
        if (holdStockStr != null) {
            //可售库存
            sellableQuantity = Long.parseLong(stockStr) - (Long.parseLong(holdStockStr));
            if (sellableQuantity >= withholdingQuantity) {
                //修改预占库存
                redisTemplate.opsForValue().increment(withholdingQuantityKey, withholdingQuantity);
                // 扣减库存
                try {
                    redisTemplate.opsForValue().decrement(stockKey, withholdingQuantity);
                } finally {
                    // 释放预占库存
                    redisTemplate.opsForValue().decrement(withholdingQuantityKey,withholdingQuantity);
                }
                return true;
            } else {
                throw new RuntimeException("库存不足");
            }
        } else {
            // 增加预占库存锁
            redisTemplate.opsForValue().set(withholdingQuantityKey, withholdingQuantity);
            //可售库存
            sellableQuantity = Long.parseLong(stockStr);
            if (sellableQuantity >= withholdingQuantity) {
                // 扣减库存
                try {
                    redisTemplate.opsForValue().decrement(stockKey, withholdingQuantity);
                } finally{
                    // 释放预占库存
                    redisTemplate.delete(withholdingQuantityKey);
                }
                return true;
            } else {
                throw new RuntimeException("库存不足");
            }
        }
    }
}
