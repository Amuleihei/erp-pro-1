/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.history.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.history.entity.AutoHistoryStepDatabase;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AutoHistoryStepDatabaseService
 * @Description: 数据库执行历史服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/4/17 8:12
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface AutoHistoryStepDatabaseService extends SkyeyeBusinessService<AutoHistoryStepDatabase> {

    void saveList(String objectId, List<AutoHistoryStepDatabase> list);

    void deleteByObjectId(String objectId);

    Map<String, AutoHistoryStepDatabase> selectByObjectId(String objectId);

}
