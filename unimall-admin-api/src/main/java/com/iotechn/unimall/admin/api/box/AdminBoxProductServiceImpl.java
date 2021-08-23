package com.iotechn.unimall.admin.api.box;


import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.BoxDO;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.dto.box.BoxProductDTO;
import com.iotechn.unimall.data.dto.box.BoxProductReturnDTO;
import com.iotechn.unimall.data.dto.product.ProductSimpleDTO;
import com.iotechn.unimall.data.mapper.BoxMapper;
import com.iotechn.unimall.data.mapper.LevelMapper;
import com.iotechn.unimall.data.mapper.ProductMapper;
import com.iotechn.unimall.data.model.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotechn.unimall.data.domain.BoxProductDO;
import com.iotechn.unimall.data.mapper.BoxProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;


/**
 * Generate Code By Unimall
 */
@Service
public class AdminBoxProductServiceImpl implements AdminBoxProductService {

    @Autowired
    private BoxProductMapper boxProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private BoxMapper boxMapper;

    @Override
    public boolean delete(Long id,  Long adminId) throws ServiceException {
        return boxProductMapper.deleteById(id) > 0;
    }

    @Override
    public BoxProductReturnDTO list(Long productId, Long boxId, Long levelId, Integer page, Integer limit, Long adminId) throws ServiceException {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoxProductDTO create(BoxProductDTO insertDTO, Long adminId) throws ServiceException {
         Date now = new Date();
         BoxProductDO boxProductDO = new BoxProductDO();
         BeanUtils.copyProperties(insertDTO,boxProductDO);
        boxProductDO.setGmtUpdate(now);
        boxProductDO.setGmtCreate(now);
         if (boxProductMapper.insert(boxProductDO) > 0) {
             insertDTO.setId(boxProductDO.getId());
             insertDTO.setBoxName(boxMapper.selectById(boxProductDO.getBoxId()).getName());
             insertDTO.setProductName(productMapper.selectById(boxProductDO.getProductId()).getTitle());
             insertDTO.setLevelName(levelMapper.selectById(boxProductDO.getLevelId()).getName());
             return insertDTO;
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BoxProductDTO edit(BoxProductDTO updateDTO, Long adminId) throws ServiceException {
         Date now = new Date();
        BoxProductDO boxProductDO = new BoxProductDO();
        BeanUtils.copyProperties(updateDTO,boxProductDO);
        boxProductDO.setGmtUpdate(now);
         if (boxProductMapper.updateById(boxProductDO) > 0) {
             updateDTO.setBoxName(boxMapper.selectById(boxProductDO.getBoxId()).getName());
             updateDTO.setProductName(productMapper.selectById(boxProductDO.getProductId()).getTitle());
             updateDTO.setLevelName(levelMapper.selectById(boxProductDO.getLevelId()).getName());
             return updateDTO;
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }
}
