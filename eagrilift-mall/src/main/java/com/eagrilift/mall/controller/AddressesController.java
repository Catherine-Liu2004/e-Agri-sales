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
import com.eagrilift.mall.domain.Addresses;
import com.eagrilift.mall.service.IAddressesService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 收货地址Controller
 *
 * @author eagrilift
 * @date 2025-12-15
 */
@RestController
@RequestMapping("/mall/addresses")
public class AddressesController extends BaseController
{
    @Autowired
    private IAddressesService addressesService;

    /**
     * 查询收货地址列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:list')")
    @GetMapping("/list")
    public TableDataInfo list(Addresses addresses)
    {
        startPage();
        List<Addresses> list = addressesService.selectAddressesList(addresses);
        return getDataTable(list);
    }

    /**
     * 导出收货地址列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:export')")
    @Log(title = "收货地址", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Addresses addresses)
    {
        List<Addresses> list = addressesService.selectAddressesList(addresses);
        ExcelUtil<Addresses> util = new ExcelUtil<Addresses>(Addresses.class);
        util.exportExcel(response, list, "收货地址数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Addresses> util = new ExcelUtil<Addresses>(Addresses.class);
        util.importTemplateExcel(response, "收货地址数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "收货地址", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:addresses:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Addresses> util = new ExcelUtil<Addresses>(Addresses.class);
        InputStream inputStream = file.getInputStream();
        List<Addresses> list = util.importExcel(inputStream );
        inputStream.close();
        int count = addressesService.batchInsertAddresses(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取收货地址详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:query')")
    @GetMapping(value = "/{addressesId}")
    public AjaxResult getInfo(@PathVariable("addressesId") String addressesId)
    {
        return success(addressesService.selectAddressesByAddressesId(addressesId));
    }

    /**
     * 新增收货地址
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:add')")
    @Log(title = "收货地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Addresses addresses)
    {
        return toAjax(addressesService.insertAddresses(addresses));
    }

    /**
     * 修改收货地址
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:edit')")
    @Log(title = "收货地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Addresses addresses)
    {
        return toAjax(addressesService.updateAddresses(addresses));
    }

    /**
     * 删除收货地址
     */
    //@PreAuthorize("@ss.hasPermi('mall:addresses:remove')")
    @Log(title = "收货地址", businessType = BusinessType.DELETE)
	@DeleteMapping("/{addressesIds}")
    public AjaxResult remove(@PathVariable String[] addressesIds)
    {
        return toAjax(addressesService.deleteAddressesByAddressesIds(addressesIds));
    }
}
