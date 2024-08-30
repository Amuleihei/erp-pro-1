/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.holder.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.holder.entity.HolderNorms;

import java.util.List;

/**
 * @ClassName: HolderNormsService
 * @Description: 关联的客户/供应商/会员购买或者出售的商品信息服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2023/9/2 21:25
 * @Copyright: 2023 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目
 */
public interface HolderNormsService extends SkyeyeBusinessService<HolderNorms> {

    void queryHolderMaterialListByHolder(InputObject inputObject, OutputObject outputObject);

    List<String> queryHolderMaterialIdListByHolderId(String holderId);

    void queryHolderMaterialNormsListByHolder(InputObject inputObject, OutputObject outputObject);

    void queryHolderMaterialNormsCodeListByHolder(InputObject inputObject, OutputObject outputObject);
}
