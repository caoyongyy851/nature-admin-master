package com.ruoyi.nature.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 话题封面对象 c_topic_cover
 *
 * @author ruoyi
 * @date 2021-12-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_topic_cover")
public class CTopicCover implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 话题类型 */
    @Excel(name = "话题类型")
    private Integer topicType;

    /** 封面图 */
    @Excel(name = "封面图")
    private String coverImg;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private String deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;
    /** 英文标签 */
    @Excel(name = "英文标签")
    private String tagEn;
    /** 分类名称 */
    @Excel(name = "分类名称")
    private String tagName;
}
