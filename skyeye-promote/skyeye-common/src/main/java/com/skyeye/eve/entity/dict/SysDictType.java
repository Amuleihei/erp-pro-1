/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.entity.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyeye.annotation.api.ApiModel;
import com.skyeye.annotation.api.ApiModelProperty;
import com.skyeye.common.entity.features.OperatorUserInfo;
import lombok.Data;

/**
 * @ClassName: SysDictType
 * @Description: 数据字典类型实体类
 * @author: skyeye云系列--卫志强
 * @date: 2022/6/30 22:35
 * @Copyright: 2022 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Data
@TableName(value = "sys_dict_type")
@ApiModel("数据字典类型实体类")
public class SysDictType extends OperatorUserInfo {

    @TableId("id")
    @ApiModelProperty(value = "主键id。为空时新增，不为空时编辑")
    private String id;

    @TableField("dict_name")
    @ApiModelProperty(value = "字典分类名称", required = "required", fuzzyLike = true)
    private String dictName;

    @TableField("dict_code")
    @ApiModelProperty(value = "字典分类CODE，需要保证唯一", required = "required", fuzzyLike = true)
    private String dictCode;

    @TableField("remark")
    @ApiModelProperty(value = "备注信息")
    private String remark;

    @TableField("dict_type")
    @ApiModelProperty(value = "字典类型  1. 一级分类  2. 多级分类", required = "required,num")
    private Integer dictType;

    @TableField("choose_level")
    @ApiModelProperty(value = "几级之后的可以选择，比如设置2级(包含2级)之后的可以选择", required = "required,num")
    private Integer chooseLevel;

    @TableField("enabled")
    @ApiModelProperty(value = "状态（1 启用  2.停用）", required = "required,num")
    private Integer enabled;

}
