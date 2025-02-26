/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.project.service;

import com.skyeye.base.business.service.SkyeyeFlowableService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.project.entity.Project;

/**
 * @ClassName: ProProjectService
 * @Description: 项目管理服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2023/8/1 16:24
 * @Copyright: 2023 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目
 */
public interface ProProjectService extends SkyeyeFlowableService<Project> {

    void executeProjectById(InputObject inputObject, OutputObject outputObject);

    void perfectProjectById(InputObject inputObject, OutputObject outputObject);

}
