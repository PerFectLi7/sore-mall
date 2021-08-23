package com.iotechn.unimall.data.dto.product;

import com.iotechn.unimall.data.domain.ProductDO;
import lombok.Data;

import java.util.List;

/**
 * Created by rize on 2019/7/2.
 */
@Data
public class AdminProductDTO extends ProductDO {


    private Integer points;

    private Integer stock;

    private Integer sales;

    private String title;

    /**
     * 主图
     */
    private String img;

    /**
     * 后面的图，仅在详情接口才出现
     */
    private List<String> imgList;

    private String detail;

    private String description;

    private Integer status;
}
