package com.iotechn.unimall.data.dto.box;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iotechn.unimall.data.domain.BoxProductDO;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.dto.SuperDTO;
import com.iotechn.unimall.data.dto.product.ProductSimpleDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("unimall_box")
public class BoxDTO extends SuperDTO {

    private Long id;

    private String name;

    private Integer status;

    private Integer count;

    private String img;

    private Date gmtUpdate;

    private Date gmtCreate;
}
