/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.leave.service;

import com.skyeye.base.business.service.SkyeyeLinkDataService;
import com.skyeye.leave.entity.LeaveTimeSlot;

/**
 * @ClassName: LeaveTimeSlotService
 * @Description: 请假申请请假时间段服务接口层
 * @author: skyeye云系列--卫志强
 * @date: 2023/4/5 8:40
 * @Copyright: 2023 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface LeaveTimeSlotService extends SkyeyeLinkDataService<LeaveTimeSlot> {

    void editStateById(String id, String state, Integer useYearHoliday);

}
