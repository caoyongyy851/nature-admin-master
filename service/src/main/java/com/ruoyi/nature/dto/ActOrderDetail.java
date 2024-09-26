package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CActOrder;
import lombok.Data;


/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/11/27 19:01
 * @Description:
 */
@Data
public class ActOrderDetail extends CActOrder {
    // 活动名称
    private String actName;
    // 订单状态
    private String statusStr;

}
