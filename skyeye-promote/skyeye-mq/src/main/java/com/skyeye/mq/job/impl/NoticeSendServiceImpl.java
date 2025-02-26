/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/
package com.skyeye.mq.job.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.skyeye.common.constans.MqConstants;
import com.skyeye.common.util.MailUtil;
import com.skyeye.service.JobMateMationService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author 卫志强
 * @ClassName: NoticeSendServiceImpl
 * @Description: 消息通知
 * @date 2020年8月22日
 */
@Component
@RocketMQMessageListener(
    topic = "${topic.notice-send-service}",
    consumerGroup = "${topic.notice-send-service}",
    selectorExpression = "${spring.profiles.active}")
public class NoticeSendServiceImpl implements RocketMQListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSendServiceImpl.class);

    @Autowired
    private JobMateMationService jobMateMationService;

    @Override
    public void onMessage(String data) {
        Map<String, Object> map = JSONUtil.toBean(data, null);
        String jobId = map.get("jobMateId").toString();
        try {
            // 任务开始
            jobMateMationService.comMQJobMation(jobId, MqConstants.JOB_TYPE_IS_PROCESSING, "");
            String email = map.get("email").toString();
            List<Map<String, Object>> beans = JSONUtil.toList(email, null);
            for (int i = 0; i < beans.size(); i++) {
                Map<String, Object> bean = beans.get(i);
                if (CollectionUtil.isNotEmpty(bean)) {
                    // 邮件账号不为空，发送邮件
                    new MailUtil().send(bean.get("email").toString(), map.get("title").toString(), map.get("content").toString());
                }
            }
            // 任务完成
            jobMateMationService.comMQJobMation(jobId, MqConstants.JOB_TYPE_IS_SUCCESS, "");
        } catch (Exception e) {
            LOGGER.warn("notification failed, reason is {}.", e);
            // 任务失败
            jobMateMationService.comMQJobMation(jobId, MqConstants.JOB_TYPE_IS_FAIL, "");
        }
    }

}
