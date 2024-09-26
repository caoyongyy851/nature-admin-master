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
import com.ruoyi.nature.domain.CTopic;
import com.ruoyi.nature.service.ICTopicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 话题Controller
 *
 * @author ruoyi
 * @date 2021-11-03
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/topic" )
public class CTopicController extends BaseController {

    private final ICTopicService iCTopicService;

    /**
     * 查询话题列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:list')")
    @GetMapping("/list")
    public TableDataInfo list(CTopic cTopic)
    {
        startPage();
        LambdaQueryWrapper<CTopic> lqw = new LambdaQueryWrapper<CTopic>();
        if (StringUtils.isNotBlank(cTopic.getUid())){
            lqw.eq(CTopic::getUid ,cTopic.getUid());
        }
        if (StringUtils.isNotBlank(cTopic.getTitle())){
            lqw.eq(CTopic::getTitle ,cTopic.getTitle());
        }
        if (StringUtils.isNotBlank(cTopic.getCover())){
            lqw.eq(CTopic::getCover ,cTopic.getCover());
        }
        if (StringUtils.isNotBlank(cTopic.getImage())){
            lqw.eq(CTopic::getImage ,cTopic.getImage());
        }
        if (StringUtils.isNotBlank(cTopic.getSimpleDesc())){
            lqw.eq(CTopic::getSimpleDesc ,cTopic.getSimpleDesc());
        }
        if (StringUtils.isNotBlank(cTopic.getDetail())){
            lqw.eq(CTopic::getDetail ,cTopic.getDetail());
        }
        if (cTopic.getViews() != null){
            lqw.eq(CTopic::getViews ,cTopic.getViews());
        }
        if (cTopic.getChecked() != null){
            lqw.eq(CTopic::getChecked ,cTopic.getChecked());
        }
        if (cTopic.getDeleted() != null){
            lqw.eq(CTopic::getDeleted ,cTopic.getDeleted());
        }
        if (cTopic.getType() != null){
            lqw.eq(CTopic::getType ,cTopic.getType());
        }
        List<CTopic> list = iCTopicService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出话题列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:export')" )
    @Log(title = "话题" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CTopic cTopic) {
        LambdaQueryWrapper<CTopic> lqw = new LambdaQueryWrapper<CTopic>(cTopic);
        List<CTopic> list = iCTopicService.list(lqw);
        ExcelUtil<CTopic> util = new ExcelUtil<CTopic>(CTopic. class);
        return util.exportExcel(list, "topic" );
    }

    /**
     * 获取话题详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCTopicService.getById(id));
    }

    /**
     * 新增话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:add')" )
    @Log(title = "话题" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CTopic cTopic) {
        return toAjax(iCTopicService.save(cTopic) ? 1 : 0);
    }

    /**
     * 修改话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:edit')" )
    @Log(title = "话题" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CTopic cTopic) {
        return toAjax(iCTopicService.updateById(cTopic) ? 1 : 0);
    }

    /**
     * 删除话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topic:remove')" )
    @Log(title = "话题" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCTopicService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
