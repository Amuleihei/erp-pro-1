/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.echarts.classenum;

import com.skyeye.common.base.classenum.SkyeyeEnumClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ReportModelState
 * @Description:
 * @author: skyeye云系列--卫志强
 * @date: 2024/4/3 10:13
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ReportModelState implements SkyeyeEnumClass {

    NORMAL(1, "正常", true, false),
    ABANDONED(2, "废弃", true, false);

    private Integer key;

    private String value;

    private Boolean show;

    private Boolean isDefault;

}
