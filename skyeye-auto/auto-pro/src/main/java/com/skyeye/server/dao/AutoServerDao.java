/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.server.dao;

import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.eve.dao.SkyeyeBaseMapper;
import com.skyeye.server.entity.AutoServer;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AutoServerDao
 * @Description: 服务器管理数据接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/3/26 9:00
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface AutoServerDao extends SkyeyeBaseMapper<AutoServer> {

    List<Map<String, Object>> queryAutoServerList(CommonPageInfo commonPageInfo);
}