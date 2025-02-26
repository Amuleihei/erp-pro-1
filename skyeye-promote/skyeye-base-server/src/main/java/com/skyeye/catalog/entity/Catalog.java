/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.catalog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.skyeye.annotation.api.ApiModel;
import com.skyeye.annotation.api.ApiModelProperty;
import com.skyeye.annotation.cache.RedisCacheField;
import com.skyeye.annotation.unique.UniqueField;
import com.skyeye.common.constans.CacheConstants;
import com.skyeye.common.constans.RedisConstants;
import com.skyeye.common.entity.features.OperatorUserInfo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: Catalog
 * @Description: 目录信息实体类
 * @author: skyeye云系列--卫志强
 * @date: 2022/11/28 22:07
 * @Copyright: 2022 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Data
@UniqueField(value = {"name", "parentId", "objectKey"})
@RedisCacheField(name = CacheConstants.SKYEYE_CATALOG_CACHE_KEY, cacheTime = RedisConstants.THIRTY_DAY_SECONDS)
@TableName(value = "skyeye_catalog")
@ApiModel("目录信息实体类")
public class Catalog extends OperatorUserInfo {

    @TableId("id")
    @ApiModelProperty(value = "主键id。为空时新增，不为空时编辑")
    private String id;

    @TableField(value = "`name`")
    @ApiModelProperty(value = "目录名称", required = "required")
    private String name;

    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;

    @TableField(value = "parent_id")
    @ApiModelProperty(value = "所属父节点id", required = "required", defaultValue = "0")
    private String parentId;

    /**
     * 所有的父节点id，用逗号隔开，有序
     */
    @TableField(exist = false)
    private String parentIds;

    @TableField(value = "object_id", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "目录所属业务对象数据的id")
    private String objectId;

    @TableField(value = "object_app_id", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "目录所属业务对象数据的应用id", required = "required")
    private String objectAppId;

    @TableField(value = "object_key", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "目录所属业务对象", required = "required")
    private String objectKey;

    /**
     * 目录子对象，查询时使用
     */
    @TableField(exist = false)
    private List<Catalog> children;

    @TableField(value = "type", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "目录类型  1. 公共  2.私有", required = "required,num")
    private Integer type;

    /**
     * 默认所有节点都是文件夹
     */
    @TableField(exist = false)
    private Boolean isParent = true;

    /**
     * 对象类型，默认是catalog
     */
    @TableField(exist = false)
    private String objectType = "catalog";

}
