package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.COrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class OrderUserDto extends COrder {
    private String placeName;

    private String statusStr;
}
