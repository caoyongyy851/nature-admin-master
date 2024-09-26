package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nature.domain.CCategory;
import com.ruoyi.nature.service.ICCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 场地分类Controller
 *
 * @author ruoyi
 * @date 2021-09-22
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/category" )
public class CCategoryController extends BaseController {

    private final ICCategoryService iCCategoryService;

    /**
     * 查询场地分类列表
     */
    @PreAuthorize("@ss.hasPermi('nature:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(CCategory cCategory)
    {
        startPage();
        LambdaQueryWrapper<CCategory> lqw = new LambdaQueryWrapper<CCategory>();
        if (StringUtils.isNotBlank(cCategory.getName())){
            lqw.like(CCategory::getName ,cCategory.getName());
        }
        if (StringUtils.isNotBlank(cCategory.getTopicName())){
            lqw.like(CCategory::getTopicName ,cCategory.getTopicName());
        }
        if (StringUtils.isNotBlank(cCategory.getIcon())){
            lqw.eq(CCategory::getIcon ,cCategory.getIcon());
        }
        if (cCategory.getOrderNum() != null){
            lqw.eq(CCategory::getOrderNum ,cCategory.getOrderNum());
        }
        List<CCategory> list = iCCategoryService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 获取分类下来选
     * @return
     */
    @GetMapping("/optionList")
    public R optionList(){
        List<CCategory> list = iCCategoryService.list();
        return R.success(list);
    }

    /**
     * 导出场地分类列表
     */
    @PreAuthorize("@ss.hasPermi('nature:category:export')" )
    @Log(title = "场地分类" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CCategory cCategory) {
        LambdaQueryWrapper<CCategory> lqw = new LambdaQueryWrapper<CCategory>(cCategory);
        List<CCategory> list = iCCategoryService.list(lqw);
        ExcelUtil<CCategory> util = new ExcelUtil<CCategory>(CCategory. class);
        return util.exportExcel(list, "category" );
    }

    /**
     * 获取场地分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:category:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCCategoryService.getById(id));
    }

    /**
     * 新增场地分类
     */
    @PreAuthorize("@ss.hasPermi('nature:category:add')" )
    @Log(title = "场地分类" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CCategory cCategory) {
        return toAjax(iCCategoryService.save(cCategory) ? 1 : 0);
    }

    /**
     * 修改场地分类
     */
    @PreAuthorize("@ss.hasPermi('nature:category:edit')" )
    @Log(title = "场地分类" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CCategory cCategory) {
        return toAjax(iCCategoryService.updateById(cCategory) ? 1 : 0);
    }

    /**
     * 删除场地分类
     */
    @PreAuthorize("@ss.hasPermi('nature:category:remove')" )
    @Log(title = "场地分类" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCCategoryService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
