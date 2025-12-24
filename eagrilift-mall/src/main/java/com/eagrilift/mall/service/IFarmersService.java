package com.eagrilift.mall.service;

import java.util.List;
import com.eagrilift.mall.domain.Farmers;

/**
 * 农户Service接口
 *
 * @author eagrilift
 * @date 2025-12-11
 */
public interface IFarmersService
{
    /**
     * 查询农户
     *
     * @param farmersId 农户主键
     * @return 农户
     */
    public Farmers selectFarmersByFarmersId(String farmersId);

    /**
     * 查询农户列表
     *
     * @param farmers 农户
     * @return 农户集合
     */
    public List<Farmers> selectFarmersList(Farmers farmers);

    /**
     * 新增农户
     *
     * @param farmers 农户
     * @return 结果
     */
    public int insertFarmers(Farmers farmers);

    /**
     * 批量新增农户
     *
     * @param farmerss 农户List
     * @return 结果
     */
    public int batchInsertFarmers(List<Farmers> farmerss);

    /**
     * 修改农户
     *
     * @param farmers 农户
     * @return 结果
     */
    public int updateFarmers(Farmers farmers);

    /**
     * 批量删除农户
     *
     * @param farmersIds 需要删除的农户主键集合
     * @return 结果
     */
    public int deleteFarmersByFarmersIds(String[] farmersIds);

    /**
     * 删除农户信息
     *
     * @param farmersId 农户主键
     * @return 结果
     */
    public int deleteFarmersByFarmersId(String farmersId);

    /**
     * 查询当前用户有没有进行过农户认证
     * 如果有 就返回审核状态
     * 如果没有 就返回未提交字符串
     * @return
     */
    String selectIsAuth();
}
