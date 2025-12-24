package com.eagrilift.mall.controller;

import java.math.BigDecimal;
import java.util.List;

import com.eagrilift.mall.vo.ReplenishVo;
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
import com.eagrilift.mall.domain.Products;
import com.eagrilift.mall.service.IProductsService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 农户产品Controller
 *
 * @author eagrilift
 * @date 2025-12-14
 */
@RestController
@RequestMapping("/mall/products")
public class ProductsController extends BaseController
{
    @Autowired
    private IProductsService productsService;

    /**
     * 查询农户产品列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:list')")
    @GetMapping("/list")
    public TableDataInfo list(Products products)
    {
        startPage();
        List<Products> list = productsService.selectProductsList(products);
        return getDataTable(list);
    }

    /**
     * 导出农户产品列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:export')")
    @Log(title = "农户产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Products products)
    {
        List<Products> list = productsService.selectProductsList(products);
        ExcelUtil<Products> util = new ExcelUtil<Products>(Products.class);
        util.exportExcel(response, list, "农户产品数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Products> util = new ExcelUtil<Products>(Products.class);
        util.importTemplateExcel(response, "农户产品数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "农户产品", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:products:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Products> util = new ExcelUtil<Products>(Products.class);
        InputStream inputStream = file.getInputStream();
        List<Products> list = util.importExcel(inputStream );
        inputStream.close();
        int count = productsService.batchInsertProducts(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取农户产品详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:query')")
    @GetMapping(value = "/{productsId}")
    public AjaxResult getInfo(@PathVariable("productsId") String productsId)
    {
        return success(productsService.selectProductsByProductsId(productsId));
    }

    /**
     * 新增农户产品
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:add')")
    @Log(title = "农户产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Products products)
    {
        return toAjax(productsService.insertProducts(products));
    }

    /**
     * 修改农户产品
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:edit')")
    @Log(title = "农户产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Products products)
    {
        return toAjax(productsService.updateProducts(products));
    }

    /**
     * 删除农户产品
     */
    //@PreAuthorize("@ss.hasPermi('mall:products:remove')")
    @Log(title = "农户产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productsIds}")
    public AjaxResult remove(@PathVariable String[] productsIds)
    {
        return toAjax(productsService.deleteProductsByProductsIds(productsIds));
    }

    /**
     * 查询农户产品列表(无数据权限)
     */
    @GetMapping("/selectList")
    public TableDataInfo selectList(Products products) {
        startPage();
        List<Products> list = productsService.selectList(products);
        return getDataTable(list);
    }

    /**
     * 补货
     */
    @Log(title = "补货", businessType = BusinessType.UPDATE)
    @PutMapping("/replenish")
    public AjaxResult replenish(@RequestBody ReplenishVo replenishVo) {
        //补货数量必须大于0
        if (replenishVo.getCount().compareTo(BigDecimal.ZERO) <= 0) {
            return error("补货数量必须大于0");
        }
        //产品ID
        String productsId = replenishVo.getProductsId();
        //要补货的数量
        BigDecimal count = replenishVo.getCount();
        //根据产品ID查询该产品补货前的数量
        BigDecimal inventory = productsService.selectProductsByProductsId(productsId).getInventory();
        //创建一个新的产品对象
        Products products = new Products();
        products.setProductsId(productsId);
        //BigDecimal是对象类型, 不能直接使用运算符进行计算, 必须调用其提供的方法
        products.setInventory(count.add(inventory));
        //更新产品的库存
        return toAjax(productsService.updateProducts(products));
    }
}
