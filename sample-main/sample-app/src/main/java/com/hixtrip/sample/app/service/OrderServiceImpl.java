package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.vo.OrderVO;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CommodityDomainService commodityDomainService;

    @Autowired
    private InventoryDomainService inventoryDomainService;

    @Autowired
    private OrderDomainService orderDomainService;

    @Override
    public OrderVO placeOrder(CommandOderCreateDTO commandOderCreateDTO) {
        //查询SKU价格
        String skuId = commandOderCreateDTO.getSkuId();
        BigDecimal skuPrice = commodityDomainService.getSkuPrice(skuId);

        Order order = new Order();
        //减库存
       if(inventoryDomainService.changeInventory(commandOderCreateDTO.getSkuId(), null, Long.parseLong(commandOderCreateDTO.getAmount().toString()), null)){
           order = orderDomainService.createOrder(OrderConvertor.INSTANCE.commandOderCreateDTOToOrder(commandOderCreateDTO));
       }
        //下单
        return OrderConvertor.INSTANCE.orderToOrderVO(order);
    }
}
