package com.iotechn.unimall.admin.api.nproduct;


import com.iotechn.unimall.biz.service.storage.StorageBizService;
import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.ImgDO;
import com.iotechn.unimall.data.dto.goods.AdminSpuDTO;
import com.iotechn.unimall.data.dto.product.AdminProductDTO;
import com.iotechn.unimall.data.enums.BizType;
import com.iotechn.unimall.data.enums.SpuStatusType;
import com.iotechn.unimall.data.mapper.ImgMapper;
import com.iotechn.unimall.data.model.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotechn.unimall.data.domain.ProductDO;
import com.iotechn.unimall.data.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Generate Code By Unimall
 */
@Service
public class AdminNewProductServiceImpl implements AdminNewProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ImgMapper imgMapper;
    @Autowired
    private StorageBizService storageBizService;


    @Override
    public String delete(Long productId,  Long adminId) throws ServiceException {
        //TODO 判断该商品是否正在抽奖
        imgMapper.delete(new QueryWrapper<ImgDO>().eq("biz_id", productId).eq("biz_type", BizType.GOODS.getCode()));
        // 将需要删除的图片路径取出来
        List<String> deleteImgList = imgMapper.getImgs(BizType.GOODS.getCode(), productId);
        storageBizService.delete(deleteImgList);

        if (productMapper.deleteById(productId) > 0) {
            return "ok";
        }
        throw new AdminServiceException(ExceptionDefinition.GOODS_NOT_EXIST);
    }

    @Override
    public Page<ProductDO> list(Integer page, Integer limit, Integer points, String title, Integer sales, Integer stock, Integer status,Long adminId) throws ServiceException {
        QueryWrapper<ProductDO> wrapper = new QueryWrapper<ProductDO>();
        // 1.标题搜索
        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
        }
        if (points != null) {
            wrapper.eq("points", points);
        }
        if (sales != null) {
            wrapper.eq("sales", sales);
        }
        if (stock != null) {
            wrapper.eq("stock", stock);
        }
        // 3.状态搜索
        if (status != null) {
            wrapper.eq("status", status.intValue() <= SpuStatusType.STOCK.getCode() ? SpuStatusType.STOCK.getCode() : SpuStatusType.SELLING.getCode());
        }
        Page<ProductDO> dtoPage = productMapper.selectPage(Page.div(page, limit, ProductDO.class), wrapper);
        return dtoPage;
    }
    @Override
    public AdminProductDTO detail(Long proId, Long adminId) throws ServiceException {
        ProductDO productDO = productMapper.selectById(proId);
        AdminProductDTO adminProductDTO = new AdminProductDTO();
        BeanUtils.copyProperties(productDO, adminProductDTO);
        // 0. imgList
        List<String> imgList = imgMapper.getImgs(BizType.GOODS.getCode(), proId);
        adminProductDTO.setImgList(imgList);
        return adminProductDTO;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(AdminProductDTO adminProductDTO, Long adminId) throws ServiceException {
        // 1.参数校验
        if (adminProductDTO.getId() != null) {
            throw new AdminServiceException(ExceptionDefinition.GOODS_CREATE_HAS_ID);
        }
         Date now = new Date();
         ProductDO productDO = new ProductDO();
         BeanUtils.copyProperties(adminProductDTO,productDO);
        productDO.setGmtUpdate(now);
        productDO.setGmtCreate(now);
         if (productMapper.insert(productDO) > 0) {
             // 2.4.插入IMG
             insertProImg(adminProductDTO, productDO.getId(), now);
             return "ok";
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String edit(AdminProductDTO productDTO, Long adminId) throws ServiceException {
        // 1. 参数校验
        if (productDTO.getId() == null) {
            throw new AdminServiceException(ExceptionDefinition.PARAM_CHECK_FAILED);
        }
         Date now = new Date();
         ProductDO productDO = new ProductDO();
         BeanUtils.copyProperties(productDTO,productDO);
        productDO.setGmtUpdate(now);

        imgMapper.delete(new QueryWrapper<ImgDO>().eq("biz_id", productDO.getId()).eq("biz_type", BizType.GOODS.getCode()));
        // 2.4. 插入IMG
        insertProImg(productDTO, productDO.getId(), now);
         if (productMapper.updateById(productDO) > 0) {
             return "ok";
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    private void insertProImg(AdminProductDTO adminProductDTO, Long bizId, Date now) {
        List<String> imgList = adminProductDTO.getImgList();
        List<ImgDO> imgDOList = imgList.stream().map(item -> {
            ImgDO imgDO = new ImgDO();
            imgDO.setBizType(BizType.GOODS.getCode());
            imgDO.setBizId(bizId);
            imgDO.setUrl(item);
            imgDO.setGmtCreate(now);
            imgDO.setGmtUpdate(now);
            return imgDO;
        }).collect(Collectors.toList());
        imgMapper.insertImgs(imgDOList);
    }
}
