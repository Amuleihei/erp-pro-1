/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.classenum;

import com.skyeye.common.base.classenum.SkyeyeEnumClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ActModelState
 * @Description: 工作流配置状态的枚举类
 * @author: skyeye云系列--卫志强
 * @date: 2022/11/24 22:46
 * @Copyright: 2022 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ActModelState implements SkyeyeEnumClass {

    NEW(1, "新建", false, false),
    UP(2, "上线", false, false),
    DOWN(3, "下线", false, false);

    private Integer key;

    private String value;

    private Boolean show;

    private Boolean isDefault;

}
