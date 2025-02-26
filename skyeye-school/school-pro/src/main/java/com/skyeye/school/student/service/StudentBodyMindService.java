/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.school.student.service;

import com.skyeye.base.business.service.SkyeyeBusinessService;
import com.skyeye.school.student.entity.StudentBodyMind;
import java.util.List;

/**
 * @ClassName: SchoolBodyMindService
 * @Description: 身心障碍管理服务接口层
 * @author: xqz
 * @date: 2023/8/9 9:52
 * @Copyright: 2023 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public interface StudentBodyMindService extends SkyeyeBusinessService<StudentBodyMind> {

    void saveLinkList(String studentId, List<StudentBodyMind> beans);

    void deleteLinkListByStudentId(String studentId);

    List<StudentBodyMind> queryLinkListByStudentId(String studentId);

}
