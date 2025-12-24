package com.eagrilift.mall.service.impl;

import java.util.List;
import com.eagrilift.common.utils.DateUtils;
import com.eagrilift.common.utils.uuid.IdUtils;
import com.eagrilift.mall.domain.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eagrilift.mall.mapper.FarmersMapper;
import com.eagrilift.mall.domain.Farmers;
import com.eagrilift.mall.service.IFarmersService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.eagrilift.common.utils.SecurityUtils.getUserId;

/**
 * 农户Service业务层处理
 *
 * @author eagrilift
 * @date 2025-12-11
 */
@Service
public class FarmersServiceImpl implements IFarmersService
{
    @Autowired
    private FarmersMapper farmersMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询农户
     *
     * @param farmersId 农户主键
     * @return 农户
     */
    @Override
    public Farmers selectFarmersByFarmersId(String farmersId)
    {
        return farmersMapper.selectFarmersByFarmersId(farmersId);
    }

    /**
     * 查询农户列表
     *
     * @param farmers 农户
     * @return 农户
     */
    @Override
    public List<Farmers> selectFarmersList(Farmers farmers)
    {
        return farmersMapper.selectFarmersList(farmers);
    }

    /**
     * 新增农户
     *
     * @param farmers 农户
     * @return 结果
     */
    @Override
    public int insertFarmers(Farmers farmers)
    {
        farmers.setCreateTime(DateUtils.getNowDate());
        farmers.setFarmersId(IdUtils.fastUUID());
        farmers.setUserId(getUserId());
        return farmersMapper.insertFarmers(farmers);
    }

    /**
     * 批量新增农户
     *
     * @param farmerss 农户List
     * @return 结果
     */
    @Override
    public int batchInsertFarmers(List<Farmers> farmerss)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(farmerss)) {
            try {
                for (int i = 0; i < farmerss.size(); i++) {
                    for (Farmers farmers : farmerss) {
                        farmers.setFarmersId(IdUtils.fastSimpleUUID());
                        farmers.setUserId(getUserId());
                    }
                    int row = farmersMapper.insertFarmers(farmerss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == farmerss.size() - 1;
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
     * 修改农户
     *
     * @param farmers 农户
     * @return 结果
     */
    @Override
    public int updateFarmers(Farmers farmers)
    {
        return farmersMapper.updateFarmers(farmers);
    }

    /**
     * 批量删除农户
     *
     * @param farmersIds 需要删除的农户主键
     * @return 结果
     */
    @Override
    public int deleteFarmersByFarmersIds(String[] farmersIds)
    {
        return farmersMapper.deleteFarmersByFarmersIds(farmersIds);
    }

    /**
     * 删除农户信息
     *
     * @param farmersId 农户主键
     * @return 结果
     */
    @Override
    public int deleteFarmersByFarmersId(String farmersId)
    {
        return farmersMapper.deleteFarmersByFarmersId(farmersId);
    }


    /**
     * 查询当前用户有没有进行过农户认证
     * 如果有 就返回审核状态
     * 如果没有 就返回未提交字符串
     * @return
     */
    @Override
    public String selectIsAuth() {
        //当前用户的ID
        Long userId = getUserId();
        //查询当前用户有没有进行农户认证
        Boolean isAuth = farmersMapper.selectIsAuth(userId);
        if (isAuth) {
            //根据用户ID查询审核状态
            return farmersMapper.selectFarmersByUserId(userId);
        } else {
            //用户没有提交过认证
            return "未提交";
        }
    }

}
