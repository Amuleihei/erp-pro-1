/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.purchase.classenum;

import com.skyeye.common.base.classenum.SkyeyeEnumClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName: PurchaseOrderFromType
 * @Description: 采购订单来源单据类型
 * @author: skyeye云系列--卫志强
 * @date: 2024/5/22 10:58
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum PurchaseOrderFromType implements SkyeyeEnumClass {

    SUPPLIER_CONTRACT(1, "采购合同", true, false),
    DELIVERY_PLAN(2, "出货计划", true, false);

    private Integer key;

    private String value;

    private Boolean show;

    private Boolean isDefault;

}
