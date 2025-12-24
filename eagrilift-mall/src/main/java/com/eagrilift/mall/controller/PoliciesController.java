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
import com.eagrilift.mall.domain.Policies;
import com.eagrilift.mall.service.IPoliciesService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 助农政策Controller
 *
 * @author eagrilift
 * @date 2025-12-16
 */
@RestController
@RequestMapping("/mall/policies")
public class PoliciesController extends BaseController
{
    @Autowired
    private IPoliciesService policiesService;

    /**
     * 查询助农政策列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:list')")
    @GetMapping("/list")
    public TableDataInfo list(Policies policies)
    {
        startPage();
        List<Policies> list = policiesService.selectPoliciesList(policies);
        return getDataTable(list);
    }

    /**
     * 导出助农政策列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:export')")
    @Log(title = "助农政策", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Policies policies)
    {
        List<Policies> list = policiesService.selectPoliciesList(policies);
        ExcelUtil<Policies> util = new ExcelUtil<Policies>(Policies.class);
        util.exportExcel(response, list, "助农政策数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Policies> util = new ExcelUtil<Policies>(Policies.class);
        util.importTemplateExcel(response, "助农政策数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "助农政策", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:policies:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Policies> util = new ExcelUtil<Policies>(Policies.class);
        InputStream inputStream = file.getInputStream();
        List<Policies> list = util.importExcel(inputStream );
        inputStream.close();
        int count = policiesService.batchInsertPolicies(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取助农政策详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:query')")
    @GetMapping(value = "/{policiesId}")
    public AjaxResult getInfo(@PathVariable("policiesId") String policiesId)
    {
        return success(policiesService.selectPoliciesByPoliciesId(policiesId));
    }

    /**
     * 新增助农政策
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:add')")
    @Log(title = "助农政策", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Policies policies)
    {
        return toAjax(policiesService.insertPolicies(policies));
    }

    /**
     * 修改助农政策
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:edit')")
    @Log(title = "助农政策", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Policies policies)
    {
        return toAjax(policiesService.updatePolicies(policies));
    }

    /**
     * 删除助农政策
     */
    //@PreAuthorize("@ss.hasPermi('mall:policies:remove')")
    @Log(title = "助农政策", businessType = BusinessType.DELETE)
	@DeleteMapping("/{policiesIds}")
    public AjaxResult remove(@PathVariable String[] policiesIds)
    {
        return toAjax(policiesService.deletePoliciesByPoliciesIds(policiesIds));
    }
}
