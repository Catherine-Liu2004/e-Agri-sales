package com.eagrilift.mall.service;

import java.util.List;
import com.eagrilift.mall.domain.Addresses;

/**
 * 收货地址Service接口
 *
 * @author eagrilift
 * @date 2025-12-15
 */
public interface IAddressesService
{
    /**
     * 查询收货地址
     *
     * @param addressesId 收货地址主键
     * @return 收货地址
     */
    public Addresses selectAddressesByAddressesId(String addressesId);

    /**
     * 查询收货地址列表
     *
     * @param addresses 收货地址
     * @return 收货地址集合
     */
    public List<Addresses> selectAddressesList(Addresses addresses);

    /**
     * 新增收货地址
     *
     * @param addresses 收货地址
     * @return 结果
     */
    public int insertAddresses(Addresses addresses);

    /**
     * 批量新增收货地址
     *
     * @param addressess 收货地址List
     * @return 结果
     */
    public int batchInsertAddresses(List<Addresses> addressess);

    /**
     * 修改收货地址
     *
     * @param addresses 收货地址
     * @return 结果
     */
    public int updateAddresses(Addresses addresses);

    /**
     * 批量删除收货地址
     *
     * @param addressesIds 需要删除的收货地址主键集合
     * @return 结果
     */
    public int deleteAddressesByAddressesIds(String[] addressesIds);

    /**
     * 删除收货地址信息
     *
     * @param addressesId 收货地址主键
     * @return 结果
     */
    public int deleteAddressesByAddressesId(String addressesId);
}
