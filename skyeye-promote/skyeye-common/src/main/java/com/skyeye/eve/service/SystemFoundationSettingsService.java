/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.eve.entity.SystemFoundationSettings;

import java.util.Map;

/**
 * @ClassName: SystemFoundationSettingsService
 * @Description: 系统基础设置服务接口类
 * @author: skyeye云系列--卫志强
 * @date: 2021/6/6 22:39
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface SystemFoundationSettingsService extends SkyeyeBusinessService<SystemFoundationSettings> {

    void querySystemFoundationSettingsList(InputObject inputObject, OutputObject outputObject);

}
