package com.iotechn.unimall.data.mapper;

import com.iotechn.unimall.data.domain.OrderDO;
import com.iotechn.unimall.data.domain.OrderNewDO;
import com.iotechn.unimall.data.dto.order.OrderDTO;
import com.iotechn.unimall.data.model.KVModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by rize on 2019/7/6.
 */
public interface OrderNewMapper extends IMapper<OrderNewDO> {
}
