package com.iotechn.unimall.admin.api.box;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.dto.box.BoxProductDTO;
import com.iotechn.unimall.data.dto.box.BoxProductReturnDTO;
import com.iotechn.unimall.data.model.Page;
import com.iotechn.unimall.data.domain.BoxProductDO;

import java.util.Date;

/**
 * Generate Code By Unimall
 */
@HttpOpenApi(group = "admin.boxproduct", description = "unimall_box_productService")
public interface AdminBoxProductService {

    @HttpMethod(description = "删除", permission = "box:boxproduct:delete", permissionParentName = "其他", permissionName = "BoxProduct")
    public boolean delete(
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "id") Long id,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;


    @HttpMethod(description = "查询", permission = "box:boxproduct:list", permissionParentName = "其他", permissionName = "BoxProduct")
    public BoxProductReturnDTO list(
            @HttpParam(name = "productId", type = HttpParamType.COMMON, description = "productId") Long productId,
            @HttpParam(name = "boxId", type = HttpParamType.COMMON, description = "boxId") Long boxId,
            @HttpParam(name = "levelId", type = HttpParamType.COMMON, description = "levelId") Long levelId,
            @HttpParam(name = "page", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer page,
            @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度", valueDef = "20") Integer limit,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "添加", permission = "box:boxproduct:create", permissionParentName = "其他", permissionName = "BoxProduct")
    public BoxProductDTO create(
            @NotNull @HttpParam(name = "BoxProductDTO", type = HttpParamType.COMMON, description = "奖箱商品对象") BoxProductDTO boxProductDTO,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "编辑", permission = "box:boxproduct:edit", permissionParentName = "其他", permissionName = "BoxProduct")
    public BoxProductDTO edit(
            @NotNull @HttpParam(name = "BoxProductDTO", type = HttpParamType.COMMON, description = "奖箱商品对象") BoxProductDTO boxProductDTO,
            @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;
}
