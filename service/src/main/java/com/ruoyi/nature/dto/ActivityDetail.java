package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CActivity;
import lombok.Data;

import java.util.List;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/8/10 15:26
 * @Description:
 */
@Data
public class ActivityDetail extends CActivity {
    private String nickname;

    private Boolean sex;

    private String avatar;

    private String sign;

    private String city;

    private Integer cheats;

    private String companyName;

    private Integer activityStatus;

    private List<String> imgList;
}
