package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nature.domain.CSwiperTopic;
import com.ruoyi.nature.service.ICSwiperTopicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 帖子轮播图Controller
 *
 * @author ruoyi
 * @date 2021-11-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/swiperTopic" )
public class CSwiperTopicController extends BaseController {

    private final ICSwiperTopicService iCSwiperTopicService;

    /**
     * 查询帖子轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:list')")
    @GetMapping("/list")
    public TableDataInfo list(CSwiperTopic cSwiperTopic)
    {
        startPage();
        LambdaQueryWrapper<CSwiperTopic> lqw = new LambdaQueryWrapper<CSwiperTopic>();
        if (StringUtils.isNotBlank(cSwiperTopic.getTopicId())){
            lqw.eq(CSwiperTopic::getTopicId ,cSwiperTopic.getTopicId());
        }
        if (cSwiperTopic.getSwiperOrder() != null){
            lqw.eq(CSwiperTopic::getSwiperOrder ,cSwiperTopic.getSwiperOrder());
        }
        List<CSwiperTopic> list = iCSwiperTopicService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出帖子轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:export')" )
    @Log(title = "帖子轮播图" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CSwiperTopic cSwiperTopic) {
        LambdaQueryWrapper<CSwiperTopic> lqw = new LambdaQueryWrapper<CSwiperTopic>(cSwiperTopic);
        List<CSwiperTopic> list = iCSwiperTopicService.list(lqw);
        ExcelUtil<CSwiperTopic> util = new ExcelUtil<CSwiperTopic>(CSwiperTopic. class);
        return util.exportExcel(list, "swiperTopic" );
    }

    /**
     * 获取帖子轮播图详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) Long id) {
        return R.success(iCSwiperTopicService.getById(id));
    }

    /**
     * 新增帖子轮播图
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:add')" )
    @Log(title = "帖子轮播图" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CSwiperTopic cSwiperTopic) {
        return toAjax(iCSwiperTopicService.save(cSwiperTopic) ? 1 : 0);
    }

    /**
     * 修改帖子轮播图
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:edit')" )
    @Log(title = "帖子轮播图" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CSwiperTopic cSwiperTopic) {
        return toAjax(iCSwiperTopicService.updateById(cSwiperTopic) ? 1 : 0);
    }

    /**
     * 删除帖子轮播图
     */
    @PreAuthorize("@ss.hasPermi('nature:swiperTopic:remove')" )
    @Log(title = "帖子轮播图" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable Long[] ids) {
        return toAjax(iCSwiperTopicService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
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
}
