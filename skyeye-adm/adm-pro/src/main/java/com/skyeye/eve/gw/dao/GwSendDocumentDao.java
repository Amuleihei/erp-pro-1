/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.gw.dao;

import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.eve.dao.SkyeyeBaseMapper;
import com.skyeye.eve.gw.entity.GwSendDocument;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: GwSendDocumentDao
 * @Description: 公文发文管理数据接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/4/26 15:42
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface GwSendDocumentDao extends SkyeyeBaseMapper<GwSendDocument> {

    List<Map<String, Object>> queryGwSendDocumentList(CommonPageInfo pageInfo);

}
