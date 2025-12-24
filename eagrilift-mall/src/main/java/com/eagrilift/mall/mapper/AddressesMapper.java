package com.eagrilift.mall.mapper;

import java.util.List;
import com.eagrilift.mall.domain.Addresses;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址Mapper接口
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@Mapper
public interface AddressesMapper
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
     * 修改收货地址
     *
     * @param addresses 收货地址
     * @return 结果
     */
    public int updateAddresses(Addresses addresses);

    /**
     * 删除收货地址
     *
     * @param addressesId 收货地址主键
     * @return 结果
     */
    public int deleteAddressesByAddressesId(String addressesId);

    /**
     * 批量删除收货地址
     *
     * @param addressesIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAddressesByAddressesIds(String[] addressesIds);

    /**
     * 根据用户ID查询该用户有没有默认地址
     */
    public int selectIsDefaultByUserId(Long userId);

    /**
     * 根据当前用户ID查询该用户此前的默认地址ID
     */
    public String selectIsDefaultAddressIdByUserId(Long userId);
}
