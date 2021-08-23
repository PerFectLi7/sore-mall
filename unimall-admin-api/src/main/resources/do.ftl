package com.iotechn.unimall.data.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
<#list importClasses! as clazz>
import ${clazz};
</#list>
import lombok.Data;

/**
 * Generate Code By Unimall
 */
@Data
@TableName("${tableName}")
public class ${doName}DO extends SuperDO {

    <#list columnDefinitionList! as column>
    @TableField("${column.name}")
    private ${column.clazzName} ${column.alias};
    </#list>

}
