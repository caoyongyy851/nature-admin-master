package com.ruoyi.nature.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单对象 c_act_order
 *
 * @author ruoyi
 * @date 2022-02-26
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_act_order")
public class CActOrder implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private String orderNo;

    /** 活动id */
    @Excel(name = "活动id")
    private String actId;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal price;

    /** 订单备注 */
    @Excel(name = "订单备注")
    private String remark;

    /** 0待付款 1已付款（待核销） 2已完成（已核销、待评价） 3已评价 4已取消 */
    @Excel(name = "0待付款 1已付款" , readConverterExp = "待=核销")
    private Integer status;

    /** 支付时间 */
    @Excel(name = "支付时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 0小程序 */
    @Excel(name = "0小程序")
    private Integer payment;

    /** 报名人数 */
    @Excel(name = "报名人数")
    private Integer person;

    /** 下单人 */
    @Excel(name = "下单人")
    private String userId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 核销码 */
    @Excel(name = "核销码")
    private String code;

    /** 付款单号 */
    @Excel(name = "付款单号")
    private String paymentNo;

    /** 0未开发票 1 已开发票 */
    @Excel(name = "0未开发票 1 已开发票")
    private Integer isInvoice;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
