/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.usercase.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.usercase.entity.AutoStepCase;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AutoStepCaseService
 * @Description: 用例步骤关联的用例管理服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2021/5/16 23:21
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye-report Inc. All rights reserved.
 * 注意：本内容具体规则请参照readme执行，地址：https://gitee.com/doc_wei01/skyeye-report/blob/master/README.md
 */
public interface AutoStepCaseService extends SkyeyeBusinessService<AutoStepCase> {
    void saveList(String objectId, List<AutoStepCase> autoStepCaseList);

    void deleteByObjectId(String objectId);

    Map<String, AutoStepCase> selectByObjectId(String objectId);
}
