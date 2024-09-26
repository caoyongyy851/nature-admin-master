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
import com.ruoyi.nature.domain.CCard;
import com.ruoyi.nature.service.ICCardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 帖子Controller
 *
 * @author ruoyi
 * @date 2021-09-06
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/card" )
public class CCardController extends BaseController {

    private final ICCardService iCCardService;

    /**
     * 查询帖子列表
     */
    @PreAuthorize("@ss.hasPermi('nature:card:list')")
    @GetMapping("/list")
    public TableDataInfo list(CCard cCard)
    {
        startPage();
        LambdaQueryWrapper<CCard> lqw = new LambdaQueryWrapper<CCard>();
        if (StringUtils.isNotBlank(cCard.getUid())){
            lqw.eq(CCard::getUid ,cCard.getUid());
        }
        if (StringUtils.isNotBlank(cCard.getTitle())){
            lqw.eq(CCard::getTitle ,cCard.getTitle());
        }
        if (cCard.getTime() != null){
            lqw.eq(CCard::getTime ,cCard.getTime());
        }
        if (StringUtils.isNotBlank(cCard.getPosition())){
            lqw.eq(CCard::getPosition ,cCard.getPosition());
        }
        if (StringUtils.isNotBlank(cCard.getPid())){
            lqw.eq(CCard::getPid ,cCard.getPid());
        }
        if (StringUtils.isNotBlank(cCard.getImgs())){
            lqw.eq(CCard::getImgs ,cCard.getImgs());
        }
        if (cCard.getImgsNum() != null){
            lqw.eq(CCard::getImgsNum ,cCard.getImgsNum());
        }
        if (StringUtils.isNotBlank(cCard.getDetail())){
            lqw.eq(CCard::getDetail ,cCard.getDetail());
        }
        if (cCard.getViews() != null){
            lqw.eq(CCard::getViews ,cCard.getViews());
        }
        if (cCard.getLikes() != null){
            lqw.eq(CCard::getLikes ,cCard.getLikes());
        }
        if (cCard.getComments() != null){
            lqw.eq(CCard::getComments ,cCard.getComments());
        }
        if (cCard.getChecked() != null){
            lqw.eq(CCard::getChecked ,cCard.getChecked());
        }
        if (cCard.getDeleted() != null){
            lqw.eq(CCard::getDeleted ,cCard.getDeleted());
        }
        List<CCard> list = iCCardService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出帖子列表
     */
    @PreAuthorize("@ss.hasPermi('nature:card:export')" )
    @Log(title = "帖子" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CCard cCard) {
        LambdaQueryWrapper<CCard> lqw = new LambdaQueryWrapper<CCard>(cCard);
        List<CCard> list = iCCardService.list(lqw);
        ExcelUtil<CCard> util = new ExcelUtil<CCard>(CCard. class);
        return util.exportExcel(list, "card" );
    }

    /**
     * 获取帖子详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:card:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCCardService.getById(id));
    }

    /**
     * 新增帖子
     */
    @PreAuthorize("@ss.hasPermi('nature:card:add')" )
    @Log(title = "帖子" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CCard cCard) {
        return toAjax(iCCardService.save(cCard) ? 1 : 0);
    }

    /**
     * 修改帖子
     */
    @PreAuthorize("@ss.hasPermi('nature:card:edit')" )
    @Log(title = "帖子" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CCard cCard) {
        return toAjax(iCCardService.updateById(cCard) ? 1 : 0);
    }

    /**
     * 删除帖子
     */
    @PreAuthorize("@ss.hasPermi('nature:card:remove')" )
    @Log(title = "帖子" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCCardService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
