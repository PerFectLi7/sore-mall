package com.iotechn.unimall.admin.api.nproduct;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.dto.goods.AdminSpuDTO;
import com.iotechn.unimall.data.dto.product.AdminProductDTO;
import com.iotechn.unimall.data.model.Page;
import com.iotechn.unimall.data.domain.ProductDO;

/**
 * Generate Code By Unimall
 */
@HttpOpenApi(group = "admin.newproduct", description = "unimall_productService")
public interface AdminNewProductService {

    @HttpMethod(description = "删除", permission = "product:product:delete", permissionParentName = "其他", permissionName = "Product")
    public String delete(
            @NotNull @HttpParam(name = "productId", type = HttpParamType.COMMON, description = "商品id") Long productId,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;


    @HttpMethod(description = "查询", permission = "nproduct:product:list", permissionParentName = "其他", permissionName = "Product")
    public Page<ProductDO> list(
            @HttpParam(name = "page", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer page,
            @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度", valueDef = "20") Integer limit,
            @HttpParam(name = "points", type = HttpParamType.COMMON, description = "points") Integer points,
            @HttpParam(name = "title", type = HttpParamType.COMMON, description = "搜索标题") String title,
            @HttpParam(name = "sales", type = HttpParamType.COMMON, description = "sales") Integer sales,
            @HttpParam(name = "stock", type = HttpParamType.COMMON, description = "stock") Integer stock,
            @HttpParam(name = "status", type = HttpParamType.COMMON, description = "status") Integer status,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "详情", permission = "product:product:detail", permissionParentName = "商品管理", permissionName = "商品管理")
    public AdminProductDTO detail(
            @NotNull @HttpParam(name = "proId", type = HttpParamType.COMMON, description = "商品Id") Long spuId,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员Id") Long adminId) throws ServiceException;

    @HttpMethod(description = "创建", permission = "product:product:create", permissionParentName = "其他", permissionName = "Product")
    public String create(
            @NotNull @HttpParam(name = "productDTO", type = HttpParamType.COMMON, description = "商品JSON数据") AdminProductDTO productDTO,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "编辑", permission = "product:product:edit", permissionParentName = "其他", permissionName = "Product")
    public String edit(
            @NotNull @HttpParam(name = "productDTO", type = HttpParamType.COMMON, description = "商品JSON数据") AdminProductDTO productDTO,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;
}
