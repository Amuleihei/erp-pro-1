/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye-report
 ******************************************************************************/

package com.skyeye.demand.dao;

import com.skyeye.demand.entity.AutoDemand;
import com.skyeye.demand.entity.AutoDemandQueryDo;
import com.skyeye.eve.dao.SkyeyeBaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AutoDemandDao
 * @Description: 需求类交互层
 * @author: skyeye云系列--卫志强
 * @date: 2021/5/16 23:19
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye-report Inc. All rights reserved.
 * 注意：本内容具体规则请参照readme执行，地址：https://gitee.com/doc_wei01/skyeye-report/blob/master/README.md
 */
public interface AutoDemandDao extends SkyeyeBaseMapper<AutoDemand> {

    List<Map<String, Object>> queryAutoDemandList(AutoDemandQueryDo pageInfo);
}
