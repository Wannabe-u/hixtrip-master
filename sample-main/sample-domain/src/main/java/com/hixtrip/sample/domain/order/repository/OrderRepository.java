package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 *
 */
public interface OrderRepository {
    Order createOrder(Order order);

    /**
     * todo 需要实现
     * 待付款订单支付成功
     */
    Boolean orderPaySuccess(CommandPay commandPay);

    /**
     * todo 需要实现
     * 待付款订单支付失败
     */
    Boolean orderPayFail(CommandPay commandPay);
}
