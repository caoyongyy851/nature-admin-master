package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

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
public class CardUserDto {
    private String id;

    private String uid;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time;

    private String position;

    private String imgs;

    private Integer imgsNum;

    private String vids;

    private Integer vidsNum;

    private String title;

    private String detail;

    private Integer comments;

    private Integer likes;

    private Boolean toLike;

    private String nickname;

    private String avatar;

    private String sign;

    private String topicId;

    private String topicTitle;

    private List<String> imgList;

    private List<String> vidList;

    private Boolean show = false;

    private Integer sn;
}
