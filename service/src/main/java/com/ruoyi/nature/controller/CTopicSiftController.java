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
import com.ruoyi.nature.domain.CTopicSift;
import com.ruoyi.nature.service.ICTopicSiftService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 精选话题Controller
 *
 * @author ruoyi
 * @date 2022-01-18
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/topicSift" )
public class CTopicSiftController extends BaseController {

    private final ICTopicSiftService iCTopicSiftService;

    /**
     * 查询精选话题列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:list')")
    @GetMapping("/list")
    public TableDataInfo list(CTopicSift cTopicSift)
    {
        startPage();
        LambdaQueryWrapper<CTopicSift> lqw = new LambdaQueryWrapper<CTopicSift>();
        if (StringUtils.isNotBlank(cTopicSift.getTopicId())){
            lqw.eq(CTopicSift::getTopicId ,cTopicSift.getTopicId());
        }
        if (cTopicSift.getSiftOrder() != null){
            lqw.eq(CTopicSift::getSiftOrder ,cTopicSift.getSiftOrder());
        }
        if (cTopicSift.getDeleted() != null){
            lqw.eq(CTopicSift::getDeleted ,cTopicSift.getDeleted());
        }
        List<CTopicSift> list = iCTopicSiftService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出精选话题列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:export')" )
    @Log(title = "精选话题" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CTopicSift cTopicSift) {
        LambdaQueryWrapper<CTopicSift> lqw = new LambdaQueryWrapper<CTopicSift>(cTopicSift);
        List<CTopicSift> list = iCTopicSiftService.list(lqw);
        ExcelUtil<CTopicSift> util = new ExcelUtil<CTopicSift>(CTopicSift. class);
        return util.exportExcel(list, "topicSift" );
    }

    /**
     * 获取精选话题详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCTopicSiftService.getById(id));
    }

    /**
     * 新增精选话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:add')" )
    @Log(title = "精选话题" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CTopicSift cTopicSift) {
        return toAjax(iCTopicSiftService.save(cTopicSift) ? 1 : 0);
    }

    /**
     * 修改精选话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:edit')" )
    @Log(title = "精选话题" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CTopicSift cTopicSift) {
        return toAjax(iCTopicSiftService.updateById(cTopicSift) ? 1 : 0);
    }

    /**
     * 删除精选话题
     */
    @PreAuthorize("@ss.hasPermi('nature:topicSift:remove')" )
    @Log(title = "精选话题" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCTopicSiftService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
