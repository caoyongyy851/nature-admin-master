package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.CActOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.ActOrderDetail;
import com.ruoyi.nature.dto.UserDetail;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-26
 */
public interface CActOrderMapper extends BaseMapper<CActOrder> {

    ActOrderDetail getOrderById(String orderId);

    CActOrder getByOrderNo(String orderNo);

    List<UserDetail> getActUserList(String id);

    List<UserDetail> getMyActOrderList(String id);
}
