package com.eagrilift.mall.mapper;

import java.util.List;
import com.eagrilift.mall.domain.Farmers;
import org.apache.ibatis.annotations.Mapper;

/**
 * 农户Mapper接口
 *
 * @author eagrilift
 * @date 2025-12-11
 */
@Mapper
public interface FarmersMapper
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
     * 修改农户
     *
     * @param farmers 农户
     * @return 结果
     */
    public int updateFarmers(Farmers farmers);

    /**
     * 删除农户
     *
     * @param farmersId 农户主键
     * @return 结果
     */
    public int deleteFarmersByFarmersId(String farmersId);

    /**
     * 批量删除农户
     *
     * @param farmersIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFarmersByFarmersIds(String[] farmersIds);

    /**
     * 查询当前用户有没有进行农户认证
     * @param userId
     * @return
     */
    Boolean selectIsAuth(Long userId);

    /**
     * 根据用户ID查询审核状态
     * @param userId
     * @return
     */
    String selectFarmersByUserId(Long userId);
}
