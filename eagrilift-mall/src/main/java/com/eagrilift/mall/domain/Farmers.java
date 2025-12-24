package com.eagrilift.mall.domain;

import com.eagrilift.common.annotation.Excel;
import com.eagrilift.common.core.domain.BaseEntity;
import lombok.*;

/**
 * 农户对象 farmers
 *
 * @author eagrilift
 * @date 2025-12-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Farmers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 农户ID */
    private String farmersId;

    /** 农户类型 */
    @Excel(name = "农户类型")
    private String farmerType;

    /** 真实姓名/合作社名称 */
    @Excel(name = "真实姓名/合作社名称")
    private String realName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String phone;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 主要农产品 */
    @Excel(name = "主要农产品")
    private String mainProducts;

    /** 种植/养殖规模 */
    @Excel(name = "种植/养殖规模")
    private String scale;

    /** 身份证正面 */
    @Excel(name = "身份证正面")
    private String idCardFront;

    /** 身份证反面 */
    @Excel(name = "身份证反面")
    private String idCardBack;

    /** 土地证明/承包合同 */
    @Excel(name = "土地证明/承包合同")
    private String landProof;

    /** 农产品照片 */
    @Excel(name = "农产品照片")
    private String productPhotos;

    /** 其他证明材料 */
    @Excel(name = "其他证明材料")
    private String otherProof;

    /** 认证状态 */
    @Excel(name = "认证状态")
    private String authStatus;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    //申请人用户名
    private String userName;
}
