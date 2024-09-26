package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2022/1/5 21:04
 * @Description:
 */
@Data
public class ContextDetail {

    private String id;

    private String uid;

    private String tid;

    private String content;

    private Integer type;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    private String nickname;

    private String avatar;

}
