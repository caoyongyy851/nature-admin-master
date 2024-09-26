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
 * 公司/机构对象 c_company
 *
 * @author ruoyi
 * @date 2021-12-05
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_company")
public class CCompany implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 公司/机构名称 */
    @Excel(name = "公司/机构名称")
    private String companyName;

    /** 公司地址 */
    @Excel(name = "公司地址")
    private String address;

    /** 公司电话 */
    @Excel(name = "公司电话")
    private String phone;

    /** 公司简介 */
    @Excel(name = "公司简介")
    private String remark;

    /** LOGO */
    @Excel(name = "LOGO")
    private String cover;

    /** 申请人id */
    @Excel(name = "申请人id")
    private String userId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Boolean deleted;

    /** 0：待审核，1：启用，2：禁用 */
    @Excel(name = "0：待审核，1：启用，2：禁用")
    private Integer status;
}
