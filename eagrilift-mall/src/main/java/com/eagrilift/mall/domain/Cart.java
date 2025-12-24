package com.eagrilift.mall.domain;

import com.eagrilift.common.annotation.Excel;
import com.eagrilift.common.core.domain.BaseEntity;
import lombok.*;

import java.math.BigDecimal;

/**
 * 购物车对象 cart
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 购物车ID */
    private String cartId;

    /** 产品ID */
    @Excel(name = "产品ID")
    private String productsId;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 规格 */
    private String specs;

    /** 产地 */
    private String origin;

    /** 价格 */
    private BigDecimal price;

    /** 图片 */
    private String image;

    /** 产品所属农户用户ID */
    private Long productsUserId;

    /** 产品名称 */
    private String productsName;

    /** 创建人用户名 */
    private String createUserName;

    /** 产品所属农户用户名 */
    private String toUserName;

}
