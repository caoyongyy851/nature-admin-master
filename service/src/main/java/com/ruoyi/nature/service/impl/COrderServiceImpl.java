package com.ruoyi.nature.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.nature.domain.COrder;
import com.ruoyi.nature.domain.CPlace;
import com.ruoyi.nature.domain.CSche;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.OrderDetail;
import com.ruoyi.nature.dto.OrderUserDto;
import com.ruoyi.nature.mapper.COrderMapper;
import com.ruoyi.nature.service.ICOrderService;
import com.ruoyi.nature.service.ICPlaceService;
import com.ruoyi.nature.service.ICScheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Slf4j
@Service
public class COrderServiceImpl extends ServiceImpl<COrderMapper, COrder> implements ICOrderService {

    @Autowired
    private ICPlaceService icPlaceService;
    @Autowired
    private ICScheService icScheService;
    @Autowired
    private RedisCache redisCache;
    @Override
    @Transactional
    public COrder createOrder(OrderDetail orderDetail, CUser user) {

        COrder order = new COrder();
        CPlace cPlace = icPlaceService.getById(orderDetail.getPlaceId());
        if (cPlace == null){
            throw new CustomException("玩场不存在");
        }
        if (!"1".equals(cPlace.getStatus())){
            throw new CustomException("玩场已停用");
        }
        if (StringUtils.isNull(orderDetail.getPlanDate())){
            throw new CustomException("预约日期为空");
        }
        if (StringUtils.isNull(orderDetail.getPerson())){
            throw new CustomException("人数为空");
        }
        if (orderDetail.getPerson() <= 0){
            throw new CustomException("人数不正确");
        }
        if (StringUtils.isEmpty(orderDetail.getUsed())){
            throw new CustomException("计划用途为空");
        }
        if (StringUtils.isNull(orderDetail.getPrice())){
            throw new CustomException("预约价为空");
        }
        if (orderDetail.getPrice().compareTo(BigDecimal.ZERO) == -1){
            throw new CustomException("预约金额有误");
        }
        Integer times = 0;
        Object timesObj = redisCache.getCacheObject("date:" + DateUtil.formatDate(orderDetail.getPlanDate()) + ":" + cPlace.getId());
        if (timesObj != null) {
            times = Integer.parseInt(timesObj.toString());
        }
        CSche cSche = null;
        //自动延期
        if ("0".equals(cPlace.getScheType())){
            QueryWrapper<CSche> wrapper = new QueryWrapper();
            wrapper.eq("place_id", cPlace.getId());
            cSche = icScheService.getOne(wrapper);
            if (times != null){
                if(cSche.getSurplus() < times + 1){
                    throw new CustomException("当前日期已预满");
                }
            }
            cSche.getSurplus();
        }else{
            // 手动延期
            QueryWrapper<CSche> wrapper = new QueryWrapper();
            wrapper.eq("place_id", cPlace.getId());
            wrapper.eq("ytd", DateUtil.formatDate(orderDetail.getPlanDate()));
            cSche = icScheService.getOne(wrapper);
            if (times != null){
                if(cSche.getSurplus() < times + 1){
                    throw new CustomException("当前日期已预满");
                }
            }
        }
        BeanUtils.copyProperties(orderDetail, order);
        order.setOrderNo(OrderNoUtil.getC(null));
        order.setUserId(user.getId());
        order.setPhone(user.getPhone());
        order.setStatus(0);
        boolean save = save(order);
        return order;
    }

    @Override
    public OrderDetail getOrderById(String orderId) {
        return baseMapper.getOrderById(orderId);
    }

    @Override
    public COrder getByOrderNo(String orderNo) {
        return baseMapper.getByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public int payNotify(COrder order) {
        COrder cOrder = getById(order.getId());
        cOrder.setPayTime(order.getPayTime());
        cOrder.setPaymentNo(order.getPaymentNo());
        //支付成功
        cOrder.setStatus(order.getStatus());
        log.info(JSON.toJSONString(cOrder));
        boolean flag = updateById(cOrder);
        return flag ? 1 : 0;
    }

    @Override
    public List<OrderUserDto> getOrderByUid(String id, Integer pageNum, Integer pageSize) {
        return baseMapper.getOrderByUid(id, (pageNum - 1) * pageSize, pageSize);
    }

    @Transactional
    @Override
    public void auditCode(String id) {
         COrder order = getById(id);
         if(order.getStatus() != 1){
             throw new CustomException("订单状态错误");
         }
         order.setStatus(2);
         updateById(order);
    }
}
