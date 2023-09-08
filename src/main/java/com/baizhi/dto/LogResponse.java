package com.baizhi.dto;

import com.google.j2objc.annotations.Property;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class LogResponse implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 管理员用户名
     */
    private String adminName;

    /**
     * 操作时间
     */
    private Date createTime;

    private String methodName;

    /**
     * 操作是否成功
     */
    private boolean optionStatus;

    /**
     *
     */
    private Date gmtModified;

    /**
     *
     */

    private static final long serialVersionUID = 1L;
}
