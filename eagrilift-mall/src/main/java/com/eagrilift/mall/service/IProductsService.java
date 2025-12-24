package com.eagrilift.mall.service;

import java.util.List;
import com.eagrilift.mall.domain.Products;

/**
 * 农户产品Service接口
 *
 * @author eagrilift
 * @date 2025-12-14
 */
public interface IProductsService
{
    /**
     * 查询农户产品
     *
     * @param productsId 农户产品主键
     * @return 农户产品
     */
    public Products selectProductsByProductsId(String productsId);

    /**
     * 查询农户产品列表
     *
     * @param products 农户产品
     * @return 农户产品集合
     */
    public List<Products> selectProductsList(Products products);

    /**
     * 新增农户产品
     *
     * @param products 农户产品
     * @return 结果
     */
    public int insertProducts(Products products);

    /**
     * 批量新增农户产品
     *
     * @param productss 农户产品List
     * @return 结果
     */
    public int batchInsertProducts(List<Products> productss);

    /**
     * 修改农户产品
     *
     * @param products 农户产品
     * @return 结果
     */
    public int updateProducts(Products products);

    /**
     * 批量删除农户产品
     *
     * @param productsIds 需要删除的农户产品主键集合
     * @return 结果
     */
    public int deleteProductsByProductsIds(String[] productsIds);

    /**
     * 删除农户产品信息
     *
     * @param productsId 农户产品主键
     * @return 结果
     */
    public int deleteProductsByProductsId(String productsId);

    /**
     * 查询农户产品列表(无数据权限)
     * @param products
     * @return
     */
    List<Products> selectList(Products products);

}
