package com.ruoyi.nature.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.nature.config.WxMineProperties;
import com.ruoyi.nature.domain.COrder;
import com.ruoyi.nature.domain.CRefundAudit;
import com.ruoyi.nature.mapper.CRefundAuditMapper;
import com.ruoyi.nature.service.ICOrderService;
import com.ruoyi.nature.service.ICRefundAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 退款审核Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@Service
public class CRefundAuditServiceImpl extends ServiceImpl<CRefundAuditMapper, CRefundAudit> implements ICRefundAuditService {

    @Autowired
    private ICOrderService icOrderService;

    @Autowired
    private ICRefundAuditService icRefundAuditService;

    @Autowired
    private WxPayService wxService;
    @Autowired
    private RedisCache redisCache;


    @Transactional
    @Override
    public int audit(String id, Integer status) throws WxPayException {
        CRefundAudit refundAudit = icRefundAuditService.getById(id);
        COrder order = icOrderService.getById(refundAudit.getOrderId());
        // 同意退款
        if (status == 1){
            WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
            wxPayRefundRequest.setTransactionId(order.getPaymentNo());
            wxPayRefundRequest.setOutRefundNo(refundAudit.getRefundNo());
            wxPayRefundRequest.setRefundFee(order.getPrice().multiply(new BigDecimal("100")).intValue());
            wxPayRefundRequest.setTotalFee(order.getPrice().multiply(new BigDecimal("100")).intValue());
            wxPayRefundRequest.setRefundDesc(refundAudit.getRemark());
//            wxPayRefundRequest.setNotifyUrl(wxMineProperties.getRefundNotifyUrl());
            WxPayRefundResult result = wxService.refund(wxPayRefundRequest);
            if("SUCCESS".equals(result.getResultCode())){
                // 退款成功
                order.setStatus(4);
                icOrderService.updateById(order);
                refundAudit.setStatus(1);
                refundAudit.setRefundTime(new Date());
                icRefundAuditService.updateById(refundAudit);
                Integer times = 0;
                Object timesObj = redisCache.getCacheObject("date:" + DateUtil.formatDate(order.getPlanDate()) + ":" + order.getPlaceId());
                if (timesObj != null) {
                    times = Integer.parseInt(timesObj.toString());
                }
                redisCache.setCacheObject("date:" + DateUtil.formatDate(order.getPlanDate()) + ":" + order.getPlaceId(), times == 0 ? 0 : times - 1, 30, TimeUnit.DAYS);
                return 1;
            }else{
                return 0;
            }
            //拒绝退款
        }else if (status == 2){
            order.setStatus(1);
            icOrderService.updateById(order);
            refundAudit.setStatus(2);
            refundAudit.setRefundTime(new Date());
            icRefundAuditService.updateById(refundAudit);
            return 1;
        }
        return 0;
    }
}
