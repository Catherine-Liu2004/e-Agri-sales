package com.eagrilift.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 补货需要的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplenishVo {

    //产品ID
    private String productsId;

    //补货数量
    private BigDecimal count;
}
