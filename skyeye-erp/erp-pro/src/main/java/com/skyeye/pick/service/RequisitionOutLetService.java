/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.pick.service;

import com.skyeye.business.service.SkyeyeErpOrderService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.pick.entity.RequisitionOutLet;

/**
 * @ClassName: RequisitionOutLetService
 * @Description: 领料出库单服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2024/6/26 20:35
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface RequisitionOutLetService extends SkyeyeErpOrderService<RequisitionOutLet> {

    void queryRequisitionOutLetsTransById(InputObject inputObject, OutputObject outputObject);

    void insertRequisitionOutLetsToTurnDepot(InputObject inputObject, OutputObject outputObject);
}
