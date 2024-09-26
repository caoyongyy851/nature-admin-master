package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/9/6 14:42
 * @Description:
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
public class TopicUserDto {
    private String id;

    private String uid;

    private String image;

    private String cover;

    private String title;

    private String simpleDesc;

    private String detail;

    private Integer type;

    private Integer views;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    private String nickname;

    private String avatar;

    private Boolean show = false;

    private String companyName;

    private Integer sign;
}
