package com.ruoyi.nature.service;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.ruoyi.nature.domain.CRefundAudit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 退款审核Service接口
 *
 * @author ruoyi
 * @date 2021-12-03
 */
public interface ICRefundAuditService extends IService<CRefundAudit> {

    int audit(String id, Integer status) throws WxPayException;
}
