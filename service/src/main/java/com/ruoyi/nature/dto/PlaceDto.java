package com.ruoyi.nature.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/9/6 14:42
 * @Description: 场地列表dto
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
public class PlaceDto {
    private String id;

    private String images;

    private String videos;

    private String name;

    private String label;

    private Integer bugs;

    private List<String> labelList;

    private List<String> imageList;

    private List<String> videoList;

    private BigDecimal planPrice;
}
