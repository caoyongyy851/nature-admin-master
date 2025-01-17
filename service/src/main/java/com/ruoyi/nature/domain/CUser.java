package com.ruoyi.nature.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信用户对象 c_user
 *
 * @author ruoyi
 * @date 2020-10-28
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("c_user")
public class CUser implements Serializable {

private static final long serialVersionUID=1L;


    /** 用户id */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /** openid */
    @Excel(name = "openid")
    private String openid;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 性别 */
    @Excel(name = "性别")
    private Boolean sex;

    /** 年龄 */
    @Excel(name = "年龄")
    private Integer age;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 城市 */

    @Excel(name = "城市")
    private String city;

    /** 用户签名 */
    @Excel(name = "用户签名")
    private String sign;

    /** 是否小黑屋 */
    @Excel(name = "是否小黑屋")
    private Boolean disabled;

    /** 最近一次登录 */
    @Excel(name = "最近一次登录" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date lastLoginTime;

    /** 秘籍数量 */
    @Excel(name = "秘籍数量")
    private Integer cheats;

    /** 公司id */
    @Excel(name = "公司id")
    private String companyId;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    @TableLogic
    private Boolean deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 积分 */
    @Excel(name = "积分")
    private Integer points;

    /** 现金 */
    @Excel(name = "现金")
    private BigDecimal cashs;

    public void setNull() {
//        id = null;
//        openid = null;
//        points = null;
//        cashs = null;
    }
}
