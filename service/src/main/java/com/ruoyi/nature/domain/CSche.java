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
 * 档期对象 c_sche
 *
 * @author ruoyi
 * @date 2021-10-10
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_sche")
public class CSche implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** place_id */
    @Excel(name = "place_id")
    private String placeId;

    /** 手动输入-档期 */
    @Excel(name = "手动输入-档期")
    private String ytd;

    /** 预约价格 */
    @Excel(name = "预约价格")
    private BigDecimal price;

    /** 自动延续-天数 */
    @Excel(name = "自动延续-天数")
    private Integer days;

    /** 票数 */
    @Excel(name = "票数")
    private Integer surplus;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
