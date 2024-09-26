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
 * 帖子轮播图对象 c_swiper_topic
 *
 * @author ruoyi
 * @date 2021-11-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_swiper_topic")
public class CSwiperTopic implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** $column.columnComment */
    @Excel(name = "话题id")
    private String topicId;

    /** $column.columnComment */
    @Excel(name = "排序号")
    private Integer swiperOrder;

    @Excel(name = "类型")
    private Integer topicType;

    @Excel(name = "图片地址")
    private String image;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "标题")
    private String title;
}
