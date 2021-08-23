package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("unimall_box_product")
public class BoxProductDO extends SuperDO {

    @TableField("id")
    private Long id;
    @TableField("product_id")
    private Long productId;
    @TableField("box_id")
    private Long boxId;
    @TableField("level_id")
    private Long levelId;

}
