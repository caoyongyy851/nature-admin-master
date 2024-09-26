package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.nature.service.ICCommentService;
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
import com.ruoyi.nature.domain.CComment;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 评论Controller
 *
 * @author ruoyi
 * @date 2021-08-14
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/comment" )
public class CCommentController extends BaseController {

    private final ICCommentService iCCommentService;

    /**
     * 查询评论列表
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CComment cComment)
    {
        startPage();
        LambdaQueryWrapper<CComment> lqw = new LambdaQueryWrapper<CComment>();
        if (cComment.getDeleted() != null){
            lqw.eq(CComment::getDeleted ,cComment.getDeleted());
        }
        if (StringUtils.isNotBlank(cComment.getUid())){
            lqw.eq(CComment::getUid ,cComment.getUid());
        }

        if (cComment.getLikes() != null){
            lqw.eq(CComment::getLikes ,cComment.getLikes());
        }
        if (StringUtils.isNotBlank(cComment.getContent())){
            lqw.eq(CComment::getContent ,cComment.getContent());
        }
        if (StringUtils.isNotNull(cComment.getIsread())){
            lqw.eq(CComment::getIsread ,cComment.getIsread());
        }
        if (StringUtils.isNotNull(cComment.getChecked())){
            lqw.eq(CComment::getChecked ,cComment.getChecked());
        }
        List<CComment> list = iCCommentService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出评论列表
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:export')" )
    @Log(title = "评论" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CComment cComment) {
        LambdaQueryWrapper<CComment> lqw = new LambdaQueryWrapper<CComment>(cComment);
        List<CComment> list = iCCommentService.list(lqw);
        ExcelUtil<CComment> util = new ExcelUtil<CComment>(CComment. class);
        return util.exportExcel(list, "comment" );
    }

    /**
     * 获取评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCCommentService.getById(id));
    }

    /**
     * 新增评论
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:add')" )
    @Log(title = "评论" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CComment cComment) {
        return toAjax(iCCommentService.save(cComment) ? 1 : 0);
    }

    /**
     * 修改评论
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:edit')" )
    @Log(title = "评论" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CComment cComment) {
        return toAjax(iCCommentService.updateById(cComment) ? 1 : 0);
    }

    /**
     * 删除评论
     */
    @PreAuthorize("@ss.hasPermi('nature:comment:remove')" )
    @Log(title = "评论" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCCommentService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
