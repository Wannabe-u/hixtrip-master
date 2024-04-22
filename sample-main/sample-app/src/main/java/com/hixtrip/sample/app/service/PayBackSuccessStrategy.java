package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.PayBackStrategy;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class PayBackSuccessStrategy implements PayBackStrategy {


    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private PayDomainService payDomainService;

    @Override
    public void payCallback(CommandPayDTO commandPayDTO) {
        // 支付成功后的业务逻辑
        CommandPay pay = OrderConvertor.INSTANCE.commandPayDTODTOToOrder(commandPayDTO);
        //修改订单状态
        orderDomainService.orderPaySuccess(pay);
        //记录日志
        payDomainService.payRecord(pay);
    }
}
