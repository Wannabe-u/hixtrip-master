package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.PayBackStrategy;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.order.vo.OrderVO;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class PayBackContext {

    private final Map<String, PayBackStrategy> payBackStrategies;

    public PayBackContext(Map<String, PayBackStrategy> payBackStrategies) {
        this.payBackStrategies = payBackStrategies;
        this.payBackStrategies.put("0", new PayBackSuccessStrategy());
        this.payBackStrategies.put("1", new PayBackFailureStrategy());
        this.payBackStrategies.put("2", new PayBackuplicateStrategy());
    }

    public void payCallbackByStrategy(CommandPayDTO commandPayDTO) {
        // 根据支付结果调用不同的策略
        PayBackStrategy strategy = payBackStrategies.get(commandPayDTO.getPayStatus());
        if (strategy != null) {
            strategy.payCallback(commandPayDTO);
        } else {
            // 处理未知支付状态
        }
    }
}
