package com.iotechn.unimall.admin.api.level;

import com.iotechn.unimall.core.annotation.HttpMethod;
import com.iotechn.unimall.core.annotation.HttpOpenApi;
import com.iotechn.unimall.core.annotation.HttpParam;
import com.iotechn.unimall.core.annotation.HttpParamType;
import com.iotechn.unimall.core.annotation.param.NotNull;
import com.iotechn.unimall.core.annotation.param.Range;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.domain.LevelDO;
import com.iotechn.unimall.data.domain.UserDO;
import com.iotechn.unimall.data.model.Page;

import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-11
 * Time: 下午7:30
 */

@HttpOpenApi(group = "admin.level", description = "等级管理")
public interface AdminLevelService {

    @HttpMethod(description = "创建", permission = "level:level:create", permissionParentName = "等级管理", permissionName = "等级管理")
    public LevelDO create(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "levelDo", type = HttpParamType.COMMON, description = "奖品等级信息") LevelDO levelDO) throws ServiceException;

    @HttpMethod(description = "删除", permission = "level:level:delete", permissionParentName = "等级管理", permissionName = "等级管理")
    public Boolean delete(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "id", type = HttpParamType.COMMON, description = "等级Id") Long id) throws ServiceException;

    @HttpMethod(description = "修改", permission = "level:level:edit", permissionParentName = "等级管理", permissionName = "等级管理")
    public LevelDO edit(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @NotNull @HttpParam(name = "levelDo", type = HttpParamType.COMMON, description = "奖品等级信息") LevelDO levelDO) throws ServiceException;

    @HttpMethod(description = "查询", permission = "level:level:list", permissionParentName = "等级管理", permissionName = "等级管理")
    public Page<LevelDO> list(
            @NotNull @HttpParam(name = "adminId", type = HttpParamType.ADMIN_ID, description = "管理员ID") Long adminId,
            @Range(min = 1) @HttpParam(name = "page", type = HttpParamType.COMMON, description = "当前页码") Integer pageNo,
            @Range(min = 1) @HttpParam(name = "limit", type = HttpParamType.COMMON, description = "页码长度") Integer limit) throws ServiceException;

}
