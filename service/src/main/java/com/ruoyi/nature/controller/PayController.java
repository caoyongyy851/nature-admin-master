package com.ruoyi.nature.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.WxLoginService;
import com.ruoyi.framework.web.service.WxUser;
import com.ruoyi.nature.config.WxMaConfiguration;
import com.ruoyi.nature.config.WxMineProperties;
import com.ruoyi.nature.domain.CActOrder;
import com.ruoyi.nature.domain.CActivity;
import com.ruoyi.nature.domain.COrder;
import com.ruoyi.nature.domain.CUser;
import com.ruoyi.nature.dto.ActOrderDetail;
import com.ruoyi.nature.dto.JoinDto;
import com.ruoyi.nature.dto.OrderDetail;
import com.ruoyi.nature.service.ICActOrderService;
import com.ruoyi.nature.service.ICActivityService;
import com.ruoyi.nature.service.ICOrderService;
import com.ruoyi.nature.service.ICUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/8/14 15:18
 * @Description:
 */
@Slf4j
@RequestMapping("nature/pay")
@RestController
@Api(tags = "前端控制器")
public class PayController {
    @Autowired
    private WxMineProperties wxMineProperties;

    @Autowired
    private ICUserService icUserService;

    @Autowired
    private WxLoginService wxLoginService;


    @Autowired
    private ICOrderService icOrderService;

    @Autowired
    private WxPayService wxService;

    @Autowired
    private ICActOrderService icActOrderService;
    @Autowired
    private ICActivityService icActivityService;

    @Autowired
    private RedisCache redisCache;




