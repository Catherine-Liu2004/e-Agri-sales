package com.eagrilift.mall.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import com.eagrilift.common.utils.DateUtils;
import com.eagrilift.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Random;

import com.eagrilift.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.eagrilift.mall.domain.OrdersProducts;
import com.eagrilift.mall.mapper.OrdersMapper;
import com.eagrilift.mall.domain.Orders;
import com.eagrilift.mall.service.IOrdersService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.eagrilift.common.utils.SecurityUtils.getUserId;

/**
 * 订单Service业务层处理
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@Service
public class OrdersServiceImpl implements IOrdersService
{
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询订单
     *
     * @param ordersId 订单主键
     * @return 订单
     */
    @Override
    public Orders selectOrdersByOrdersId(String ordersId)
    {
        return ordersMapper.selectOrdersByOrdersId(ordersId);
    }

    /**
     * 查询订单列表
     *
     * @param orders 订单
     * @return 订单
     */
    @Override
    public List<Orders> selectOrdersList(Orders orders) {
        Long userId = getUserId();
        //如果当前用户是admin角色, 则可以看见所有数据, 不做处理
        if (SecurityUtils.isAdmin(userId)) {
        } else if (SecurityUtils.hasRole("farmers")) { //如果当前用户是农户角色, 就只能看到归属于自己的数据
            orders.setProductsUserId(userId);
        } else { //如果是普通用户, 就只能看见自己创建的订单数据
            orders.setUserId(userId);
        }
        //调用方法查询订单列表数据
        List<Orders> ordersList = ordersMapper.selectOrdersList(orders);
        //遍历查询到的订单列表, 为每个订单设置关联的产品列表
        for (Orders toOrders : ordersList) {
            //获取每个订单的订单ID
            String ordersId = toOrders.getOrdersId();
            //根据订单ID查询该订单下的所有产品
            List<OrdersProducts> ordersProductsList = ordersMapper.selectOrdersByOrdersId(ordersId)
                .getOrdersProductsList();
            //将查询到的产品列表设置到当前订单对象中
            toOrders.setOrdersProductsList(ordersProductsList);
        }
        return ordersList;
    }
    /**
     * 新增订单
     *
     * @param orders 订单
     * @return 结果
     */
    @Transactional
    @Override
    public String insertOrders(Orders orders) {
        orders.setCreateTime(DateUtils.getNowDate());
        //获取当前操作用户ID
        Long loginUserId = getUserId();
        orders.setUserId(loginUserId);
        //获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        //格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);
        //生成三位的随机数
        String randomSuffix = String.format("%03d", new Random().nextInt(1000));
        //将订单号所需的数据拼接起来并插入至订单对象中 (NO + 年月日时分秒 + 三位随机数 + 下单用户ID)
        String ordersId = "NO" + formattedDateTime + randomSuffix + loginUserId;
        orders.setOrdersId(ordersId);
        int rows = ordersMapper.insertOrders(orders);
        insertOrdersProducts(orders);
        return ordersId;
    }
     /* 批量新增订单
     *
     * @param orderss 订单List
     * @return 结果
     */
    @Override
    public int batchInsertOrders(List<Orders> orderss)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(orderss)) {
            try {
                for (int i = 0; i < orderss.size(); i++) {
                    int row = ordersMapper.insertOrders(orderss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == orderss.size() - 1;
                    if (bool){
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            }catch (Exception e){
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            }finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改订单
     *
     * @param orders 订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOrders(Orders orders)
    {
        return ordersMapper.updateOrders(orders);
    }

    /**
     * 批量删除订单
     *
     * @param ordersIds 需要删除的订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOrdersByOrdersIds(String[] ordersIds)
    {
        ordersMapper.deleteOrdersProductsByOrdersIds(ordersIds);
        return ordersMapper.deleteOrdersByOrdersIds(ordersIds);
    }

    /**
     * 删除订单信息
     *
     * @param ordersId 订单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOrdersByOrdersId(String ordersId)
    {
        ordersMapper.deleteOrdersProductsByOrdersId(ordersId);
        return ordersMapper.deleteOrdersByOrdersId(ordersId);
    }

    /**
     * 新增订单产品信息
     *
     * @param orders 订单对象
     */
    public void insertOrdersProducts(Orders orders)
    {
        List<OrdersProducts> ordersProductsList = orders.getOrdersProductsList();
        String ordersId = orders.getOrdersId();
        if (StringUtils.isNotNull(ordersProductsList))
        {
            List<OrdersProducts> list = new ArrayList<OrdersProducts>();
            for (OrdersProducts ordersProducts : ordersProductsList)
            {
                ordersProducts.setOrdersId(ordersId);
                list.add(ordersProducts);
            }
            if (list.size() > 0)
            {
                ordersMapper.batchOrdersProducts(list);
            }
        }
    }

    /**
     * 发货
     * @param orders
     * @return
     */
    @Override
    public int sendOutGoods(Orders orders) {
        orders.setShippingTime(new Date());
        orders.setStatus("待收货");
        return ordersMapper.updateOrders(orders);
    }
}
