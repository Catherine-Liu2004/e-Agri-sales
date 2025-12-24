package com.eagrilift.mall.domain;

import com.eagrilift.common.annotation.Excel;
import com.eagrilift.common.core.domain.BaseEntity;
import lombok.*;

/**
 * 助农政策对象 policies
 *
 * @author eagrilift
 * @date 2025-12-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Policies extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 助农政策ID */
    private String policiesId;

    /** 政策标题 */
    @Excel(name = "政策标题")
    private String title;

    /** 政策分类 */
    @Excel(name = "政策分类")
    private String category;

    /** 适用地区 */
    @Excel(name = "适用地区")
    private String region;

    /** 发布单位 */
    @Excel(name = "发布单位")
    private String publisher;

    /** 政策摘要 */
    @Excel(name = "政策摘要")
    private String summary;

    /** 详细内容 */
    @Excel(name = "详细内容")
    private String content;

    /** 负责部门 */
    @Excel(name = "负责部门")
    private String department;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 联系地址 */
    @Excel(name = "联系地址")
    private String address;


}
