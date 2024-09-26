package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.CActOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.ActOrderDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.UserDetail;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2022-02-26
 */
public interface ICActOrderService extends IService<CActOrder> {

    CActOrder createOrder(JoinDto joinDto, CUser cUser);

    ActOrderDetail getOrderById(String orderId);

    CActOrder getByOrderNo(String orderNo);

    boolean refundActOrder(String id);

    List<UserDetail> getActUserList(String id);

    boolean confirmActOrder(String id);

    List<UserDetail> getMyActOrderList(String id);
}
