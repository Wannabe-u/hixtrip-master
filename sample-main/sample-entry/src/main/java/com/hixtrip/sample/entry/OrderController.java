package com.hixtrip.sample.entry;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.app.service.PayBackContext;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.order.vo.OrderVO;
import com.hixtrip.sample.domain.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo 这是你要实现的
 */
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    private final PayBackContext payBackContext;

    public OrderController(PayBackContext payBackContext) {
        this.payBackContext = payBackContext;
    }

    /**
     * todo 这是你要实现的接口
     *
     * @param commandOderCreateDTO 入参对象
     * @return OrderVO
     */
    @PostMapping(path = "/command/order/create")
    public OrderVO placeOder(@RequestBody CommandOderCreateDTO commandOderCreateDTO) {
        //登录信息可以在这里模拟
        var userId = "";
        return orderService.placeOrder(commandOderCreateDTO);
    }

    /**
     * todo 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return OrderVO
     */

    /**
     {
     "id": "123456789",
     "payStatus": "0",
     "timestamp": "2024-04-10 10:00:00",
     "signature": "xxx"
     }
     根据订单号和订单状态，判断是否重复
     */
    @PostMapping(path = "/command/order/pay/callback")
    public OrderVO payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        payBackContext.payCallbackByStrategy(commandPayDTO);
        Order order = new Order();
        OrderVO orderVo = OrderConvertor.INSTANCE.orderToOrderVO(order);
        orderVo.setCode("200");
        orderVo.setMsg("");
        return orderVo;
    }
}
