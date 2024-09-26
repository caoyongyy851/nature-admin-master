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
 * 退款审核对象 c_refund_audit
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_refund_audit")
public class CRefundAudit implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 订单id */
    @Excel(name = "订单id")
    private String orderId;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 退款单号 */
    @Excel(name = "退款单号")
    private String refundNo;

    /** 退款原因 */
    @Excel(name = "退款原因")
    private String remark;

    /** 退款状态0：待审核 1：退款成功 2：拒绝退款 */
    @Excel(name = "退款状态0：待审核 1：退款成功 2：拒绝退款")
    private Integer status;

    /** 退款类型0：预约退款 */
    @Excel(name = "退款类型0：预约退款")
    private Integer type;

    /** 退款时间 */
    @Excel(name = "退款时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refundTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /** 是否删除 */
    @Excel(name = "是否删除")
    private Boolean deleted;
}
