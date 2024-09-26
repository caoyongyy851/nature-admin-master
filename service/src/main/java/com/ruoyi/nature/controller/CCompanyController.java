package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.service.ICUserService;
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
import com.ruoyi.nature.domain.CCompany;
import com.ruoyi.nature.service.ICCompanyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公司/机构Controller
 *
 * @author ruoyi
 * @date 2021-12-05
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/company" )
public class CCompanyController extends BaseController {

    private final ICCompanyService iCCompanyService;

    @Autowired
    private ICUserService icUserService;

    /**
     * 查询公司/机构列表
     */
    @PreAuthorize("@ss.hasPermi('nature:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(CCompany cCompany)
    {
        startPage();
        LambdaQueryWrapper<CCompany> lqw = new LambdaQueryWrapper<CCompany>();
        if (StringUtils.isNotBlank(cCompany.getCompanyName())){
            lqw.like(CCompany::getCompanyName ,cCompany.getCompanyName());
        }
        if (StringUtils.isNotBlank(cCompany.getAddress())){
            lqw.eq(CCompany::getAddress ,cCompany.getAddress());
        }
        if (StringUtils.isNotBlank(cCompany.getPhone())){
            lqw.eq(CCompany::getPhone ,cCompany.getPhone());
        }
        if (StringUtils.isNotBlank(cCompany.getUserId())){
            lqw.eq(CCompany::getUserId ,cCompany.getUserId());
        }
        if (cCompany.getDeleted() != null){
            lqw.eq(CCompany::getDeleted ,cCompany.getDeleted());
        }
        if (cCompany.getStatus() != null){
            lqw.eq(CCompany::getStatus ,cCompany.getStatus());
        }
        List<CCompany> list = iCCompanyService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出公司/机构列表
     */
    @PreAuthorize("@ss.hasPermi('nature:company:export')" )
    @Log(title = "公司/机构" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CCompany cCompany) {
        LambdaQueryWrapper<CCompany> lqw = new LambdaQueryWrapper<CCompany>(cCompany);
        List<CCompany> list = iCCompanyService.list(lqw);
        ExcelUtil<CCompany> util = new ExcelUtil<CCompany>(CCompany. class);
        return util.exportExcel(list, "company" );
    }

    /**
     * 获取公司/机构详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:company:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCCompanyService.getById(id));
    }

    /**
     * 新增公司/机构
     */
    @PreAuthorize("@ss.hasPermi('nature:company:add')" )
    @Log(title = "公司/机构" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CCompany cCompany) {
        return toAjax(iCCompanyService.save(cCompany) ? 1 : 0);
    }

    /**
     * 修改公司/机构
     */
    @PreAuthorize("@ss.hasPermi('nature:company:edit')" )
    @Log(title = "公司/机构" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CCompany cCompany) {
        return toAjax(iCCompanyService.updateById(cCompany) ? 1 : 0);
    }

    /**
     * 删除公司/机构
     */
    @PreAuthorize("@ss.hasPermi('nature:company:remove')" )
    @Log(title = "公司/机构" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCCompanyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 审核
     */
//    @PreAuthorize("@ss.hasPermi('nature:refundAudit:remove')" )
    @Log(title = "审核" , businessType = BusinessType.DELETE)
    @GetMapping("/audit/{id}/{status}" )
    public R audit(@PathVariable String id, @PathVariable Integer status) {

        CCompany company = iCCompanyService.getById(id);
        if (company == null){
            R.error("操作失败");
        }
        if (status == 1){
            company.setStatus(1);
            iCCompanyService.updateById(company);
            CUser cUser = icUserService.getById(company.getUserId());
            cUser.setCompanyId(company.getId());
            icUserService.updateById(cUser);
        }else if(status == 2){
            company.setStatus(2);
            iCCompanyService.updateById(company);
        }

        return R.success("操作成功");
    }
}
