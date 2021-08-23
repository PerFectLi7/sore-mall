package com.iotechn.unimall.data.mapper;

import com.iotechn.unimall.data.domain.ProductDO;
import com.iotechn.unimall.data.dto.product.ProductSimpleDTO;

import java.util.List;

/**
 * Generate Code By Unimall
 */
public interface ProductMapper extends IMapper<ProductDO> {

   public List<ProductSimpleDTO> selectSimpleProduct();

}
