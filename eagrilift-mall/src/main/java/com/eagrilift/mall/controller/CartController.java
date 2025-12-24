package com.eagrilift.mall.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eagrilift.common.annotation.Log;
import com.eagrilift.common.core.controller.BaseController;
import com.eagrilift.common.core.domain.AjaxResult;
import com.eagrilift.common.enums.BusinessType;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.eagrilift.mall.domain.Cart;
import com.eagrilift.mall.service.ICartService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 购物车Controller
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@RestController
@RequestMapping("/mall/cart")
public class CartController extends BaseController
{
    @Autowired
    private ICartService cartService;

    /**
     * 查询购物车列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:list')")
    @GetMapping("/list")
    public TableDataInfo list(Cart cart)
    {
        startPage();
        List<Cart> list = cartService.selectCartList(cart);
        return getDataTable(list);
    }

    /**
     * 导出购物车列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:export')")
    @Log(title = "购物车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Cart cart)
    {
        List<Cart> list = cartService.selectCartList(cart);
        ExcelUtil<Cart> util = new ExcelUtil<Cart>(Cart.class);
        util.exportExcel(response, list, "购物车数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Cart> util = new ExcelUtil<Cart>(Cart.class);
        util.importTemplateExcel(response, "购物车数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "购物车", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:cart:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Cart> util = new ExcelUtil<Cart>(Cart.class);
        InputStream inputStream = file.getInputStream();
        List<Cart> list = util.importExcel(inputStream );
        inputStream.close();
        int count = cartService.batchInsertCart(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取购物车详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:query')")
    @GetMapping(value = "/{cartId}")
    public AjaxResult getInfo(@PathVariable("cartId") String cartId)
    {
        return success(cartService.selectCartByCartId(cartId));
    }

    /**
     * 新增购物车
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:add')")
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Cart cart)
    {
        return toAjax(cartService.insertCart(cart));
    }

    /**
     * 修改购物车
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:edit')")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Cart cart)
    {
        return toAjax(cartService.updateCart(cart));
    }

    /**
     * 删除购物车
     */
    //@PreAuthorize("@ss.hasPermi('mall:cart:remove')")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cartIds}")
    public AjaxResult remove(@PathVariable String[] cartIds)
    {
        return toAjax(cartService.deleteCartByCartIds(cartIds));
    }
}
