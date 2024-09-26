package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/8/10 15:26
 * @Description:
 */
@Data
public class CardDetail {
    private String nickname;

    private String avatar;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /** id */
    private String id;

    /** 发布人id */
    private String uid;

    /** 标题 */
    private String title;

    /** 拍摄时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time;

    /** 地点 */
    private String position;

    /** 玩场id */
    private String pid;

    /** 图片组或视频 */
    private String imgs;

    /** 图片数或视频数 */
    private Integer imgsNum;

    /** 图片组或视频 */
    private String vids;

    /** 图片数或视频数 */
    private Integer vidsNum;

    /** 描述 */
    private String detail;

    /** 浏览数 */
    private Integer views;

    /** 喜欢数 */
    private Integer likes;

    /** 话题id */
    private String topicId;

    private String topicTitle;

    /** 评论数 */
    private Integer comments;

    private List<String> avatarLikes;
}
