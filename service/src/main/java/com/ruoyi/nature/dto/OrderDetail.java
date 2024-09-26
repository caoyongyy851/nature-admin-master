package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.COrder;
import lombok.Data;

import java.util.List;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/11/27 19:01
 * @Description:
 */
@Data
public class OrderDetail extends COrder {
    private String placeName;

    private String statusStr;

    private List<String> useds;
}
