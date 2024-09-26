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
 * 订单对象 c_order
 *
 * @author ruoyi
 * @date 2021-10-12
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_order")
public class COrder implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private String orderNo;

    /** 玩场id */
    @Excel(name = "玩场id")
    private String placeId;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal price;

    /** 订单备注 */
    @Excel(name = "订单备注")
    private String remark;

    /** 0待付款 1已付款（待核销） 2已完成（已核销、待评价） 3已评价 4已取消 */
    @Excel(name = "0待付款 1已付款" , readConverterExp = "待=核销")
    private Integer status;

    /** 修改时间 */
    @Excel(name = "修改时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 0小程序 */
    @Excel(name = "0小程序")
    private Integer payment;

    /** 下单人id */
    @Excel(name = "下单人id")
    private String userId;

    /** 人数 */
    @Excel(name = "人数")
    private Integer person;

    /** 预约日期 */
    @Excel(name = "预约日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDate;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;


    /** 核销码 */
    @Excel(name = "核销码")
    private String code;

    /** 玩场用途 */
    @Excel(name = "玩场用途")
    private String used;

    /** 付款单号 */
    @Excel(name = "付款单号")
    private String paymentNo;

    /** 0未开发票 1 已开发票 */
    @Excel(name = "0未开发票 1 已开发票")
    private Integer isInvoice;

    /** 用户优惠券 */
    @Excel(name = "用户优惠券")
    private Long userCouponId;

    /** 现金券 */
    @Excel(name = "现金券")
    private BigDecimal userCash;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
