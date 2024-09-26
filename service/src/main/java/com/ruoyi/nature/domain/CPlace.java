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
 * 场地对象 c_place
 *
 * @author ruoyi
 * @date 2021-09-22
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_place")
public class CPlace implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 场地名 */
    @Excel(name = "场地名")
    private String name;

    /** 分类id */
    @Excel(name = "分类id")
    private String categoryId;

    /** 场地详情 */
    @Excel(name = "场地详情")
    private String detail;

    /** 0准备中 1上架 2 下架 */
    @Excel(name = "0准备中 1上架 2 下架")
    private String status;

    /** 0允许取消 1不允许 */
    @Excel(name = "0允许取消 1不允许")
    private String allowReturn;

    /** 描述 */
    @Excel(name = "描述")
    private String describle;

    /** 标签 */
    @Excel(name = "标签")
    private String label;

    /** 用途 */
    @Excel(name = "用途")
    private String used;

    /** 添加人 */
    @Excel(name = "添加人")
    private Long operator;

    /** 图片 */
    @Excel(name = "图片")
    private String images;

    /** 视频 */
    @Excel(name = "视频")
    private String videos;

    /** 点击数 */
    @Excel(name = "点击数")
    private Long views;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Long collects;

    /** 购买数 */
    @Excel(name = "购买数")
    private Long bugs;

    /** 喜欢数 */
    @Excel(name = "购买数")
    private Long likes;

    /** 坐标 */
    @Excel(name = "坐标")
    private String coordinate;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 开放-开始时间 */
    @Excel(name = "开放-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date start;

    /** 开放-结束时间 */
    @Excel(name = "开放-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date end;

    /** 0全天 1白天 2 晚上 */
    @Excel(name = "0全天 1白天 2 晚上")
    private String dayType;

    /** 0自动延期 1手动获取 */
    @Excel(name = "0自动延期 1手动获取")
    private String scheType;

    /** 预约价 */
    @Excel(name = "预约价")
    private BigDecimal planPrice;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
