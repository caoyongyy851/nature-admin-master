package com.ruoyi.nature.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.nature.domain.CCategory;
import com.ruoyi.nature.domain.CCompany;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.CashsForm;
import com.ruoyi.nature.service.ICCompanyService;
import com.ruoyi.nature.service.ICUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 微信用户Controller
 *
 * @author ruoyi
 * @date 2020-10-28
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/user")
public class CUserController extends BaseController {

    private final ICUserService iCUserService;

    @Autowired
    private ICCompanyService icCompanyService;

    /**
     * 查询微信用户列表
     */
    @PreAuthorize("@ss.hasPermi('nature:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(CUser cUser) {
        startPage();
        LambdaQueryWrapper<CUser> lqw = new LambdaQueryWrapper<CUser>();
        if (StringUtils.isNotBlank(cUser.getOpenid())) {
            lqw.eq(CUser::getOpenid, cUser.getOpenid());
        }
        if (StringUtils.isNotBlank(cUser.getNickname())) {
            lqw.like(CUser::getNickname, cUser.getNickname());
        }
        if (StringUtils.isNotBlank(cUser.getPhone())) {
            lqw.like(CUser::getPhone, cUser.getPhone());
        }
        if (StringUtils.isNotBlank(cUser.getCompanyId())) {
            lqw.like(CUser::getCompanyId, cUser.getCompanyId());
        }
        if (cUser.getSex() != null) {
            lqw.eq(CUser::getSex, cUser.getSex());
        }
        if (StringUtils.isNotBlank(cUser.getAvatar())) {
            lqw.eq(CUser::getAvatar, cUser.getAvatar());
        }
        if (StringUtils.isNotBlank(cUser.getCity())) {
            lqw.eq(CUser::getCity, cUser.getCity());
        }
        if (StringUtils.isNotBlank(cUser.getSign())) {
            lqw.eq(CUser::getSign, cUser.getSign());
        }
        if (cUser.getDisabled() != null) {
            lqw.eq(CUser::getDisabled, cUser.getDisabled());
        }
        if (cUser.getLastLoginTime() != null) {
            lqw.eq(CUser::getLastLoginTime, cUser.getLastLoginTime());
        }
        if (cUser.getCheats() != null) {
            lqw.eq(CUser::getCheats, cUser.getCheats());
        }
        if (cUser.getDeleted() != null) {
            lqw.eq(CUser::getDeleted, cUser.getDeleted());
        }
        List<CUser> list = iCUserService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出微信用户列表
     */
    @PreAuthorize("@ss.hasPermi('nature:user:export')")
    @Log(title = "微信用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public R export(CUser cUser) {

        LambdaQueryWrapper<CUser> lqw = new LambdaQueryWrapper<CUser>(cUser);
        List<CUser> list = iCUserService.list(lqw);
        ExcelUtil<CUser> util = new ExcelUtil<CUser>(CUser.class);
        return util.exportExcel(list, "user");
    }

    /**
     * 获取微信用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:user:query')")
    @GetMapping(value = "/{id}")
    public R getInfo(@PathVariable("id") String id) {
        return R.success(iCUserService.getById(id));
    }

    /**
     * 新增微信用户
     */
    @PreAuthorize("@ss.hasPermi('nature:user:add')")
    @Log(title = "微信用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CUser cUser) {
        return toAjax(iCUserService.save(cUser) ? 1 : 0);
    }

    /**
     * 修改微信用户
     */
    @PreAuthorize("@ss.hasPermi('nature:user:edit')")
    @Log(title = "微信用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CUser cUser) {
        return toAjax(iCUserService.updateById(cUser) ? 1 : 0);
    }

    /**
     * 删除微信用户
     */
    @PreAuthorize("@ss.hasPermi('nature:user:remove')")
    @Log(title = "微信用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCUserService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 加/减款
     */
    @PostMapping("/modifyCashs")
    public R modifyCashs(@RequestBody CashsForm form) {
        iCUserService.modifyCashs(form);
        return R.success("操作成功");
    }

    /**
     * 获取分类下来选
     * @return
     */
    @GetMapping("/companyList")
    public R companyList(){
        List<CCompany> list = icCompanyService.list();
        return R.success(list);
    }
}
