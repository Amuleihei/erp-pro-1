/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.menu.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyeye.annotation.api.ApiModel;
import com.skyeye.annotation.api.ApiModelProperty;
import com.skyeye.annotation.api.Property;
import com.skyeye.common.entity.features.OperatorUserInfo;
import lombok.Data;

/**
 * @ClassName: SysMenuAuthPointMation
 * @Description: 菜单权限点实体类
 * @author: skyeye云系列--卫志强
 * @date: 2022/7/23 19:14
 * @Copyright: 2022 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Data
@TableName(value = "sys_eve_menu_auth_point")
@ApiModel("菜单权限点实体类")
public class AuthPoint extends OperatorUserInfo {

    @TableId("id")
    @ApiModelProperty(value = "主键id。为空时新增，不为空时编辑")
    private String id;

    @TableField(value = "object_id", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "所属菜单id【不可修改】", required = "required")
    private String objectId;

    @TableField(value = "object_key", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "所属菜单的类型(pc端或者手机端)【不可修改】", required = "required")
    private String objectKey;

    @TableField("`name`")
    @ApiModelProperty(value = "权限点名称/分组名称/数据权限名称", required = "required")
    private String name;

    @TableField("auth_menu")
    @ApiModelProperty(value = "接口id/分组标识/数据权限表达式", required = "required")
    private String authMenu;

    @TableField(value = "order_by")
    @ApiModelProperty(value = "排序", required = "num")
    private Integer orderBy;

    @TableField("menu_num")
    @Property(value = "菜单数字码（权限点）")
    private String menuNum;

    @TableField(value = "parent_id", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "所属父id，层级结构参考type字段【不可修改】", required = "required")
    private String parentId;

    @TableField(value = "type", updateStrategy = FieldStrategy.NEVER)
    @ApiModelProperty(value = "类型  1.权限点  2.数据分组(父级为1)  3.数据分组下的数据类型(父级为2)【不可修改】，参考#MenuPointType", required = "required,num")
    private String type;

}
