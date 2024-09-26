package com.ruoyi.nature.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档对象 c_doc
 *
 * @author ruoyi
 * @date 2021-11-25
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_doc")
public class CDoc implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 内容 */
    @Excel(name = "内容")
    private String detail;

    /** 类型（1：玩场责任书） */
    @Excel(name = "类型" , readConverterExp = "1=：玩场责任书")
    private Integer type;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Boolean deleted;

    /** 阅览数 */
    @Excel(name = "阅览数")
    private Long views;
}
