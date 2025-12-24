package com.eagrilift.mall.mapper;

import java.util.List;
import com.eagrilift.mall.domain.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车Mapper接口
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@Mapper
public interface CartMapper
{
    /**
     * 查询购物车
     *
     * @param cartId 购物车主键
     * @return 购物车
     */
    public Cart selectCartByCartId(String cartId);

    /**
     * 查询购物车列表
     *
     * @param cart 购物车
     * @return 购物车集合
     */
    public List<Cart> selectCartList(Cart cart);

    /**
     * 新增购物车
     *
     * @param cart 购物车
     * @return 结果
     */
    public int insertCart(Cart cart);

    /**
     * 修改购物车
     *
     * @param cart 购物车
     * @return 结果
     */
    public int updateCart(Cart cart);

    /**
     * 删除购物车
     *
     * @param cartId 购物车主键
     * @return 结果
     */
    public int deleteCartByCartId(String cartId);

    /**
     * 批量删除购物车
     *
     * @param cartIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCartByCartIds(String[] cartIds);

    /**
     * 根据产品ID和用户ID查询购物车项ID
     */
    public String selectCartIdByProductsAndUserId(String productsId, Long userId);
}
