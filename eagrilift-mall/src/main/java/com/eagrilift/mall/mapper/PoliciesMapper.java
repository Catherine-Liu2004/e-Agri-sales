package com.eagrilift.mall.mapper;

import java.util.List;
import com.eagrilift.mall.domain.Policies;
import org.apache.ibatis.annotations.Mapper;

/**
 * 助农政策Mapper接口
 *
 * @author eagrilift
 * @date 2025-12-16
 */
@Mapper
public interface PoliciesMapper
{
    /**
     * 查询助农政策
     *
     * @param policiesId 助农政策主键
     * @return 助农政策
     */
    public Policies selectPoliciesByPoliciesId(String policiesId);

    /**
     * 查询助农政策列表
     *
     * @param policies 助农政策
     * @return 助农政策集合
     */
    public List<Policies> selectPoliciesList(Policies policies);

    /**
     * 新增助农政策
     *
     * @param policies 助农政策
     * @return 结果
     */
    public int insertPolicies(Policies policies);

    /**
     * 修改助农政策
     *
     * @param policies 助农政策
     * @return 结果
     */
    public int updatePolicies(Policies policies);

    /**
     * 删除助农政策
     *
     * @param policiesId 助农政策主键
     * @return 结果
     */
    public int deletePoliciesByPoliciesId(String policiesId);

    /**
     * 批量删除助农政策
     *
     * @param policiesIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePoliciesByPoliciesIds(String[] policiesIds);
}
