package com.eagrilift.mall.service.impl;

import java.util.List;
import com.eagrilift.common.utils.DateUtils;
import com.eagrilift.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eagrilift.mall.mapper.PoliciesMapper;
import com.eagrilift.mall.domain.Policies;
import com.eagrilift.mall.service.IPoliciesService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.eagrilift.common.utils.SecurityUtils.getUserId;

/**
 * 助农政策Service业务层处理
 *
 * @author eagrilift
 * @date 2025-12-16
 */
@Service
public class PoliciesServiceImpl implements IPoliciesService
{
    @Autowired
    private PoliciesMapper policiesMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询助农政策
     *
     * @param policiesId 助农政策主键
     * @return 助农政策
     */
    @Override
    public Policies selectPoliciesByPoliciesId(String policiesId)
    {
        return policiesMapper.selectPoliciesByPoliciesId(policiesId);
    }

    /**
     * 查询助农政策列表
     *
     * @param policies 助农政策
     * @return 助农政策
     */
    @Override
    public List<Policies> selectPoliciesList(Policies policies)
    {
        return policiesMapper.selectPoliciesList(policies);
    }

    /**
     * 新增助农政策
     *
     * @param policies 助农政策
     * @return 结果
     */
    @Override
    public int insertPolicies(Policies policies)
    {
        policies.setCreateTime(DateUtils.getNowDate());
        policies.setPoliciesId(IdUtils.fastSimpleUUID());
        return policiesMapper.insertPolicies(policies);
    }

    /**
     * 批量新增助农政策
     *
     * @param policiess 助农政策List
     * @return 结果
     */
    @Override
    public int batchInsertPolicies(List<Policies> policiess)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(policiess)) {
            try {
                for (int i = 0; i < policiess.size(); i++) {
                    int row = policiesMapper.insertPolicies(policiess.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == policiess.size() - 1;
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
     * 修改助农政策
     *
     * @param policies 助农政策
     * @return 结果
     */
    @Override
    public int updatePolicies(Policies policies)
    {
        return policiesMapper.updatePolicies(policies);
    }

    /**
     * 批量删除助农政策
     *
     * @param policiesIds 需要删除的助农政策主键
     * @return 结果
     */
    @Override
    public int deletePoliciesByPoliciesIds(String[] policiesIds)
    {
        return policiesMapper.deletePoliciesByPoliciesIds(policiesIds);
    }

    /**
     * 删除助农政策信息
     *
     * @param policiesId 助农政策主键
     * @return 结果
     */
    @Override
    public int deletePoliciesByPoliciesId(String policiesId)
    {
        return policiesMapper.deletePoliciesByPoliciesId(policiesId);
    }
}
