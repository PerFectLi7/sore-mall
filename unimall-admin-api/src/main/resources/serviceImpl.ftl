package com.iotechn.unimall.admin.api.${packageName};


import com.iotechn.unimall.core.exception.AdminServiceException;
import com.iotechn.unimall.core.exception.ExceptionDefinition;
import com.iotechn.unimall.core.exception.ServiceException;
import com.iotechn.unimall.data.model.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotechn.unimall.data.domain.${doName}DO;
import com.iotechn.unimall.data.mapper.${doName}Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;


/**
 * Generate Code By Unimall
 */
@Service
public class ${serviceName}ServiceImpl implements ${serviceName}Service {

    @Autowired
    private ${doName}Mapper ${doLowCaseName}Mapper;

    @Override
    public boolean delete(Long id,  Long adminId) throws ServiceException {
        return ${doLowCaseName}Mapper.deleteById(id) > 0;
    }

    @Override
    public Page<${doName}DO> list(<#list columnDefinitionList! as column><#if column.query>${column.clazzName} ${column.alias}, </#if></#list>Integer page, Integer limit, Long adminId) throws ServiceException {
        QueryWrapper<${doName}DO> wrapper = new QueryWrapper<${doName}DO>();
        <#list columnDefinitionList! as column>
        <#if column.query>
        if (${column.alias} != null) {
            <#if column.likeQuery>
            wrapper.like("${column.name}", ${column.alias});
            <#else>
            wrapper.eq("${column.name}", ${column.alias});
            </#if>
        }
        </#if>
        </#list>
        Page<${doName}DO> p${doName}s = ${doLowCaseName}Mapper.selectPage(Page.div(page,limit,${doName}DO.class), wrapper);
        Integer count = ${doLowCaseName}Mapper.selectCount(wrapper);
        return p${doName}s;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${doName}DO create(<#list columnDefinitionList! as column><#if column.insertColumn>${column.clazzName} ${column.alias}, </#if></#list>Long adminId) throws ServiceException {
         Date now = new Date();
         ${doName}DO insertDO = new ${doName}DO();
         <#list columnDefinitionList! as column>
         <#if column.insertColumn>
         insertDO.set${column.alias?cap_first}(${column.alias});
         </#if>
         </#list>
         insertDO.setGmtUpdate(now);
         insertDO.setGmtCreate(now);
         if (${doLowCaseName}Mapper.insert(insertDO) > 0) {
             return insertDO;
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String edit(Long id, <#list columnDefinitionList! as column><#if column.insertColumn>${column.clazzName} ${column.alias}, </#if></#list>Long adminId) throws ServiceException {
         Date now = new Date();
         ${doName}DO updateDO = new ${doName}DO();
         updateDO.setId(id);
         <#list columnDefinitionList! as column>
         <#if column.insertColumn>
         updateDO.set${column.alias?cap_first}(${column.alias});
         </#if>
         </#list>
         updateDO.setGmtUpdate(now);
         if (${doLowCaseName}Mapper.updateById(updateDO) > 0) {
             return "ok";
         }
         throw new AdminServiceException(ExceptionDefinition.ADMIN_UNKNOWN_EXCEPTION);
    }

}
