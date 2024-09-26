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
import com.ruoyi.nature.domain.CActOrder;
import com.ruoyi.nature.service.ICActOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单Controller
 *
 * @author ruoyi
 * @date 2022-02-26
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/actOrder" )
public class CActOrderController extends BaseController {

    private final ICActOrderService iCActOrderService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(CActOrder cActOrder)
    {
        startPage();
        LambdaQueryWrapper<CActOrder> lqw = new LambdaQueryWrapper<CActOrder>();
        if (StringUtils.isNotBlank(cActOrder.getOrderNo())){
            lqw.eq(CActOrder::getOrderNo ,cActOrder.getOrderNo());
        }
        if (StringUtils.isNotBlank(cActOrder.getActId())){
            lqw.eq(CActOrder::getActId ,cActOrder.getActId());
        }
        if (cActOrder.getPrice() != null){
            lqw.eq(CActOrder::getPrice ,cActOrder.getPrice());
        }
        if (cActOrder.getStatus() != null){
            lqw.eq(CActOrder::getStatus ,cActOrder.getStatus());
        }
        if (cActOrder.getPayTime() != null){
            lqw.eq(CActOrder::getPayTime ,cActOrder.getPayTime());
        }
        if (cActOrder.getPayment() != null){
            lqw.eq(CActOrder::getPayment ,cActOrder.getPayment());
        }
        if (cActOrder.getPerson() != null){
            lqw.eq(CActOrder::getPerson ,cActOrder.getPerson());
        }
        if (StringUtils.isNotBlank(cActOrder.getUserId())){
            lqw.eq(CActOrder::getUserId ,cActOrder.getUserId());
        }
        if (StringUtils.isNotBlank(cActOrder.getPhone())){
            lqw.eq(CActOrder::getPhone ,cActOrder.getPhone());
        }
        if (StringUtils.isNotBlank(cActOrder.getCode())){
            lqw.eq(CActOrder::getCode ,cActOrder.getCode());
        }
        if (StringUtils.isNotBlank(cActOrder.getPaymentNo())){
            lqw.eq(CActOrder::getPaymentNo ,cActOrder.getPaymentNo());
        }
        if (cActOrder.getIsInvoice() != null){
            lqw.eq(CActOrder::getIsInvoice ,cActOrder.getIsInvoice());
        }
        List<CActOrder> list = iCActOrderService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:export')" )
    @Log(title = "订单" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(CActOrder cActOrder) {
        LambdaQueryWrapper<CActOrder> lqw = new LambdaQueryWrapper<CActOrder>(cActOrder);
        List<CActOrder> list = iCActOrderService.list(lqw);
        ExcelUtil<CActOrder> util = new ExcelUtil<CActOrder>(CActOrder. class);
        return util.exportExcel(list, "actOrder" );
    }

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCActOrderService.getById(id));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:add')" )
    @Log(title = "订单" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody CActOrder cActOrder) {
        return toAjax(iCActOrderService.save(cActOrder) ? 1 : 0);
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:edit')" )
    @Log(title = "订单" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody CActOrder cActOrder) {
        return toAjax(iCActOrderService.updateById(cActOrder) ? 1 : 0);
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('nature:actOrder:remove')" )
    @Log(title = "订单" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCActOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
