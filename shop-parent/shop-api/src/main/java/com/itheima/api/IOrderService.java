package com.itheima.api;

import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradeOrder;

/**
 * 订单接口
 */
public interface IOrderService {

    /**
     * 下单接口
     * @param order
     * @return
     */
    Result confirmOrder(TradeOrder order);

}
