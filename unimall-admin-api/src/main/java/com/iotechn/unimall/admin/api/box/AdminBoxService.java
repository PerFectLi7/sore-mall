package com.iotechn.unimall.admin.api.box;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.dto.box.BoxDTO;
import com.iotechn.unimall.data.dto.box.BoxProductDTO;
import com.iotechn.unimall.data.dto.box.BoxProductReturnDTO;
import com.iotechn.unimall.data.model.Page;
import com.iotechn.unimall.data.domain.BoxDO;

import java.util.List;

/**
 * Generate Code By Unimall
 */
@HttpOpenApi(group = "admin.box", description = "unimall_boxService")
public interface AdminBoxService {

    @HttpMethod(description = "删除", permission = "box:box:delete", permissionParentName = "其他", permissionName = "Box")
    public boolean delete(
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "id") Long id,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;


    @HttpMethod(description = "查询", permission = "box:box:list", permissionParentName = "其他", permissionName = "Box")
    public Page<BoxDO> list(
            @HttpParam(name = "name", type = HttpParamType.COMMON, description = "name") String name,
            @HttpParam(name = "status", type = HttpParamType.COMMON, description = "status") Integer status,
            @HttpParam(name = "count", type = HttpParamType.COMMON, description = "count") Integer count,
            @HttpParam(name = "page", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer page,
            @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度", valueDef = "20") Integer limit,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "添加", permission = "box:box:create", permissionParentName = "其他", permissionName = "Box")
    public BoxDO create(
            @NotNull @HttpParam(name = "BoxDo", type = HttpParamType.COMMON, description = "抽奖箱Json数据") BoxDO boxDO,
            @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "编辑", permission = "box:box:edit", permissionParentName = "其他", permissionName = "Box")
    public BoxDO edit(
            @NotNull @HttpParam(name = "BoxDo", type = HttpParamType.COMMON, description = "抽奖箱Json数据") BoxDO boxDO,
            @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;

    @HttpMethod(description = "详情", permission = "box:boxproduct:detail", permissionParentName = "其他", permissionName = "BoxProduct")
    public BoxProductReturnDTO detail(
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "主键") Long id,
            @HttpParam(name = "productId", type = HttpParamType.COMMON, description = "productId") Long productId,
            @HttpParam(name = "levelId", type = HttpParamType.COMMON, description = "levelId") Long levelId,
            @HttpParam(name = "page", type = HttpParamType.COMMON, description = "页码", valueDef = "1") Integer page,
            @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度", valueDef = "20") Integer limit,
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId) throws ServiceException;
}
