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
public class TopicDetail {
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

    /** 图片组 */
    private String image;

    /** 封面 */
    private String cover;

    /** 描述 */
    private String simpleDesc;

    /** 内容 */
    private String detail;

    /** 浏览数 */
    private Integer views;

    /** 类型 */
    private Integer type;

    /** 参与人数 */
    private Integer joinNum;

    private String companyName;

    private List<String> imageList;

}
