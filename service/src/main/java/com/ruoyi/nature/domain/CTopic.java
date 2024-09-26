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
 * 话题对象 c_topic
 *
 * @author ruoyi
 * @date 2021-11-03
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_topic")
public class CTopic implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 发布人id */
    @Excel(name = "发布人id")
    private String uid;

    /** 话题名称 */
    @Excel(name = "话题名称")
    private String title;

    /** 封面 */
    @Excel(name = "封面")
    private String cover;

    /** 图片组 */
    @Excel(name = "图片组")
    private String image;

    /** 描述 */
    @Excel(name = "描述")
    private String simpleDesc;

    /** 内容 */
    @Excel(name = "内容")
    private String detail;

    /** 浏览数 */
    @Excel(name = "浏览数")
    private Integer views;

    /** 审核与否 */
    @Excel(name = "审核与否")
    private Boolean checked;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Boolean deleted;

    /** 话题类型: 1: 活动 2: 任务 3: 知识 4: 公益 */
    @Excel(name = "话题类型: 1: 活动 2: 任务 3: 知识 4: 公益")
    private Integer type;

    @Excel(name = "来源")
    private Integer source;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
