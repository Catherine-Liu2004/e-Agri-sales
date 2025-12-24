package com.eagrilift.mall.service.impl;

import java.util.List;
import com.eagrilift.common.utils.DateUtils;
import com.eagrilift.common.utils.SecurityUtils;
import com.eagrilift.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eagrilift.mall.mapper.ProductsMapper;
import com.eagrilift.mall.domain.Products;
import com.eagrilift.mall.service.IProductsService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.eagrilift.common.utils.SecurityUtils.getUserId;

/**
 * 农户产品Service业务层处理
 *
 * @author eagrilift
 * @date 2025-12-14
 */
@Service
public class ProductsServiceImpl implements IProductsService
{
    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询农户产品
     *
     * @param productsId 农户产品主键
     * @return 农户产品
     */
    @Override
    public Products selectProductsByProductsId(String productsId)
    {
        return productsMapper.selectProductsByProductsId(productsId);
    }

    /**
     * 查询农户产品列表
     *
     * @param products 农户产品
     * @return 农户产品
     */
    @Override
    public List<Products> selectProductsList(Products products) {
        //当前登录用户的userId
        Long loginUserId = getUserId();
        //如果当前用户不是admin角色 则设置用户ID作为查询条件, 实现数据隔离
        if (!SecurityUtils.isAdmin(loginUserId)) {
            //将当前用户ID作为过滤条件, 确保稚嫩刚查询自己创建的数据
            products.setUserId(loginUserId);
        }
        return productsMapper.selectProductsList(products);
    }
    /**
     * 新增农户产品
     *
     * @param products 农户产品
     * @return 结果
     */
    @Override
    public int insertProducts(Products products)
    {
        products.setCreateTime(DateUtils.getNowDate());
        products.setProductsId(IdUtils.fastSimpleUUID());
        products.setUserId(getUserId());
        return productsMapper.insertProducts(products);
    }

    /**
     * 批量新增农户产品
     *
     * @param productss 农户产品List
     * @return 结果
     */
    @Override
    public int batchInsertProducts(List<Products> productss) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(productss)) {
            try {
                for (int i = 0; i < productss.size(); i++) {
                    //插入主键ID和用户ID
                    for (Products products : productss) {
                        products.setProductsId(IdUtils.fastSimpleUUID());
                        products.setUserId(getUserId());
                    }
                    int row = productsMapper.insertProducts(productss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == productss.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改农户产品
     *
     * @param products 农户产品
     * @return 结果
     */
    @Override
    public int updateProducts(Products products)
    {
        return productsMapper.updateProducts(products);
    }

    /**
     * 批量删除农户产品
     *
     * @param productsIds 需要删除的农户产品主键
     * @return 结果
     */
    @Override
    public int deleteProductsByProductsIds(String[] productsIds)
    {
        return productsMapper.deleteProductsByProductsIds(productsIds);
    }

    /**
     * 删除农户产品信息
     *
     * @param productsId 农户产品主键
     * @return 结果
     */
    @Override
    public int deleteProductsByProductsId(String productsId)
    {
        return productsMapper.deleteProductsByProductsId(productsId);
    }

    /**
     * 查询农户产品列表(无数据权限)
     * @param products
     * @return
     */
    @Override
    public List<Products> selectList(Products products) {
        return productsMapper.selectProductsList(products);
    }
}
