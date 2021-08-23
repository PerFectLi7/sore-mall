package com.iotechn.unimall.data.dto.order;

import com.iotechn.unimall.data.domain.ProductDO;
import com.iotechn.unimall.data.dto.SuperDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by rize on 2019/7/6.
 */
@Data
public class OrderNewDTO extends SuperDTO {


    private String orderNo;

    private Long userId;

    private Long productId;

    private Long addrId;

    private Integer points;

    private Integer status;

    private String shipNo;

    private String shipCode;

    private String province;

    private String city;

    private String county;

    private String address;

    private String phone;

    private String consignee;

    private Date gmtShip;

    private Date gmtConfirm;

    private String productName;

    private String userNick;

    private ProductDO productDO;

}
