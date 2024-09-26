package com.ruoyi.nature.service;

import com.ruoyi.nature.domain.COrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.OrderDetail;
import com.ruoyi.nature.dto.OrderUserDto;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2021-10-12
 */
public interface ICOrderService extends IService<COrder> {
    COrder createOrder(OrderDetail orderDetail, CUser user);

    OrderDetail getOrderById(String orderId);

    COrder getByOrderNo(String orderNo);

    int payNotify(COrder order);

    List<OrderUserDto> getOrderByUid(String id, Integer pageNum, Integer pageSize);

    void auditCode(String id);
}
