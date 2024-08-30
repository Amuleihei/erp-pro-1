/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.dao;

import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.eve.entity.UserMessage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserMessageDao
 * @Description: 用户消息管理数据层
 * @author: skyeye云系列--卫志强
 * @date: 2021/8/7 19:12
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface UserMessageDao extends SkyeyeBaseMapper<UserMessage> {

    List<Map<String, Object>> queryUserMessageList(CommonPageInfo commonPageInfo);

}
