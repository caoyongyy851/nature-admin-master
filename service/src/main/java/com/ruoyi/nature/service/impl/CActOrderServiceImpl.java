package com.ruoyi.nature.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.OrderNoUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.nature.config.WxMaConfiguration;
import com.ruoyi.nature.config.WxMineProperties;
import com.ruoyi.nature.domain.CActivity;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.ActOrderDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.UserDetail;
import com.ruoyi.nature.service.ICActivityService;
import com.ruoyi.nature.service.ICUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.nature.mapper.CActOrderMapper;
import com.ruoyi.nature.domain.CActOrder;
import com.ruoyi.nature.service.ICActOrderService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订单Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-26
 */
@Slf4j
@Service
public class CActOrderServiceImpl extends ServiceImpl<CActOrderMapper, CActOrder> implements ICActOrderService {

    @Autowired
    private ICActivityService icActivityService;

    @Autowired
    private ICUserService icUserService;

    @Autowired
    private WxMineProperties wxMineProperties;

    @Autowired
    private WxPayService wxService;


    /**
     * 创建活动订单
     * @param joinDto
     * @param cUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CActOrder createOrder(JoinDto joinDto, CUser cUser) {
        CActivity cActivity = icActivityService.getById(joinDto.getAid());
        if (StringUtils.isNull(cActivity))
        {
            throw new CustomException("活动不存在");
        }
        if (new Date().getTime() >= (cActivity.getTime().getTime() + 86400000 - 1))
        {
            throw new CustomException("当前活动已截止");
        }
        //活动创建者
        CUser creater = icUserService.getById(cActivity.getPbid());
        // 查询之前是否参加过该活动
        if (cActivity.getPayType() == 1){
            QueryWrapper<CActOrder> wr = new QueryWrapper<CActOrder>();
            wr.eq("act_id", cActivity.getId()).eq("user_id", cUser.getId()).eq("status", 0);
            if(count(wr) > 0){
                throw new CustomException("您的订单已生成，请前往订单列表进行支付");
            }
        }
        QueryWrapper<CActOrder> wrapper = new QueryWrapper<CActOrder>();
        wrapper.eq("act_id", cActivity.getId()).eq("user_id", cUser.getId()).in("status", 1,2,5);
        if (count(wrapper) > 0){
            throw new CustomException("已参加过活动");
        }
        // 预参与人数
        Integer joinPerson = cActivity.getPerLimit() + joinDto.getPerson();
        if (joinPerson > cActivity.getPerson()){
            throw new CustomException("当前活动剩余名额不足");
        }
        CActOrder actOrder = new CActOrder();
        // 如果当前活动不是线上支付
        if (cActivity.getPayType() != 1){
            cActivity.setPerLimit(joinPerson);
            icActivityService.updateById(cActivity);
            //生成活动订单（免费的或是个人）
            actOrder.setActId(cActivity.getId());
            actOrder.setPrice(BigDecimal.ZERO);
            actOrder.setRemark(joinDto.getRemark());
            actOrder.setPerson(joinDto.getPerson());

            actOrder.setOrderNo(OrderNoUtil.getC(null));
            actOrder.setUserId(cUser.getId());
            actOrder.setPhone(cUser.getPhone());
            //免费的或是个人
            actOrder.setStatus(5);
            //生成核销码
            actOrder.setCode(StrUtil.sub(IdUtil.simpleUUID(), 0, 4));
            actOrder.setCreateTime(new Date());
            boolean save = save(actOrder);
            if (save){
                if (joinPerson >= cActivity.getPerson()) {
                    log.info("发信息给发布者");
                    List<WxMaSubscribeMessage.Data> dataList = new ArrayList<>();
                    dataList.add(new WxMaSubscribeMessage.Data() {{
                        setName("thing1");
                        setValue(cActivity.getTitle());
                    }});
                    dataList.add(new WxMaSubscribeMessage.Data() {{
                        setName("number3");
                        setValue(cActivity.getPerson().toString());
                    }});
                    dataList.add(new WxMaSubscribeMessage.Data() {{
                        setName("thing4");
                        setValue("当前活动已成团");
                    }});
                    try {
                        sendMsg(dataList, wxMineProperties.getTempId1(), creater.getOpenid());
                    } catch (WxErrorException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.info("未发信息");
                }
            }
            return actOrder;
        } else {
            // 如果为线上支付
            //生成活动订单（免费的或是个人）
            actOrder.setActId(cActivity.getId());
            actOrder.setPrice(cActivity.getPrice().multiply(new BigDecimal(joinDto.getPerson())));
            actOrder.setRemark(joinDto.getRemark());
            actOrder.setPerson(joinDto.getPerson());
            actOrder.setOrderNo(OrderNoUtil.getC(null));
            actOrder.setUserId(cUser.getId());
            actOrder.setPhone(cUser.getPhone());
            //待支付
            actOrder.setStatus(0);
            actOrder.setCreateTime(new Date());
            save(actOrder);
            return actOrder;
        }
    }

    @Override
    public ActOrderDetail getOrderById(String orderId) {
        return baseMapper.getOrderById(orderId);
    }

    @Override
    public CActOrder getByOrderNo(String orderNo) {
        return baseMapper.getByOrderNo(orderNo);
    }
    @Transactional
    @Override
    public boolean refundActOrder(String id) {
        CActOrder order = getById(id);
        CActivity activity = icActivityService.getById(order.getActId());
        if (order.getStatus() != 1 && order.getStatus() != 5){
            throw new CustomException("订单状态有误");
        }
        if (order.getStatus() == 5 &&
                (activity.getPayType() == 0 || activity.getPayType() == 2)){
            // 免费的/个人的
            order.setStatus(4);
            boolean update = updateById(order);
            if (update){
                activity.setPerLimit(activity.getPerLimit()-order.getPerson());
                boolean updateAct = icActivityService.updateById(activity);
                if (updateAct){
                    return true;
                }
            }
        }
        if (order.getStatus() == 1 &&
                activity.getPayType() == 1){
            // 查看活动是否已逾期
            if (new Date().getTime() >= (activity.getTime().getTime() + 86400000 - 1))
            {
                throw new CustomException("不允许退款，详情请联系活动发布者~");
            }
            // 平台付费的
            WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
            wxPayRefundRequest.setTransactionId(order.getPaymentNo());
            wxPayRefundRequest.setOutRefundNo(order.getOrderNo());
            wxPayRefundRequest.setRefundFee(order.getPrice().multiply(new BigDecimal("100")).intValue());
            wxPayRefundRequest.setTotalFee(order.getPrice().multiply(new BigDecimal("100")).intValue());
            wxPayRefundRequest.setRefundDesc("活动订单退款");
            // wxPayRefundRequest.setNotifyUrl(wxMineProperties.getRefundNotifyUrl());
            WxPayRefundResult result = null;
            try {
                result = wxService.refund(wxPayRefundRequest);
            } catch (WxPayException e) {
                e.printStackTrace();
            }
            if("SUCCESS".equals(result.getResultCode())){
                // 退款成功
                order.setStatus(4);
                boolean update = updateById(order);
                if (update){
                    //扣减活动发布者的现金
                    activity.setPerLimit(activity.getPerLimit()-order.getPerson());
                    boolean updateAct = icActivityService.updateById(activity);
                    if (updateAct) {
                        log.info("【活动】:{}", JSONUtil.toJsonStr(activity));
                        CUser creater = icUserService.getById(activity.getPbid());
                        log.info("【活动发布者】: {}", JSONUtil.toJsonStr(creater));
                        BigDecimal cash = creater.getCashs() == null ? BigDecimal.ZERO : creater.getCashs();
                        creater.setCashs(cash.subtract(new BigDecimal(wxPayRefundRequest.getTotalFee()).divide(new BigDecimal("100"))));
                        boolean updateUser = icUserService.updateById(creater);
                        if (updateUser) {
                            return true;
                        }
                    }
                }
                return false;
            }else{
                return false;
            }
        }
        if (order.getStatus() != 1 &&
                activity.getPayType() == 1){
            throw new CustomException("不能取消报名，详情请联系活动发布者~");
        }
       return false;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean confirmActOrder(String id) {
        CActOrder order = getById(id);
        CActivity activity = icActivityService.getById(order.getActId());
        if (order.getStatus() != 1){
            throw new CustomException("订单状态有误");
        }
        if (activity.getPayType() != 1){
            throw new CustomException("活动支付类型有误");
        }
        order.setStatus(2);
        updateById(order);
        return true;
    }

    @Override
    public List<UserDetail> getActUserList(String id) {
        return baseMapper.getActUserList(id);
    }

    @Override
    public List<UserDetail> getMyActOrderList(String id) {
        return baseMapper.getMyActOrderList(id);
    }

    /**
     * 发消息
     *
     * @param dataList
     * @param tempId
     * @param openId
     * @throws WxErrorException
     */
    private void sendMsg(List<WxMaSubscribeMessage.Data> dataList, String tempId, String openId) throws WxErrorException {
        final WxMaService wxService = WxMaConfiguration.getMaService(wxMineProperties.getAppId());
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
        wxMaSubscribeMessage.setData(dataList);
        wxMaSubscribeMessage.setTemplateId(tempId);
        wxMaSubscribeMessage.setToUser(openId);
        wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
    }
}
