package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by rize on 2019/7/5.
 */
@Data
@TableName("unimall_ordernew")
public class OrderNewDO extends SuperDO {

    @TableField("order_no")
    private String orderNo;
    @TableField("user_id")
    private Long userId;
    @TableField("product_id")
    private Long productId;
    @TableField("addr_id")
    private Long addrId;
    @TableField("status")
    private Integer status;
    @TableField("points")
    private Integer points;
    @TableField("product_count")
    private Integer count;
    @TableField("ship_no")
    private String shipNo;
    @TableField("gmt_ship")
    private Date gmtShip;
    @TableField("gmt_confirm")
    private Date gmtConfirm;
}
