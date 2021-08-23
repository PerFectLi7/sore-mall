package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("unimall_level")
public class LevelDO extends SuperDO {

    @TableField("id")
    private Long id;
    @TableField("name")
    private String name;

}
