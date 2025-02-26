/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.variable.service;

import com.skyeye.base.business.service.SkyeyeTeamAuthService;
import com.skyeye.variable.entity.AutoVariable;

import java.util.Map;

/**
 * @ClassName: AutoVariableService
 * @Description: 变量管理服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/3/26 9:03
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface AutoVariableService extends SkyeyeTeamAuthService<AutoVariable> {

    Map<String, String> getAutoVariable(String type, String environmentId);

}
