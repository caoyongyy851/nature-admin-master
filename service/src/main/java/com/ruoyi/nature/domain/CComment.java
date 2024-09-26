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
 * 评论对象 c_comment
 *
 * @author ruoyi
 * @date 2021-08-14
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_comment")
public class CComment implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Boolean deleted;

    /** 评论人id */
    @Excel(name = "评论人id")
    private String uid;

    /** 帖子id */
    @Excel(name = "帖子id")
    private String cid;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Integer likes;

    /** 评论 */
    @Excel(name = "评论")
    private String content;

    /** 是否已读 */
    @Excel(name = "是否已读")
    private Boolean isread;

    /** 是否审核 */
    @Excel(name = "是否审核")
    private Boolean checked;
}
