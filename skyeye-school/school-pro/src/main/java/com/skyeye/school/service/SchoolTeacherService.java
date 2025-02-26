/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.school.service;

import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;

public interface SchoolTeacherService {

    void querySchoolTeacherToTableList(InputObject inputObject, OutputObject outputObject);

    void querySchoolTeacherListByStaffIds(InputObject inputObject, OutputObject outputObject);

}
