package com.iotechn.unimall.data.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import lombok.Data;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("unimall_product")
public class ProductDO extends SuperDO {

    @TableField("id")
    private Long id;
    @TableField("points")
    private Integer points;
    @TableField("title")
    private String title;
    @TableField("sales")
    private Integer sales;
    @TableField("stock")
    private Integer stock;
    @TableField("img")
    private String img;
    @TableField("detail")
    private String detail;
    @TableField("description")
    private String description;
    @TableField("status")
    private Integer status;
    @TableField("gmt_update")
    private Date gmtUpdate;
    @TableField("gmt_create")
    private Date gmtCreate;

}
