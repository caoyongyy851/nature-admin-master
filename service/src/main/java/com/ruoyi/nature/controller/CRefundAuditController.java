package com.ruoyi.nature.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.github.binarywang.wxpay.exception.WxPayException;
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
import com.ruoyi.nature.domain.CRefundAudit;
import com.ruoyi.nature.service.ICRefundAuditService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 退款审核Controller
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/refundAudit" )
public class CRefundAuditController extends BaseController {

    private final ICRefundAuditService iCRefundAuditService;

    /**
     * 查询退款审核列表
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:list')")
    @GetMapping("/list")
    public TableDataInfo list(CRefundAudit cRefundAudit)
    {
        startPage();
        LambdaQueryWrapper<CRefundAudit> lqw = new LambdaQueryWrapper<CRefundAudit>();
        if (StringUtils.isNotBlank(cRefundAudit.getOrderId())){
            lqw.eq(CRefundAudit::getOrderId ,cRefundAudit.getOrderId());
        }
        if (StringUtils.isNotBlank(cRefundAudit.getOrderNo())){
            lqw.eq(CRefundAudit::getOrderNo ,cRefundAudit.getOrderNo());
        }
        if (cRefundAudit.getStatus() != null){
            lqw.eq(CRefundAudit::getStatus ,cRefundAudit.getStatus());
        }
        if (cRefundAudit.getType() != null){
            lqw.eq(CRefundAudit::getType ,cRefundAudit.getType());
        }
        if (cRefundAudit.getRefundTime() != null){
            lqw.eq(CRefundAudit::getRefundTime ,cRefundAudit.getRefundTime());
        }
        if (cRefundAudit.getDeleted() != null){
            lqw.eq(CRefundAudit::getDeleted ,cRefundAudit.getDeleted());
        }
        List<CRefundAudit> list = iCRefundAuditService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出退款审核列表
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:export')" )
    @Log(title = "退款审核" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CRefundAudit cRefundAudit) {
        LambdaQueryWrapper<CRefundAudit> lqw = new LambdaQueryWrapper<CRefundAudit>(cRefundAudit);
        List<CRefundAudit> list = iCRefundAuditService.list(lqw);
        ExcelUtil<CRefundAudit> util = new ExcelUtil<CRefundAudit>(CRefundAudit. class);
        return util.exportExcel(list, "refundAudit" );
    }

    /**
     * 获取退款审核详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCRefundAuditService.getById(id));
    }

    /**
     * 新增退款审核
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:add')" )
    @Log(title = "退款审核" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CRefundAudit cRefundAudit) {
        return toAjax(iCRefundAuditService.save(cRefundAudit) ? 1 : 0);
    }

    /**
     * 修改退款审核
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:edit')" )
    @Log(title = "退款审核" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CRefundAudit cRefundAudit) {
        return toAjax(iCRefundAuditService.updateById(cRefundAudit) ? 1 : 0);
    }

    /**
     * 删除退款审核
     */
    @PreAuthorize("@ss.hasPermi('nature:refundAudit:remove')" )
    @Log(title = "退款审核" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCRefundAuditService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 处理退款
     */
//    @PreAuthorize("@ss.hasPermi('nature:refundAudit:remove')" )
    @Log(title = "处理退款" , businessType = BusinessType.DELETE)
    @GetMapping("/audit/{id}/{status}" )
    public R audit(@PathVariable String id, @PathVariable Integer status) throws WxPayException {

        int row = iCRefundAuditService.audit(id, status);

        return toAjax(row);
    }
}