    @ApiOperation(value = "创建订单")
    @PostMapping("/createCOrder")
    public R createCOrder(@RequestBody OrderDetail orderDetail, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            COrder order = icOrderService.createOrder(orderDetail, cUser);
            if (order.getId() != null) {
                return R.success("订单创建成功", order.getId());
            }
            return R.error("订单创建失败");
        }
        return R.error("订单创建失败");
    }

    @ApiOperation(value = "创建活动订单")
    @PostMapping("/createActOrder")
    public R createActOrder(@RequestBody JoinDto joinDto, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            CActOrder order = icActOrderService.createOrder(joinDto, cUser);
            if (order.getId() != null) {
                return R.success("订单创建成功", order.getId());
            }
            return R.error("订单创建失败");
        }
        return R.error("订单创建失败");
    }

    /**
     * <pre>
     * 获取订单
     * </pre>
     */
    @GetMapping("/getOrderById")
    public R getOrderById(String orderId, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            OrderDetail orderDetail = icOrderService.getOrderById(orderId);
            if (orderDetail != null) {
                if (StringUtils.isNotEmpty(orderDetail.getUsed())) {
                    orderDetail.setUseds(Arrays.asList(orderDetail.getUsed().split(",")));
                }
                return R.success("订单查询成功", orderDetail);
            }
        }
        return R.error("订单查询失败");
    }

    /**
     * <pre>
     * 获取订单
     * </pre>
     */
    @GetMapping("/getActOrderById")
    public R getActOrderById(String orderId, HttpServletRequest request) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            ActOrderDetail orderDetail = icActOrderService.getOrderById(orderId);
            if (orderDetail != null) {
                return R.success("订单查询成功", orderDetail);
            }
        }
        return R.error("订单查询失败");
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone/{appid}")
    public R phone(@PathVariable String appid, String encryptedData, String iv, HttpServletRequest request, String code) {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            if (StringUtils.isBlank(code)) {
                return R.error("empty jscode");
            }
            final WxMaService wxService = WxMaConfiguration.getMaService(appid);
            WxMaJscode2SessionResult session = null;
            try {
                session = wxService.getUserService().getSessionInfo(code);
            } catch (WxErrorException e) {
                log.error(e.getMessage());
                return R.error(e.toString());
            }
            WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);

            cUser.setPhone(phoneNoInfo.getPhoneNumber());
            icUserService.updateById(cUser);

            return R.success(phoneNoInfo);
        }
        return R.error("获取手机号出错");
    }


    /**
     * 调用统一下单接口，并组装生成支付所需参数对象.
     *
     * @param
     * @param {@link com.github.binarywang.wxpay.bean.order}包下的类
     * @return 返回 {@link com.github.binarywang.wxpay.bean.order}包下的类对象
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrder")
    public R createOrder(@RequestBody COrder order, HttpServletRequest request) throws WxPayException {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            OrderDetail corder = icOrderService.getOrderById(order.getId());
            if (corder == null) {
                return R.error("订单不存在");
            }
            if (corder.getStatus() != 0) {
                return R.error("订单状态错误");
            }
            if (corder.getPrice().compareTo(BigDecimal.ZERO) == -1) {
                return R.error("订单金额出错");
            }
            WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
            req.setBody("自然玩主订单玩场支付");
            req.setNotifyUrl(wxMineProperties.getPlaceNotifyUrl());
            req.setTradeType(wxMineProperties.getTradeType());
            req.setOutTradeNo(corder.getOrderNo());
            req.setSpbillCreateIp(wxMineProperties.getSpbillCreateIp());
            req.setVersion("1");
            int totalFee = corder.getPrice().multiply(new BigDecimal("100")).intValue();
            req.setTotalFee(totalFee);
            req.setDetail("玩场【" + corder.getPlaceName() + "】预定");
            req.setOpenid(cUser.getOpenid());
            Object res = this.wxService.createOrder(req);
            return R.success(res);
        }
        return R.error("创建微信预支付订单失败");
    }

    @ApiOperation(value = "活动订单预支付")
    @PostMapping("/payActOrder")
    public R payActOrder(@RequestBody CActOrder order, HttpServletRequest request) throws WxPayException {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            ActOrderDetail corder = icActOrderService.getOrderById(order.getId());
            if (corder == null) {
                return R.error("订单不存在");
            }
            if (corder.getStatus() != 0) {
                return R.error("订单状态错误");
            }
            if (corder.getPrice().compareTo(BigDecimal.ZERO) == -1) {
                return R.error("订单金额出错");
            }
            CActivity activity = icActivityService.getById(corder.getActId());
            if (activity.getPerLimit() + corder.getPerson() > activity.getPerson()){
                return R.error("手慢啦，活动名额已满");
            }
            WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
            req.setBody("自然玩主订单活动支付");
            req.setNotifyUrl(wxMineProperties.getActNotifyUrl());
            req.setTradeType(wxMineProperties.getTradeType());
            req.setOutTradeNo(corder.getOrderNo());
            req.setSpbillCreateIp(wxMineProperties.getSpbillCreateIp());
            req.setVersion("1");
            int totalFee = corder.getPrice().multiply(new BigDecimal("100")).intValue();
            req.setTotalFee(totalFee);
            req.setDetail("活动【" + corder.getActName() + "】报名");
            req.setOpenid(cUser.getOpenid());
            Object res = this.wxService.createOrder(req);
            return R.success(res);
        }
        return R.error("创建微信预支付订单失败");
    }


    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/placeNotifyOrder")
    public String placeOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        log.info(JSON.toJSONString(notifyResult));
        String orderNo = notifyResult.getOutTradeNo();
        log.info("【玩场订单orderNo】: " + orderNo);
        if (StringUtils.isNotEmpty(orderNo)) {
            COrder order = icOrderService.getByOrderNo(orderNo);
            log.info("order: " + JSON.toJSONString(order));
            COrder cOrder = icOrderService.getById(order.getId());
            cOrder.setPayTime(new Date());
            cOrder.setPaymentNo(notifyResult.getTransactionId());
            //支付成功
            cOrder.setStatus(1);
            //生成核销码
            cOrder.setCode(StrUtil.sub(IdUtil.simpleUUID(), 0, 4));
            log.info("corder：" + JSON.toJSONString(cOrder));
            boolean flag = icOrderService.updateById(cOrder);
            if (flag) {
                Integer times = 0;
                Object timesObj = redisCache.getCacheObject("date:" + DateUtil.formatDate(cOrder.getPlanDate()) + ":" + cOrder.getPlaceId());
                if (timesObj != null) {
                    times = Integer.parseInt(timesObj.toString());
                }
                redisCache.setCacheObject("date:" + DateUtil.formatDate(cOrder.getPlanDate()) + ":" + cOrder.getPlaceId(), times + 1, 30, TimeUnit.DAYS);
                return WxPayNotifyResponse.success("成功");
            }
        }
        return WxPayNotifyResponse.failResp("失败");
    }

    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/actNotifyOrder")
    public String actOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxService.parseOrderNotifyResult(xmlData);
        log.info(JSON.toJSONString(notifyResult));
        String orderNo = notifyResult.getOutTradeNo();
        log.info("【活动订单orderNo】: " + orderNo);
        if (StringUtils.isNotEmpty(orderNo)) {
            CActOrder order = icActOrderService.getByOrderNo(orderNo);
            log.info("order: " + JSON.toJSONString(order));
            CActOrder cActOrder = icActOrderService.getById(order.getId());
            cActOrder.setPayTime(new Date());
            cActOrder.setPaymentNo(notifyResult.getTransactionId());
            //支付成功
            cActOrder.setStatus(1);
            //生成核销码
            cActOrder.setCode(StrUtil.sub(IdUtil.simpleUUID(), 0, 4));
            log.info("cActOrder：" + JSON.toJSONString(cActOrder));
            boolean flag = icActOrderService.updateById(cActOrder);
            if (flag) {
                CActivity activity = icActivityService.getById(cActOrder.getActId());
                activity.setPerLimit(activity.getPerLimit() + cActOrder.getPerson());
                boolean update = icActivityService.updateById(activity);
                if (update){
                    //给发布者增加报名费用
                    BigDecimal fee = new BigDecimal(notifyResult.getTotalFee().toString()).divide(new BigDecimal("100"));
                    CUser creater = icUserService.getById(activity.getPbid());
                    BigDecimal cash = creater.getCashs() == null ? BigDecimal.ZERO : creater.getCashs();
                    creater.setCashs(cash.add(fee));
                    icUserService.updateById(creater);
                    return WxPayNotifyResponse.success("成功");
                }
                return WxPayNotifyResponse.success("成功");
            }
        }
        return WxPayNotifyResponse.failResp("失败");
    }



    @ApiOperation(value = "关闭订单")
    @GetMapping("/closeOrder/{outTradeNo}")
    public R closeOrder(@PathVariable String outTradeNo, HttpServletRequest request) throws WxPayException {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null){
            WxPayOrderCloseResult wxPayOrderCloseResult = this.wxService.closeOrder(outTradeNo);
            if (wxPayOrderCloseResult.getResultCode().equals("SUCCESS")){
                COrder order = icOrderService.getByOrderNo(outTradeNo);
                if(order != null){
                    order.setStatus(4);
                    order.setUpdateTime(new Date());
                    boolean update = icOrderService.updateById(order);
                    if (update){
                        return R.success("取消订单成功", wxPayOrderCloseResult);
                    }
                }
            }
            return R.error("取消订单失败", wxPayOrderCloseResult);
        }
        return R.error("取消订单失败, 用户信息为空");
    }

    @ApiOperation(value = "关闭订单")
    @GetMapping("/closeActOrder/{outTradeNo}")
    public R closeActOrder(@PathVariable String outTradeNo, HttpServletRequest request) throws WxPayException {
        CUser cUser = getCUserByRequest(request);
        if (cUser != null){
            WxPayOrderCloseResult wxPayOrderCloseResult = this.wxService.closeOrder(outTradeNo);
            if (wxPayOrderCloseResult.getResultCode().equals("SUCCESS")){
                CActOrder order = icActOrderService.getByOrderNo(outTradeNo);
                if(order != null){
                    order.setStatus(4);
                    order.setUpdateTime(new Date());
                    boolean update = icActOrderService.updateById(order);
                    if (update){
                        return R.success("取消订单成功", wxPayOrderCloseResult);
                    }
                }
            }
            return R.error("取消订单失败", wxPayOrderCloseResult);
        }
        return R.error("取消订单失败, 用户信息为空");
    }

    @ApiOperation(value = "活动订单退款")
    @GetMapping("/refundActOrder/{id}" )
    public R refundActOrder(@PathVariable String id, HttpServletRequest request){

        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            boolean flag = icActOrderService.refundActOrder(id);
            if (flag) {
                return R.success("活动订单退款成功");
            }
        }
        return R.error("活动订单退款失败");
    }

    @ApiOperation(value = "确认活动订单")
    @GetMapping("/confirmActOrder/{id}" )
    public R confirmActOrder(@PathVariable String id, HttpServletRequest request){

        CUser cUser = getCUserByRequest(request);
        if (cUser != null) {
            boolean flag = icActOrderService.confirmActOrder(id);
            if (flag) {
                return R.success("确认订单成功~");
            }
        }
        return R.error("确认订单失败~");
    }



    private CUser getCUserByRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        System.out.println(authorization);
        String token = wxLoginService.getToken(request);
        WxUser wxUser = wxLoginService.getWxUser(token);
        String openid = wxUser.getOpenid();
        QueryWrapper<CUser> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return icUserService.getOne(wrapper);
    }
}
