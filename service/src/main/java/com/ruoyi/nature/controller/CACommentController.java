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
import com.ruoyi.nature.domain.CAComment;
import com.ruoyi.nature.service.ICACommentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动评论Controller
 *
 * @author ruoyi
 * @date 2021-10-08
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/aComment" )
public class CACommentController extends BaseController {

    private final ICACommentService iCACommentService;

    /**
     * 查询活动评论列表
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CAComment cAComment)
    {
        startPage();
        LambdaQueryWrapper<CAComment> lqw = new LambdaQueryWrapper<CAComment>();
        if (StringUtils.isNotBlank(cAComment.getPid())){
            lqw.eq(CAComment::getPid ,cAComment.getPid());
        }
        if (cAComment.getDeleted() != null){
            lqw.eq(CAComment::getDeleted ,cAComment.getDeleted());
        }
        if (StringUtils.isNotBlank(cAComment.getUid())){
            lqw.eq(CAComment::getUid ,cAComment.getUid());
        }
        if (StringUtils.isNotBlank(cAComment.getAid())){
            lqw.eq(CAComment::getAid ,cAComment.getAid());
        }
        if (cAComment.getLikes() != null){
            lqw.eq(CAComment::getLikes ,cAComment.getLikes());
        }
        if (cAComment.getReplies() != null){
            lqw.eq(CAComment::getReplies ,cAComment.getReplies());
        }
        if (StringUtils.isNotBlank(cAComment.getContent())){
            lqw.eq(CAComment::getContent ,cAComment.getContent());
        }
        if (cAComment.getIsread() != null){
            lqw.eq(CAComment::getIsread ,cAComment.getIsread());
        }
        if (cAComment.getChecked() != null){
            lqw.eq(CAComment::getChecked ,cAComment.getChecked());
        }
        List<CAComment> list = iCACommentService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出活动评论列表
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:export')" )
    @Log(title = "活动评论" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CAComment cAComment) {
        LambdaQueryWrapper<CAComment> lqw = new LambdaQueryWrapper<CAComment>(cAComment);
        List<CAComment> list = iCACommentService.list(lqw);
        ExcelUtil<CAComment> util = new ExcelUtil<CAComment>(CAComment. class);
        return util.exportExcel(list, "aComment" );
    }

    /**
     * 获取活动评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCACommentService.getById(id));
    }

    /**
     * 新增活动评论
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:add')" )
    @Log(title = "活动评论" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CAComment cAComment) {
        return toAjax(iCACommentService.save(cAComment) ? 1 : 0);
    }

    /**
     * 修改活动评论
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:edit')" )
    @Log(title = "活动评论" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CAComment cAComment) {
        return toAjax(iCACommentService.updateById(cAComment) ? 1 : 0);
    }

    /**
     * 删除活动评论
     */
    @PreAuthorize("@ss.hasPermi('nature:aComment:remove')" )
    @Log(title = "活动评论" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCACommentService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
