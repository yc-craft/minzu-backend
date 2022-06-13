package com.yupi.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * minzu
 * @TableName minzu
 */
@TableName(value ="minzu")
@Data
public class Minzu implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源名
     */
    private String minzuName;

    /**
     * url
     */
    private String minzuUrl;

    /**
     * 民族类型
     */
    private String minzuType;

    /**
     * 0 - 图片 1 - 音频 2 - 视频
     */
    private Integer minzuSource;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}