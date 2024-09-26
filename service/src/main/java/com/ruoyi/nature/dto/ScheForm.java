package com.ruoyi.nature.dto;

import com.ruoyi.nature.domain.CSche;
import lombok.Data;

import java.util.List;

/**
 * @Author: CaoYong
 * @Version: V1.0
 * @Program: veror-svp
 * @Date: 2021/12/4 20:49
 * @Description:
 */
@Data
public class ScheForm {
    private String id;

    private List<CSche> domains;
}
