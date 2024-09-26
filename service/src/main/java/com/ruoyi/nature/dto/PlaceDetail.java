package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CPlace;
import com.ruoyi.nature.domain.CSche;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/8/10 15:26
 * @Description: 场地详情
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
public class PlaceDetail extends CPlace {
    private List<Map> sches;

    private String topicName;

    private String dayTypeStr;

    private List<String> tags;

    private List<Map> useds;

    private List<String> imageList;
}
