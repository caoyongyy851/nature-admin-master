package com.ruoyi.nature.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/10/14 16:33
 * @Description:
 */
@Data
public class SelfDetail {
//    关注数
    private Integer follows;
//    粉丝数
    private Integer fans;
//    访客数
    private Integer callers;

//    帖子数
    private Integer cards;

//    评论数
    private Integer critics;
//    积分数
    private Integer points;
//比例
    private Integer ratio;
//    现金
    private BigDecimal cashs;

    //帖子数
    private Integer topics;
    //新私信
    private Integer newLetters;

}
