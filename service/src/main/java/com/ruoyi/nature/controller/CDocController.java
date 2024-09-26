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
import com.ruoyi.nature.domain.CDoc;
import com.ruoyi.nature.service.ICDocService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文档Controller
 *
 * @author ruoyi
 * @date 2021-11-25
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/doc" )
public class CDocController extends BaseController {

    private final ICDocService iCDocService;

    /**
     * 查询文档列表
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(CDoc cDoc)
    {
        startPage();
        LambdaQueryWrapper<CDoc> lqw = new LambdaQueryWrapper<CDoc>();
        if (StringUtils.isNotBlank(cDoc.getDetail())){
            lqw.eq(CDoc::getDetail ,cDoc.getDetail());
        }
        if (cDoc.getType() != null){
            lqw.eq(CDoc::getType ,cDoc.getType());
        }
        if (cDoc.getDeleted() != null){
            lqw.eq(CDoc::getDeleted ,cDoc.getDeleted());
        }
        if (cDoc.getViews() != null){
            lqw.eq(CDoc::getViews ,cDoc.getViews());
        }
        List<CDoc> list = iCDocService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出文档列表
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:export')" )
    @Log(title = "文档" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CDoc cDoc) {
        LambdaQueryWrapper<CDoc> lqw = new LambdaQueryWrapper<CDoc>(cDoc);
        List<CDoc> list = iCDocService.list(lqw);
        ExcelUtil<CDoc> util = new ExcelUtil<CDoc>(CDoc. class);
        return util.exportExcel(list, "doc" );
    }

    /**
     * 获取文档详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) Long id) {
        return R.success(iCDocService.getById(id));
    }

    /**
     * 新增文档
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:add')" )
    @Log(title = "文档" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CDoc cDoc) {
        return toAjax(iCDocService.save(cDoc) ? 1 : 0);
    }

    /**
     * 修改文档
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:edit')" )
    @Log(title = "文档" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CDoc cDoc) {
        return toAjax(iCDocService.updateById(cDoc) ? 1 : 0);
    }

    /**
     * 删除文档
     */
    @PreAuthorize("@ss.hasPermi('nature:doc:remove')" )
    @Log(title = "文档" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable Long[] ids) {
        return toAjax(iCDocService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
