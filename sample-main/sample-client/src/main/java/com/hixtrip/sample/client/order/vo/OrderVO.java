package com.hixtrip.sample.client.order.vo;

import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
public class OrderVO {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付状态
     */
    private String payStatus;
    private String code;
    private String msg;
}
