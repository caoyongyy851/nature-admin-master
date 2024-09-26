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
import com.ruoyi.nature.domain.CSche;
import com.ruoyi.nature.dto.PlaceForm;
import com.ruoyi.nature.dto.ScheForm;
import com.ruoyi.nature.service.ICScheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.nature.domain.CPlace;
import com.ruoyi.nature.service.ICPlaceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 场地Controller
 *
 * @author ruoyi
 * @date 2021-09-22
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/place")
public class CPlaceController extends BaseController {

    private final ICPlaceService iCPlaceService;
    @Autowired
    private ICScheService icScheService;

    /**
     * 查询场地列表
     */
    @PreAuthorize("@ss.hasPermi('nature:place:list')")
    @GetMapping("/list")
    public TableDataInfo list(CPlace cPlace) {
        startPage();
        LambdaQueryWrapper<CPlace> lqw = new LambdaQueryWrapper<CPlace>();
        if (StringUtils.isNotBlank(cPlace.getName())) {
            lqw.like(CPlace::getName, cPlace.getName());
        }
        if (StringUtils.isNotBlank(cPlace.getCategoryId())) {
            lqw.eq(CPlace::getCategoryId, cPlace.getCategoryId());
        }
        if (StringUtils.isNotBlank(cPlace.getDetail())) {
            lqw.eq(CPlace::getDetail, cPlace.getDetail());
        }
        if (cPlace.getStatus() != null) {
            lqw.eq(CPlace::getStatus, cPlace.getStatus());
        }
        if (cPlace.getAllowReturn() != null) {
            lqw.eq(CPlace::getAllowReturn, cPlace.getAllowReturn());
        }
        if (StringUtils.isNotBlank(cPlace.getDescrible())) {
            lqw.eq(CPlace::getDescrible, cPlace.getDescrible());
        }
        if (StringUtils.isNotBlank(cPlace.getLabel())) {
            lqw.eq(CPlace::getLabel, cPlace.getLabel());
        }
        if (cPlace.getOperator() != null) {
            lqw.eq(CPlace::getOperator, cPlace.getOperator());
        }
        if (StringUtils.isNotBlank(cPlace.getImages())) {
            lqw.eq(CPlace::getImages, cPlace.getImages());
        }
        if (cPlace.getViews() != null) {
            lqw.eq(CPlace::getViews, cPlace.getViews());
        }
        if (cPlace.getCollects() != null) {
            lqw.eq(CPlace::getCollects, cPlace.getCollects());
        }
        if (cPlace.getBugs() != null) {
            lqw.eq(CPlace::getBugs, cPlace.getBugs());
        }
        if (StringUtils.isNotBlank(cPlace.getCoordinate())) {
            lqw.eq(CPlace::getCoordinate, cPlace.getCoordinate());
        }
        if (StringUtils.isNotBlank(cPlace.getAddress())) {
            lqw.eq(CPlace::getAddress, cPlace.getAddress());
        }
        if (StringUtils.isNotNull(cPlace.getStart())) {
            lqw.eq(CPlace::getStart, cPlace.getStart());
        }
        if (StringUtils.isNotNull(cPlace.getEnd())) {
            lqw.eq(CPlace::getEnd, cPlace.getEnd());
        }
        if (cPlace.getDayType() != null) {
            lqw.eq(CPlace::getDayType, cPlace.getDayType());
        }
        if (cPlace.getScheType() != null) {
            lqw.eq(CPlace::getScheType, cPlace.getScheType());
        }
        List<CPlace> list = iCPlaceService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出场地列表
     */
    @PreAuthorize("@ss.hasPermi('nature:place:export')")
    @Log(title = "场地", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(CPlace cPlace) {
        LambdaQueryWrapper<CPlace> lqw = new LambdaQueryWrapper<CPlace>(cPlace);
        List<CPlace> list = iCPlaceService.list(lqw);
        ExcelUtil<CPlace> util = new ExcelUtil<CPlace>(CPlace.class);
        return util.exportExcel(list, "place");
    }

    /**
     * 获取场地详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:place:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        PlaceForm form = new PlaceForm();
        CPlace place = iCPlaceService.getById(id);
        BeanUtils.copyProperties(place, form);
        QueryWrapper<CSche> wrapper = new QueryWrapper<>();
        wrapper.eq("place_id", id).gt("days", 0);
        List<CSche> list = icScheService.list(wrapper);
        if (list != null && list.size() > 0) {
            CSche sche = list.get(0);
            form.setDays(sche.getDays());
            form.setPrice(sche.getPrice());
            form.setSurplus(sche.getSurplus());
        }
        return R.success(form);
    }

    /**
     * 新增场地
     */
    @PreAuthorize("@ss.hasPermi('nature:place:add')")
    @Log(title = "场地", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PlaceForm cPlace) {
        boolean save = iCPlaceService.save(cPlace);
        if (save) {
            if ("0".equals(cPlace.getScheType())) {
                if (cPlace.getDays() > 0) {
                    CSche sche = new CSche();
                    sche.setPrice(cPlace.getPrice());
                    sche.setSurplus(cPlace.getSurplus());
                    sche.setDays(cPlace.getDays());
                    sche.setPlaceId(cPlace.getId());
                    icScheService.save(sche);
                }
            } else if ("1".equals(cPlace.getScheType())) {
                // TODO 手动设置到期
            }
        }
        return toAjax(save ? 1 : 0);
    }

    /**
     * 修改场地
     */
    @PreAuthorize("@ss.hasPermi('nature:place:edit')")
    @Log(title = "场地", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PlaceForm cPlace) {
        boolean update = iCPlaceService.updateById(cPlace);
        if (update) {
            if ("0".equals(cPlace.getScheType())) {
                if (StringUtils.isNull(cPlace.getDays())) {
                    return R.error("请输入天数");
                }
                if (StringUtils.isNull(cPlace.getSurplus())) {
                    return R.error("请输入票数");
                }
                if (StringUtils.isNull(cPlace.getPrice())) {
                    return R.error("请输入预约价");
                }
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("place_id", cPlace.getId());
                icScheService.remove(wrapper);

                CSche sche = new CSche();
                sche.setPrice(cPlace.getPrice());
                sche.setSurplus(cPlace.getSurplus());
                sche.setDays(cPlace.getDays());
                sche.setPlaceId(cPlace.getId());
                icScheService.save(sche);
            }
        }
        return toAjax(update ? 1 : 0);
    }

    /**
     * 删除场地
     */
    @PreAuthorize("@ss.hasPermi('nature:place:remove')")
    @Log(title = "场地", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCPlaceService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 玩场图片上传
     */
    @Log(title = "玩场图片上传", businessType = BusinessType.UPDATE)
    @CrossOrigin
    @PostMapping("/file")
    public R file(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String imgUrl = FileUploadUtils.upload(RuoYiConfig.getPlacePath(), file);
            R r = R.success();
            r.put("imgUrl", imgUrl);
            return r;
        }

        return R.error("上传图片异常，请联系管理员");
    }

    /**
     * 玩场图片上传
     */
    @Log(title = "玩场视频上传", businessType = BusinessType.UPDATE)
    @CrossOrigin
    @PostMapping("/file_video")
    public R fileVideo(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String videoUrl = FileUploadUtils.uploadVideo(RuoYiConfig.getPlacePath(), file);
            R r = R.success();
            r.put("videoUrl", videoUrl);
            return r;
        }

        return R.error("上传图片异常，请联系管理员");
    }


    /**
     * 档期保存
     */
    @Log(title = "档期保存", businessType = BusinessType.UPDATE)
    @CrossOrigin
    @PostMapping("/saveSche")
    public R saveSche(@RequestBody ScheForm form) {
        if (StringUtils.isEmpty(form.getId())) {
            return R.error("玩场id为空");
        }
        CPlace place = iCPlaceService.getById(form.getId());

        if (!"1".equals(place.getScheType())) {
            return R.error("玩场档期类型不正确");
        }
        iCPlaceService.saveSche(form);
        return R.success("操作成功");
    }
}
