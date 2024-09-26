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
import com.ruoyi.nature.domain.CSche;
import com.ruoyi.nature.service.ICScheService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 档期Controller
 *
 * @author ruoyi
 * @date 2021-10-10
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/sche" )
public class CScheController extends BaseController {

    private final ICScheService iCScheService;

    /**
     * 查询档期列表
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:list')")
    @GetMapping("/list")
    public TableDataInfo list(CSche cSche)
    {
        startPage();
        LambdaQueryWrapper<CSche> lqw = new LambdaQueryWrapper<CSche>();
        if (cSche.getPlaceId() != null){
            lqw.eq(CSche::getPlaceId ,cSche.getPlaceId());
        }
        if (StringUtils.isNotBlank(cSche.getYtd())){
            lqw.eq(CSche::getYtd ,cSche.getYtd());
        }
        if (cSche.getPrice() != null){
            lqw.eq(CSche::getPrice ,cSche.getPrice());
        }
        if (cSche.getDays() != null){
            lqw.eq(CSche::getDays ,cSche.getDays());
        }
        if (cSche.getSurplus() != null){
            lqw.eq(CSche::getSurplus ,cSche.getSurplus());
        }
        List<CSche> list = iCScheService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出档期列表
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:export')" )
    @Log(title = "档期" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CSche cSche) {
        LambdaQueryWrapper<CSche> lqw = new LambdaQueryWrapper<CSche>(cSche);
        List<CSche> list = iCScheService.list(lqw);
        ExcelUtil<CSche> util = new ExcelUtil<CSche>(CSche. class);
        return util.exportExcel(list, "sche" );
    }

    /**
     * 获取档期详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) Long id) {
        return R.success(iCScheService.getById(id));
    }

    /**
     * 新增档期
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:add')" )
    @Log(title = "档期" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CSche cSche) {
        return toAjax(iCScheService.save(cSche) ? 1 : 0);
    }

    /**
     * 修改档期
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:edit')" )
    @Log(title = "档期" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CSche cSche) {
        return toAjax(iCScheService.updateById(cSche) ? 1 : 0);
    }

    /**
     * 删除档期
     */
    @PreAuthorize("@ss.hasPermi('nature:sche:remove')" )
    @Log(title = "档期" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable Long[] ids) {
        return toAjax(iCScheService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
