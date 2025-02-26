/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.meal.classenum;

import com.skyeye.common.base.classenum.SkyeyeEnumClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ShopMealOrderType
 * @Description: 套餐订单来源枚举类
 * @author: skyeye云系列--卫志强
 * @date: 2024/5/13 17:18
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ShopMealOrderType implements SkyeyeEnumClass {

    MEMBER(1, "用户下单", true, false),
    WORK_USER(2, "工作人员下单", true, false);

    private Integer key;

    private String value;

    private Boolean show;

    private Boolean isDefault;

}
