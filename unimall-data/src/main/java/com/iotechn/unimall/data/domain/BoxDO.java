package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("unimall_box")
public class BoxDO extends SuperDO {

    @TableField("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("status")
    private Integer status;
    @TableField("count")
    private Integer count;
    @TableField("img")
    private String img;
    @TableField("gmt_update")
    private Date gmtUpdate;
    @TableField("gmt_create")
    private Date gmtCreate;


}
