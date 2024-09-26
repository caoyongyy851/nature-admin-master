package com.ruoyi.nature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * wxpay pay properties.
 *
 * @author Binary Wang
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.mine")
public class WxMineProperties {
  /**
   * notify_url
   */
  private String placeNotifyUrl;


  private String actNotifyUrl;

  /**
   * refund_notify_url
   */
  private String refundNotifyUrl;

  /**
   * spbill_create_ip
   */
  private String spbillCreateIp;

  /**
   * trade_type
   */
  private String tradeType;

  /**
   * appId
   */
  private String appId;
  /**
   * baseUrl
   */
  private String baseUrl;

  /**
   * 消息模板1
   */
  private String tempId1;


  /**
   * 消息模板2
   */
  private String tempId2;
}
