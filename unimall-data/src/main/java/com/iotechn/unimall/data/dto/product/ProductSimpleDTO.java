package com.iotechn.unimall.data.dto.product;

import com.iotechn.unimall.data.dto.SuperDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by rize on 2019/7/2.
 */
@Data
public class ProductSimpleDTO extends SuperDTO {

    private Integer points;

    private String title;

    private String img;
}
