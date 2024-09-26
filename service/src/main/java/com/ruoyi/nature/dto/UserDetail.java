package com.ruoyi.nature.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.nature.domain.CUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/12/5 17:01
 * @Description:
 */
@Data
public class UserDetail extends CUser {
    private String companyName;

    private String cover;

    private String remark;

    private Integer person;

    private String aid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;

    private BigDecimal price;

    private Integer payType;

    private String orderId;

    private Integer status;

    private String statusStr;

    private String title;
}
