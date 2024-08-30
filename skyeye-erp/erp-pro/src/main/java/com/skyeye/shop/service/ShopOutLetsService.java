/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.shop.service;

import com.skyeye.business.service.SkyeyeErpOrderService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.other.entity.OtherOutLets;
import com.skyeye.shop.entity.ShopOutLets;

/**
 * @ClassName: ShopOutLetsService
 * @Description: 门店申领单管理服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2021/7/8 21:07
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface ShopOutLetsService extends SkyeyeErpOrderService<ShopOutLets> {

    void queryShopOutLetsTransById(InputObject inputObject, OutputObject outputObject);

    void insertShopOutLetsToTurnDepot(InputObject inputObject, OutputObject outputObject);

}
