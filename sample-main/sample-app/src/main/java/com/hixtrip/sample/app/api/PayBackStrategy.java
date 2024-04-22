package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;

/**
 * 支付策略调用
 **/
public interface PayBackStrategy
{
    void payCallback(CommandPayDTO commandPayDTO);
}
