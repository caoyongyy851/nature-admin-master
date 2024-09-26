package com.ruoyi.nature.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/12/5 12:16
 * @Description:
 */
@Data
public class CashsForm {

    private String id;

    private Integer type;

    private BigDecimal cashs;
}
