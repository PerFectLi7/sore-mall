package com.iotechn.unimall.data.dto.box;

import com.iotechn.unimall.data.dto.SuperDTO;
import lombok.Data;


/**
 * Generate Code By Unimall
 */
@Data
public class BoxProductDTO extends SuperDTO {

    private Long id;

    private Long productId;

    private String productName;

    private Long boxId;

    private String boxName;

    private Long levelId;

    private String LevelName;
}
