package com.iotechn.unimall.admin.api.box;


import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.BoxProductDO;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.dto.CategoryDTO;
import com.iotechn.unimall.data.dto.box.BoxDTO;
import com.iotechn.unimall.data.dto.box.BoxProductDTO;
import com.iotechn.unimall.data.dto.box.BoxProductReturnDTO;
import com.iotechn.unimall.data.dto.product.ProductSimpleDTO;
import com.iotechn.unimall.data.mapper.BoxProductMapper;
import com.iotechn.unimall.data.mapper.LevelMapper;
import com.iotechn.unimall.data.mapper.ProductMapper;
import com.iotechn.unimall.data.model.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotechn.unimall.data.domain.BoxDO;
import com.iotechn.unimall.data.mapper.BoxMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Generate Code By Unimall
 */
@Service
public class AdminBoxServiceImpl implements AdminBoxService {

    @Autowired
    private BoxMapper boxMapper;

    @Autowired
    private BoxProductMapper boxProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private LevelMapper levelMapper;



    @Override
    public boolean delete(Long id,  Long adminId) throws ServiceException {
        return boxMapper.deleteById(id) > 0;
    }

    @Override
    public Page<BoxDO> list(String name, Integer status, Integer count, Integer page, Integer limit, Long adminId) throws ServiceException {
        QueryWrapper<BoxDO> wrapper = new QueryWrapper<BoxDO>();
        if (name != null) {
            wrapper.like("name", name);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (count != null) {
            wrapper.eq("count", count);
        }
        Page<BoxDO> pBoxs = boxMapper.selectPage(Page.div(page,limit,BoxDO.class), wrapper);
        Integer counts = boxMapper.selectCount(wrapper);
        return pBoxs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoxDO create(BoxDO boxDO,Long adminId) throws ServiceException {
         Date now = new Date();
        boxDO.setGmtUpdate(now);
        boxDO.setGmtCreate(now);
         if (boxMapper.insert(boxDO) > 0) {
             return boxDO;
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoxDO edit(BoxDO boxDO, Long adminId) throws ServiceException {
         Date now = new Date();
        boxDO.setGmtUpdate(now);
         if (boxMapper.updateById(boxDO) > 0) {
             return boxDO;
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    public BoxProductReturnDTO detail(Long boxId, Long productId, Long levelId, Integer page, Integer limit, Long adminId) throws ServiceException {
        QueryWrapper<BoxProductDO> wrapper = new QueryWrapper<>();
        QueryWrapper<LevelDO> leveWrapper = new QueryWrapper<>();
        QueryWrapper<BoxDO> boxWrapper = new QueryWrapper<>();
        if (boxId != null) {
            wrapper.eq("box_id",boxId);
        }
        if (productId != null) {
            wrapper.eq("product_id",productId);
        }
        if (levelId != null) {
            wrapper.eq("level_id",levelId);
        }
        BoxProductReturnDTO boxProductReturnDTO = new BoxProductReturnDTO();

        List<ProductSimpleDTO> productSimpleDTOS = productMapper.selectSimpleProduct();
        List<LevelDO> levelDOS = levelMapper.selectList(leveWrapper);
        List<BoxDO> boxDOS = boxMapper.selectList(boxWrapper);
        boxProductReturnDTO.setProductSimpleDTOS(productSimpleDTOS);
        boxProductReturnDTO.setLevelDOS(levelDOS);
        boxProductReturnDTO.setBoxDOS(boxDOS);
        Page<BoxProductDTO> boxProductDOPage = boxProductMapper.selectPage(Page.div(page, limit, BoxProductDO.class), wrapper).trans(item -> {
            BoxProductDTO boxProductDTO = new BoxProductDTO();
            BeanUtils.copyProperties(item,boxProductDTO);
            for (BoxDO boxDO : boxDOS) {
                if (boxDO.getId().equals(item.getBoxId())) {
                    boxProductDTO.setBoxName(boxDO.getName());
                }
            }
            for (ProductSimpleDTO productSimpleDTO : productSimpleDTOS) {
                if (productSimpleDTO.getId().equals(item.getProductId())) {
                    boxProductDTO.setProductName(productSimpleDTO.getTitle());
                }
            }
            for (LevelDO levelDO : levelDOS) {
                if (levelDO.getId().equals(item.getLevelId())) {
                    boxProductDTO.setLevelName(levelDO.getName());
                }
            }
            return boxProductDTO;

        });
         boxProductReturnDTO.setBoxProductDTOPage(boxProductDOPage);
        return boxProductReturnDTO;
    }

}
