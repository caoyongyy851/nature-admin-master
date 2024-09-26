package com.ruoyi.nature.mapper;

import com.ruoyi.nature.domain.COrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.nature.dto.OrderDetail;
import com.ruoyi.nature.dto.OrderUserDto;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author ruoyi
 * @date 2021-10-12
 */
public interface COrderMapper extends BaseMapper<COrder> {

    OrderDetail getOrderById(String orderId);

    COrder getByOrderNo(String orderNo);

    List<OrderUserDto> getOrderByUid(String id, int i, Integer pageSize);
}
