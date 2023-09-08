package com.baizhi.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.google.j2objc.annotations.Property;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName yx_log
 */
@TableName(value ="yx_log")
@Data
public class Log implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 管理员用户名
     */
    private String adminName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Property("createTime")
    private Date gmtCreate;

    /**
     * 操作方法名
     */
    private String methodName;

    /**
     * 操作是否成功
     */
    private Integer optionStatus;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * 
     */
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}