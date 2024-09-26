package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CUser;
import lombok.Data;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/10/22 11:28
 * @Description:
 */
@Data
public class UserSpaceInfo extends CUser {
    private Integer cards;
    private Integer callers;
    private Integer fans;
    private Integer follows;
}
