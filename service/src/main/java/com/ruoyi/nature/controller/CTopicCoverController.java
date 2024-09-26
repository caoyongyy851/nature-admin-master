package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.nature.domain.CCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nature.domain.CTopicCover;
import com.ruoyi.nature.service.ICTopicCoverService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 话题封面Controller
 *
 * @author ruoyi
 * @date 2021-12-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/topicCover" )
public class CTopicCoverController extends BaseController {

    private final ICTopicCoverService iCTopicCoverService;

    /**
     * 查询话题封面列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:list')")
    @GetMapping("/list")
    public TableDataInfo list(CTopicCover cTopicCover)
    {
        startPage();
        LambdaQueryWrapper<CTopicCover> lqw = new LambdaQueryWrapper<CTopicCover>();
        if (cTopicCover.getTopicType() != null){
            lqw.eq(CTopicCover::getTopicType ,cTopicCover.getTopicType());
        }
        if (StringUtils.isNotBlank(cTopicCover.getCoverImg())){
            lqw.eq(CTopicCover::getCoverImg ,cTopicCover.getCoverImg());
        }
        if (StringUtils.isNotBlank(cTopicCover.getDeleted())){
            lqw.eq(CTopicCover::getDeleted ,cTopicCover.getDeleted());
        }
        List<CTopicCover> list = iCTopicCoverService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出话题封面列表
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:export')" )
    @Log(title = "话题封面" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CTopicCover cTopicCover) {
        LambdaQueryWrapper<CTopicCover> lqw = new LambdaQueryWrapper<CTopicCover>(cTopicCover);
        List<CTopicCover> list = iCTopicCoverService.list(lqw);
        ExcelUtil<CTopicCover> util = new ExcelUtil<CTopicCover>(CTopicCover. class);
        return util.exportExcel(list, "topicCover" );
    }

    /**
     * 获取话题封面详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) Long id) {
        return R.success(iCTopicCoverService.getById(id));
    }

    /**
     * 新增话题封面
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:add')" )
    @Log(title = "话题封面" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CTopicCover cTopicCover) {
        QueryWrapper<CTopicCover> wrapper = new QueryWrapper<>();
        wrapper.eq("topic_type", cTopicCover.getTopicType());
        wrapper.eq("deleted", 0);
        CTopicCover one = iCTopicCoverService.getOne(wrapper);
        if (one != null){
            return R.error("已存在该类型的封面图");
        }
        QueryWrapper<CTopicCover> wrapper1 = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        int count = iCTopicCoverService.count(wrapper1);
        if (count >= 4){
            return R.error("已经不能再新增了");
        }
        return toAjax(iCTopicCoverService.save(cTopicCover) ? 1 : 0);
    }

    /**
     * 修改话题封面
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:edit')" )
    @Log(title = "话题封面" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CTopicCover cTopicCover) {
        return toAjax(iCTopicCoverService.updateById(cTopicCover) ? 1 : 0);
    }

    /**
     * 删除话题封面
     */
    @PreAuthorize("@ss.hasPermi('nature:topicCover:remove')" )
    @Log(title = "话题封面" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable Long[] ids) {
        return toAjax(iCTopicCoverService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 图片上传
     */
    @Log(title = "图片上传", businessType = BusinessType.UPDATE)
    @CrossOrigin
    @PostMapping("/file")
    public R file(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String imgUrl = FileUploadUtils.upload(RuoYiConfig.getCommonPath(), file);
            R r = R.success();
            r.put("imgUrl", imgUrl);
            return r;
        }

        return R.error("上传图片异常，请联系管理员");
    }

    /**
     * 获取类型下拉选
     * @return
     */
    @GetMapping("/optionList")
    public R optionList(){
        List<CTopicCover> list = iCTopicCoverService.list();
        return R.success(list);
    }
}
