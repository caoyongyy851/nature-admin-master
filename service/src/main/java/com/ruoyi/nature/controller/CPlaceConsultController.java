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
import com.ruoyi.nature.domain.CPlaceConsult;
import com.ruoyi.nature.service.ICPlaceConsultService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 玩场咨询Controller
 *
 * @author ruoyi
 * @date 2021-12-06
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/placeConsult" )
public class CPlaceConsultController extends BaseController {

    private final ICPlaceConsultService iCPlaceConsultService;

    /**
     * 查询玩场咨询列表
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:list')")
    @GetMapping("/list")
    public TableDataInfo list(CPlaceConsult cPlaceConsult)
    {
        startPage();
        LambdaQueryWrapper<CPlaceConsult> lqw = new LambdaQueryWrapper<CPlaceConsult>();
        if (StringUtils.isNotBlank(cPlaceConsult.getContext())){
            lqw.eq(CPlaceConsult::getContext ,cPlaceConsult.getContext());
        }
        if (StringUtils.isNotBlank(cPlaceConsult.getReply())){
            lqw.eq(CPlaceConsult::getReply ,cPlaceConsult.getReply());
        }
        if (StringUtils.isNotBlank(cPlaceConsult.getPlaceId())){
            lqw.eq(CPlaceConsult::getPlaceId ,cPlaceConsult.getPlaceId());
        }
        if (StringUtils.isNotBlank(cPlaceConsult.getUserId())){
            lqw.eq(CPlaceConsult::getUserId ,cPlaceConsult.getUserId());
        }
        if (cPlaceConsult.getDeleted() != null){
            lqw.eq(CPlaceConsult::getDeleted ,cPlaceConsult.getDeleted());
        }
        List<CPlaceConsult> list = iCPlaceConsultService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出玩场咨询列表
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:export')" )
    @Log(title = "玩场咨询" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CPlaceConsult cPlaceConsult) {
        LambdaQueryWrapper<CPlaceConsult> lqw = new LambdaQueryWrapper<CPlaceConsult>(cPlaceConsult);
        List<CPlaceConsult> list = iCPlaceConsultService.list(lqw);
        ExcelUtil<CPlaceConsult> util = new ExcelUtil<CPlaceConsult>(CPlaceConsult. class);
        return util.exportExcel(list, "placeConsult" );
    }

    /**
     * 获取玩场咨询详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) Long id) {
        return R.success(iCPlaceConsultService.getById(id));
    }

    /**
     * 新增玩场咨询
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:add')" )
    @Log(title = "玩场咨询" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CPlaceConsult cPlaceConsult) {
        return toAjax(iCPlaceConsultService.save(cPlaceConsult) ? 1 : 0);
    }

    /**
     * 修改玩场咨询
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:edit')" )
    @Log(title = "玩场咨询" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CPlaceConsult cPlaceConsult) {
        return toAjax(iCPlaceConsultService.updateById(cPlaceConsult) ? 1 : 0);
    }

    /**
     * 删除玩场咨询
     */
    @PreAuthorize("@ss.hasPermi('nature:placeConsult:remove')" )
    @Log(title = "玩场咨询" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable Long[] ids) {
        return toAjax(iCPlaceConsultService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
