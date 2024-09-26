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
import com.ruoyi.nature.domain.CTags;
import com.ruoyi.nature.service.ICTagsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 标签Controller
 *
 * @author ruoyi
 * @date 2021-09-22
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/tags" )
public class CTagsController extends BaseController {

    private final ICTagsService iCTagsService;

    /**
     * 查询标签列表
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:list')")
    @GetMapping("/list")
    public TableDataInfo list(CTags cTags)
    {
        startPage();
        LambdaQueryWrapper<CTags> lqw = new LambdaQueryWrapper<CTags>();
        if (StringUtils.isNotBlank(cTags.getName())){
            lqw.like(CTags::getName ,cTags.getName());
        }
        List<CTags> list = iCTagsService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出标签列表
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:export')" )
    @Log(title = "标签" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CTags cTags) {
        LambdaQueryWrapper<CTags> lqw = new LambdaQueryWrapper<CTags>(cTags);
        List<CTags> list = iCTagsService.list(lqw);
        ExcelUtil<CTags> util = new ExcelUtil<CTags>(CTags. class);
        return util.exportExcel(list, "tags" );
    }

    /**
     * 获取标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCTagsService.getById(id));
    }

    /**
     * 新增标签
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:add')" )
    @Log(title = "标签" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CTags cTags) {
        return toAjax(iCTagsService.save(cTags) ? 1 : 0);
    }

    /**
     * 修改标签
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:edit')" )
    @Log(title = "标签" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CTags cTags) {
        return toAjax(iCTagsService.updateById(cTags) ? 1 : 0);
    }

    /**
     * 删除标签
     */
    @PreAuthorize("@ss.hasPermi('nature:tags:remove')" )
    @Log(title = "标签" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCTagsService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
