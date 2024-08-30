/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.eve.entity.ActGroupUser;

import java.util.List;

/**
 * @ClassName: ActGroupService
 * @Description: 用户组关联用户管理服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/4/12 14:12
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface ActGroupUserService extends SkyeyeBusinessService<ActGroupUser> {

    void insertActGroupUser(InputObject inputObject, OutputObject outputObject);

    void deleteAllActGroupUserByGroupId(InputObject inputObject, OutputObject outputObject);

    List<ActGroupUser> queryAllActGroupUser();

    List<ActGroupUser> queryActGroupUser(List<String> groupIds);

}
