package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2022/1/9 14:35
 * @Description:
 */
@Data
public class LetterWriterDto {

    private String toId;

    private String context;

    private String uid;

    private String avatar;

    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    private Boolean read;
}
