/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.xxljob;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.skyeye.common.enumeration.WeekTypeEnum;
import com.skyeye.common.util.DateAfterSpacePointTime;
import com.skyeye.common.util.DateUtil;
import com.skyeye.common.util.ToolUtil;
import com.skyeye.checkwork.service.CheckWorkService;
import com.skyeye.eve.service.IScheduleDayService;
import com.skyeye.leave.service.LeaveService;
import com.skyeye.worktime.classenum.CheckWorkTimeWeekType;
import com.skyeye.worktime.entity.CheckWorkTime;
import com.skyeye.worktime.entity.CheckWorkTimeWeek;
import com.skyeye.worktime.service.CheckWorkTimeService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CheckWorkQuartz
 * @Description: 定时器填充打卡信息, 每天凌晨一点执行一次
 * @author: skyeye云系列--卫志强
 * @date: 2021/4/25 21:15
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Component
public class CheckWorkQuartz {

    private static Logger log = LoggerFactory.getLogger(CheckWorkQuartz.class);

    @Autowired
    private CheckWorkService checkWorkService;

    @Autowired
    private CheckWorkTimeService checkWorkTimeService;

    @Autowired
    private LeaveService checkWorkLeaveService;

    @Autowired
    private IScheduleDayService iScheduleDayService;

    /**
     * 定时器填充打卡信息,每天凌晨一点执行一次
     */
    @XxlJob("checkWorkQuartz")
    public void editCheckWorkMation() {
        log.info("填充打卡信息定时任务执行");
        try {
            // 1.获取所有的考勤班次信息
            List<CheckWorkTime> workTime = checkWorkTimeService.queryAllData();
            // 得到昨天的时间
            String yesterdayTime = DateAfterSpacePointTime.getSpecifiedTime(
                DateAfterSpacePointTime.ONE_DAY.getType(), DateUtil.getTimeAndToString(), DateUtil.YYYY_MM_DD, DateAfterSpacePointTime.AroundType.BEFORE);
            if (workTime != null && !workTime.isEmpty() && !iScheduleDayService.judgeISHoliday(yesterdayTime)) {
                // 班次信息不为空，并且昨天不是节假日
                log.info("Fill in the clocking information for timing task execution time is {}", yesterdayTime);
                // 判断昨天的日期是周几
                int weekDay = DateUtil.getWeek(yesterdayTime);
                // 判断昨天的日期是单周还是双周
                int weekType = DateUtil.getWeekType(yesterdayTime);
                // 2.获取昨天应该打卡的班次信息
                List<CheckWorkTime> shouldCheckTime = getShouldCheckTime(weekDay, weekType, workTime);
                if (!shouldCheckTime.isEmpty()) {
                    shouldCheckTime.forEach(bean -> {
                        try {
                            // 3.1 处理所有昨天只打早卡没有打晚卡的记录id
                            handleNotCheckWorkEndMember(yesterdayTime, bean.getId());
                            // 3.2 处理所有昨天没有打卡的用户
                            handleNotCheckWorkMember(yesterdayTime, bean.getId());
                        } catch (Exception e) {
                            log.info("Handling abnormal attendance information, message is {}.", e);
                        }
                    });
                }
            }
            // 4 处理所有昨天加班只打早卡没有打晚卡的记录id
            handleNotCheckWorkEndMember(yesterdayTime, "-");
        } catch (Exception e) {
            log.warn("CheckWorkQuartz error.", e);
        }
        log.info("填充打卡信息定时任务 end");
    }

    /**
     * 获取昨天应该打卡的班次信息
     *
     * @param weekDay  周几
     * @param weekType 1是双周，0是单周
     * @param workTime 考勤班次
     * @return
     */
    private List<CheckWorkTime> getShouldCheckTime(int weekDay, int weekType, List<CheckWorkTime> workTime) {
        List<CheckWorkTime> shouldCheckTime = new ArrayList<>();
        for (CheckWorkTime bean : workTime) {
            // 该班次中上班的天数
            List<CheckWorkTimeWeek> days = bean.getCheckWorkTimeWeekList();
            CheckWorkTimeWeek simpleDay = days.stream().filter(item -> item.getWeekNumber() == weekDay).findFirst().orElse(null);
            if (ObjectUtil.isNotEmpty(simpleDay)) {
                // 在该班次中找到了指定日期的上班时间
                if (weekType == WeekTypeEnum.ODD_WEEKS.getKey() && simpleDay.getType() == CheckWorkTimeWeekType.SINGLE_DAY.getKey()) {
                    // 单周
                    shouldCheckTime.add(bean);
                } else if (simpleDay.getType() == CheckWorkTimeWeekType.DAY.getKey()) {
                    shouldCheckTime.add(bean);
                }
            }
        }
        log.info("shouldCheckTime is {}", JSONUtil.toJsonStr(shouldCheckTime));
        return shouldCheckTime;
    }

    /**
     * 处理所有昨天只打早卡没有打晚卡的记录id
     *
     * @param yesterdayTime
     * @param timeId
     */
    private void handleNotCheckWorkEndMember(String yesterdayTime, String timeId) {
        List<Map<String, Object>> beans = checkWorkService.queryNotCheckEndWorkId(timeId, yesterdayTime);
        if (!beans.isEmpty()) {
            for (Map<String, Object> b : beans) {
                Map<String, Object> checkWorkMation = new HashMap<>();
                checkWorkMation.put("id", b.get("id").toString());
                checkWorkMation.put("state", "5");
                checkWorkMation.put("clockOutState", "3");
                checkWorkMation.put("workHours", "0:0:0");
                // 填充打晚卡信息
                checkWorkService.editCheckWorkBySystem(checkWorkMation);
            }
        }
    }

    /**
     * 处理所有昨天没有打卡的用户
     *
     * @param yesterdayTime 昨天的日期,格式为：yyyy-MM-dd
     * @param timeId        班次id
     */
    private void handleNotCheckWorkMember(String yesterdayTime, String timeId) {
        // 获取所有昨天没有打卡的用户
        List<Map<String, Object>> beans = checkWorkService.queryNotCheckMember(timeId, yesterdayTime);
        if (CollectionUtil.isEmpty(beans)) {
            return;
        }
        List<Map<String, Object>> listBeans = new ArrayList<>();
        for (Map<String, Object> b : beans) {
            String createId = b.get("createId").toString();
            // 判断昨天是否有请假记录,如果有，则不填充这条记录
            Map<String, Object> leaveMation = checkWorkLeaveService.queryCheckWorkLeaveByMation(timeId, createId, yesterdayTime);
            if (CollectionUtil.isEmpty(leaveMation)) {
                // 找不到该员工这个班次在这一天的请假记录，则记为旷工
                listBeans.add(getNoCheckWorkObject(timeId, createId, yesterdayTime));
            }
        }
        if (CollectionUtil.isNotEmpty(listBeans)) {
            checkWorkService.insertCheckWorkBySystem(listBeans);
        }
    }

    private Map<String, Object> getNoCheckWorkObject(String timeId, String createId, String yesterdayTime) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", ToolUtil.getSurFaceId());
        item.put("createId", createId);
        item.put("checkDate", yesterdayTime);
        item.put("state", "2");
        item.put("clockInState", "3");
        item.put("clockOutState", "3");
        item.put("timeId", timeId);
        item.put("workHours", "0:0:0");
        return item;
    }

}
