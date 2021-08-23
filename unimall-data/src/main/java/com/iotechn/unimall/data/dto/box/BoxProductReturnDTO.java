package com.iotechn.unimall.data.dto.box;

import com.iotechn.unimall.data.domain.BoxDO;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.dto.product.ProductSimpleDTO;
import com.iotechn.unimall.data.model.Page;
import lombok.Data;

import java.util.List;

@Data
public class BoxProductReturnDTO {

    private Page<BoxProductDTO> boxProductDTOPage;

    private List<ProductSimpleDTO> productSimpleDTOS;

    private List<LevelDO> levelDOS;

    private List<BoxDO> boxDOS;
}
