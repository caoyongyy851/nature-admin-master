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
import com.ruoyi.nature.domain.COrder;
import com.ruoyi.nature.service.ICOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单Controller
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/nature/order" )
public class COrderController extends BaseController {

    private final ICOrderService iCOrderService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('nature:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(COrder cOrder)
    {
        startPage();
        LambdaQueryWrapper<COrder> lqw = new LambdaQueryWrapper<COrder>();
        if (StringUtils.isNotBlank(cOrder.getOrderNo())){
            lqw.eq(COrder::getOrderNo ,cOrder.getOrderNo());
        }
        if (cOrder.getPrice() != null){
            lqw.eq(COrder::getPrice ,cOrder.getPrice());
        }
        if (cOrder.getStatus() != null){
            lqw.eq(COrder::getStatus ,cOrder.getStatus());
        }
        if (cOrder.getCode() != null){
            lqw.eq(COrder::getCode ,cOrder.getCode());
        }
        if (cOrder.getPayTime() != null){
            lqw.eq(COrder::getPayTime ,cOrder.getPayTime());
        }
        if (cOrder.getPayment() != null){
            lqw.eq(COrder::getPayment ,cOrder.getPayment());
        }
        if (cOrder.getUserId() != null){
            lqw.eq(COrder::getUserId ,cOrder.getUserId());
        }
        if (StringUtils.isNotBlank(cOrder.getPaymentNo())){
            lqw.eq(COrder::getPaymentNo ,cOrder.getPaymentNo());
        }
        if (cOrder.getIsInvoice() != null){
            lqw.eq(COrder::getIsInvoice ,cOrder.getIsInvoice());
        }
        if (cOrder.getUserCouponId() != null){
            lqw.eq(COrder::getUserCouponId ,cOrder.getUserCouponId());
        }
        if (cOrder.getUserCash() != null){
            lqw.eq(COrder::getUserCash ,cOrder.getUserCash());
        }
        List<COrder> list = iCOrderService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @PreAuthorize("@ss.hasPermi('nature:order:export')" )
    @Log(title = "订单" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public R export(COrder cOrder) {
        LambdaQueryWrapper<COrder> lqw = new LambdaQueryWrapper<COrder>(cOrder);
        List<COrder> list = iCOrderService.list(lqw);
        ExcelUtil<COrder> util = new ExcelUtil<COrder>(COrder. class);
        return util.exportExcel(list, "order" );
    }

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('nature:order:query')" )
    @GetMapping(value = "/{id}" )
    public R getInfo(@PathVariable("id" ) String id) {
        return R.success(iCOrderService.getById(id));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('nature:order:add')" )
    @Log(title = "订单" , businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody COrder cOrder) {
        return toAjax(iCOrderService.save(cOrder) ? 1 : 0);
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('nature:order:edit')" )
    @Log(title = "订单" , businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody COrder cOrder) {
        return toAjax(iCOrderService.updateById(cOrder) ? 1 : 0);
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('nature:order:remove')" )
    @Log(title = "订单" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public R remove(@PathVariable String[] ids) {
        return toAjax(iCOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }


    /**
     * 核销订单
     */
    @Log(title = "核销订单" , businessType = BusinessType.UPDATE)
    @GetMapping("/auditCode/{id}" )
    public R auditCode(@PathVariable String id) {
        iCOrderService.auditCode(id);
        return R.success("核销订单成功");
    }

}
