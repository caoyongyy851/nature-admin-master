package com.ruoyi.nature.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.nature.domain.CComment;
import lombok.Data;

import java.util.Date;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program:
 * @Date: 2021/8/17 10:10
 * @Description: 评论返回封装
 */
@Data
public class CommentDetail {
    private String nickname;

    private String avatar;

    /** id */
    private String id;

    /** 创建时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /** 逻辑删除 */
    private Boolean deleted;

    /** 评论人id */
    private String uid;

    /** 帖子id */
    private String cid;

    /** 活动id */
    private String aid;

    /** 点赞数 */
    private Integer likes;

    /** 评论 */
    private String content;

    /** 是否已读 */
    private Boolean isread;

    /** 是否审核 */
    private Boolean checked;

    /** 是否点赞 */
    private Boolean isZan;
}
