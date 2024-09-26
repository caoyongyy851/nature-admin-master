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

/**
 * 帖子对象 c_card
 *
 * @author ruoyi
 * @date 2021-09-06
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_card")
public class CCard implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 发布人id */
    @Excel(name = "发布人id")
    private String uid;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 拍摄时间 */
    @Excel(name = "拍摄时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time;

    /** 地点 */
    @Excel(name = "地点")
    private String position;

    /** 玩场id */
    @Excel(name = "玩场id")
    private String pid;

    /** 图片组 */
    @Excel(name = "图片组")
    private String imgs;

    /** 图片数 */
    @Excel(name = "图片数")
    private Integer imgsNum;

    /** 视频 */
    @Excel(name = "视频")
    private String vids;

    /** 视频数 */
    @Excel(name = "视频数")
    private Integer vidsNum;

    /** 描述 */
    @Excel(name = "描述")
    private String detail;

    /** 浏览数 */
    @Excel(name = "浏览数")
    private Integer views;

    /** 喜欢数 */
    @Excel(name = "喜欢数")
    private Integer likes;

    /** 评论数 */
    @Excel(name = "评论数")
    private Integer comments;

    /** 审核与否 */
    @Excel(name = "审核与否")
    private Boolean checked;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Boolean deleted;

    /** 主题ID */
    @Excel(name = "主题ID")
    private String topicId;

    /** 来源 */
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
