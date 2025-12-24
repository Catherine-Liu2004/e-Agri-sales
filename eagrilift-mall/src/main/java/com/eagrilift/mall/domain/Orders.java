package com.eagrilift.mall.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

import com.eagrilift.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.eagrilift.common.annotation.Excel;
import lombok.*;

/**
 * 订单对象 orders
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单号 */
    private String ordersId;

    /** 所属农户UserID */
    @Excel(name = "商家UserID")
    private Long productsUserId;

    /** 发货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shippingTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 商品总价 */
    @Excel(name = "商品总价")
    private BigDecimal totalPrice;

    /** 收货人 */
    @Excel(name = "收货人")
    private String name;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 收货地址 */
    @Excel(name = "收货地址")
    private String address;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String status;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 订单产品信息 */
    private List<OrdersProducts> ordersProductsList;

    //下单用户
    private String userName;

    //农户名称
    private String farmersName;

}
