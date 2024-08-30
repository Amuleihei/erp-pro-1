/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.school.measurement.classnum;

import com.skyeye.common.base.classenum.SkyeyeEnumClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName: MeasurementTimeState
 * @Description: 测试的时间状态枚举类
 * @author: skyeye云系列--卫志强
 * @date: 2024/7/2 15:37
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MeasurementTimeState implements SkyeyeEnumClass {

    IN_PROGRESS("inProgress", "进行中", true, false),
    EXPIRED("expired", "已截止", true, false);

    private String key;

    private String value;

    private Boolean show;

    private Boolean isDefault;
}
