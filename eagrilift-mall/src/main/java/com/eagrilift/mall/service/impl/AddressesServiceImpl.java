package com.eagrilift.mall.service.impl;

import java.util.List;
import com.eagrilift.common.utils.DateUtils;
import com.eagrilift.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eagrilift.mall.mapper.AddressesMapper;
import com.eagrilift.mall.domain.Addresses;
import com.eagrilift.mall.service.IAddressesService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.eagrilift.common.utils.SecurityUtils.getUserId;

/**
 * 收货地址Service业务层处理
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@Service
public class AddressesServiceImpl implements IAddressesService
{
    @Autowired
    private AddressesMapper addressesMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询收货地址
     *
     * @param addressesId 收货地址主键
     * @return 收货地址
     */
    @Override
    public Addresses selectAddressesByAddressesId(String addressesId)
    {
        return addressesMapper.selectAddressesByAddressesId(addressesId);
    }

    /**
     * 查询收货地址列表
     *
     * @param addresses 收货地址
     * @return 收货地址
     */
    @Override
    public List<Addresses> selectAddressesList(Addresses addresses)
    {
        return addressesMapper.selectAddressesList(addresses);
    }

    /**
     * 新增收货地址
     *
     * @param addresses 收货地址
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAddresses(Addresses addresses) {
        //当前登录用户ID
        Long userId = getUserId();
        addresses.setCreateTime(DateUtils.getNowDate());
        addresses.setAddressesId(IdUtils.fastSimpleUUID());
        addresses.setUserId(getUserId());
        //查看用户提交的是不是默认地址
        Boolean isDefault = addresses.getIsDefault();
        if(isDefault==null)
            isDefault=false;
        //如果新增的是默认地址, 将原来的默认地址取消; 如果新增的不是默认地址, 直接新增
        if (isDefault) {
            //根据用户ID查询该用户有没有默认地址
            int isDefaultCount = addressesMapper.selectIsDefaultByUserId(userId);
            if (isDefaultCount>0) { //默认地址大于0, 说明此前存在默认地址
                //根据当前用户ID查询该用户此前的默认地址ID
                String addressId = addressesMapper.selectIsDefaultAddressIdByUserId(userId);
                //将此前的默认地址改为非默认
                Addresses updateAddresses = new Addresses();
                updateAddresses.setAddressesId(addressId);
                updateAddresses.setIsDefault(false);
                addressesMapper.updateAddresses(updateAddresses);
            }
        }
        return addressesMapper.insertAddresses(addresses);
    }
    /**
     * 批量新增收货地址
     *
     * @param addressess 收货地址List
     * @return 结果
     */
    @Override
    public int batchInsertAddresses(List<Addresses> addressess)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(addressess)) {
            try {
                for (int i = 0; i < addressess.size(); i++) {
                    int row = addressesMapper.insertAddresses(addressess.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == addressess.size() - 1;
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
     * 修改收货地址
     *
     * @param addresses 收货地址
     * @return 结果
     */
    @Override
    public int updateAddresses(Addresses addresses) {
        //当前登录用户ID
        Long userId = getUserId();
        //查看用户提交的是不是默认地址
        Boolean isDefault = addresses.getIsDefault();
        //如果新增的是默认地址, 将原来的默认地址取消; 如果新增的不是默认地址, 直接新增
        if (isDefault) {
            //根据用户ID查询该用户有没有默认地址
            int isDefaultCount = addressesMapper.selectIsDefaultByUserId(userId);
            if (isDefaultCount>0) { //默认地址大于0, 说明此前存在默认地址
                //根据当前用户ID查询该用户此前的默认地址ID
                String addressId = addressesMapper.selectIsDefaultAddressIdByUserId(userId);
                //将此前的默认地址改为非默认
                Addresses updateAddresses = new Addresses();
                updateAddresses.setAddressesId(addressId);
                updateAddresses.setIsDefault(false);
                addressesMapper.updateAddresses(updateAddresses);
            }
        }
        return addressesMapper.updateAddresses(addresses);
    }

    /*
     * 批量删除收货地址
     *
     * @param addressesIds 需要删除的收货地址主键
     * @return 结果
     */
    @Override
    public int deleteAddressesByAddressesIds(String[] addressesIds)
    {
        return addressesMapper.deleteAddressesByAddressesIds(addressesIds);
    }

    /**
     * 删除收货地址信息
     *
     * @param addressesId 收货地址主键
     * @return 结果
     */
    @Override
    public int deleteAddressesByAddressesId(String addressesId)
    {
        return addressesMapper.deleteAddressesByAddressesId(addressesId);
    }
}
