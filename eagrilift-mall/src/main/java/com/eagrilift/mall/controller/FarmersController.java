package com.eagrilift.mall.controller;

import java.util.List;

import com.eagrilift.common.utils.StringUtils;
import com.eagrilift.system.service.ISysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.eagrilift.mall.domain.Farmers;
import com.eagrilift.mall.service.IFarmersService;
import com.eagrilift.common.utils.poi.ExcelUtil;
import com.eagrilift.common.core.page.TableDataInfo;

/**
 * 农户Controller
 *
 * @author eagrilift
 * @date 2025-12-11
 */
@RestController
@RequestMapping("/mall/farmers")
public class FarmersController extends BaseController {
    @Autowired
    private IFarmersService farmersService;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${eagrilift.farmers}")
    private Long farmersRoleId;
    /**
     * 查询农户列表
     */
   // @PreAuthorize("@ss.hasPermi('mall:farmers:list')")
    @GetMapping("/list")
    public TableDataInfo list(Farmers farmers) {
        startPage();
        List<Farmers> list = farmersService.selectFarmersList(farmers);
        return getDataTable(list);
    }

    /**
     * 导出农户列表
     */
    //@PreAuthorize("@ss.hasPermi('mall:farmers:export')")
    @Log(title = "农户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Farmers farmers) {
        List<Farmers> list = farmersService.selectFarmersList(farmers);
        ExcelUtil<Farmers> util = new ExcelUtil<Farmers>(Farmers.class);
        util.exportExcel(response, list, "农户数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Farmers> util = new ExcelUtil<Farmers>(Farmers.class);
        util.importTemplateExcel(response, "农户数据");
    }

    /**
     * 导入数据
     */
    @Log(title = "农户", businessType = BusinessType.IMPORT)
    //@PreAuthorize("@ss.hasPermi('mall:farmers:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Farmers> util = new ExcelUtil<Farmers>(Farmers.class);
        InputStream inputStream = file.getInputStream();
        List<Farmers> list = util.importExcel(inputStream);
        inputStream.close();
        int count = farmersService.batchInsertFarmers(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取农户详细信息
     */
    //@PreAuthorize("@ss.hasPermi('mall:farmers:query')")
    @GetMapping(value = "/{farmersId}")
    public AjaxResult getInfo(@PathVariable("farmersId") String farmersId) {
        return success(farmersService.selectFarmersByFarmersId(farmersId));
    }

    /**
     * 新增农户
     */
    //@PreAuthorize("@ss.hasPermi('mall:farmers:add')")
    @Log(title = "农户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Farmers farmers) {
        return toAjax(farmersService.insertFarmers(farmers));
    }

    /**
     * 修改农户
     */
    //@PreAuthorize("@ss.hasPermi('mall:farmers:edit')")
    @Log(title = "农户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Farmers farmers) {
        return toAjax(farmersService.updateFarmers(farmers));
    }

    /**
     * 删除农户
     */
    //@PreAuthorize("@ss.hasPermi('mall:farmers:remove')")
    @Log(title = "农户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{farmersIds}")
    public AjaxResult remove(@PathVariable String[] farmersIds) {
        return toAjax(farmersService.deleteFarmersByFarmersIds(farmersIds));
    }

    /**
     * 查询当前用户有没有进行过农户认证
     * 如果有 就返回审核状态
     * 如果没有 就返回未提交字符串
     */
    @GetMapping("/selectIsAuth")
    public AjaxResult selectIsAuth() {
        String authStatus = farmersService.selectIsAuth();
        return success(authStatus);
    }

    /**
     * 通过认证
     */
    @Log(title = "通过认证", businessType = BusinessType.UPDATE)
    @PutMapping("/agree/{farmersId}")
    @Transactional(rollbackFor = Exception.class) //添加事务管理; 确保两个操作(角色认证和认证状态更新) 在一个事务中
    public AjaxResult agree(@PathVariable String farmersId) {
        //参数校验
        if (StringUtils.isBlank(farmersId)) {
            return error("农户ID不能为空");
        }
        try {
            //查询农户的信息
            Farmers farmers = farmersService.selectFarmersByFarmersId(farmersId);
            if (farmers == null) {
                return error("农户信息不存在");
            }
            //获取该农户的用户ID
            Long userId = farmers.getUserId();
            if (userId == null) {
                return error("用户ID不存在");
            }
            //更新该用户的角色为农户
            sysUserService.insertUserAuth(userId, new Long[]{farmersRoleId});
            //更新认证状态
            Farmers updateFarmers = new Farmers();
            updateFarmers.setFarmersId(farmersId);
            updateFarmers.setAuthStatus("认证通过");
            int result = farmersService.updateFarmers(updateFarmers);
            return toAjax(result);
        } catch (Exception e) {
            return error("农户认证通过失败");
        }
    }
}
