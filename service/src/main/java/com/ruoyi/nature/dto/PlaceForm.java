package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CPlace;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/10/20 9:07
 * @Description:
 */
@Data
public class PlaceForm extends CPlace {

    private Integer days;

    private Integer surplus;

    private BigDecimal price;
}
