package com.eagrilift.mall.service;

import java.util.List;
import com.eagrilift.mall.domain.Orders;

/**
 * 订单Service接口
 *
 * @author eagrilift
 * @date 2025-12-15
 */
public interface IOrdersService
{
    /**
     * 查询订单
     *
     * @param ordersId 订单主键
     * @return 订单
     */
    public Orders selectOrdersByOrdersId(String ordersId);

    /**
     * 查询订单列表
     *
     * @param orders 订单
     * @return 订单集合
     */
    public List<Orders> selectOrdersList(Orders orders);

    /**
     * 新增订单
     *
     * @param orders 订单
     * @return 结果
     */
    public String insertOrders(Orders orders);

    /**
     * 批量新增订单
     *
     * @param orderss 订单List
     * @return 结果
     */
    public int batchInsertOrders(List<Orders> orderss);

    /**
     * 修改订单
     *
     * @param orders 订单
     * @return 结果
     */
    public int updateOrders(Orders orders);

    /**
     * 批量删除订单
     *
     * @param ordersIds 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteOrdersByOrdersIds(String[] ordersIds);

    /**
     * 删除订单信息
     *
     * @param ordersId 订单主键
     * @return 结果
     */
    public int deleteOrdersByOrdersId(String ordersId);

    /**
     * 发货
     * @param orders
     * @return
     */
    public int sendOutGoods(Orders orders);
}
