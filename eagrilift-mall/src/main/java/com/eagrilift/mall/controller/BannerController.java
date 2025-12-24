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
import com.eagrilift.mall.domain.Banner;
import com.eagrilift.mall.service.IBannerService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 轮播图Controller
 *
 * @author eagrilift
 * @date 2025-12-16
 */
@RestController
@RequestMapping("/mall/banner")
public class BannerController extends BaseController
{
    @Autowired
    private IBannerService bannerService;

    /**
     * 查询轮播图列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(Banner banner)
    {
        startPage();
        List<Banner> list = bannerService.selectBannerList(banner);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:export')")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Banner banner)
    {
        List<Banner> list = bannerService.selectBannerList(banner);
        ExcelUtil<Banner> util = new ExcelUtil<Banner>(Banner.class);
        util.exportExcel(response, list, "轮播图数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Banner> util = new ExcelUtil<Banner>(Banner.class);
        util.importTemplateExcel(response, "轮播图数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "轮播图", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:banner:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Banner> util = new ExcelUtil<Banner>(Banner.class);
        InputStream inputStream = file.getInputStream();
        List<Banner> list = util.importExcel(inputStream );
        inputStream.close();
        int count = bannerService.batchInsertBanner(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取轮播图详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:query')")
    @GetMapping(value = "/{bannerId}")
    public AjaxResult getInfo(@PathVariable("bannerId") String bannerId)
    {
        return success(bannerService.selectBannerByBannerId(bannerId));
    }

    /**
     * 新增轮播图
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Banner banner)
    {
        return toAjax(bannerService.insertBanner(banner));
    }

    /**
     * 修改轮播图
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Banner banner)
    {
        return toAjax(bannerService.updateBanner(banner));
    }

    /**
     * 删除轮播图
     */
    //@PreAuthorize("@ss.hasPermi('mall:banner:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable String[] bannerIds)
    {
        return toAjax(bannerService.deleteBannerByBannerIds(bannerIds));
    }
}
