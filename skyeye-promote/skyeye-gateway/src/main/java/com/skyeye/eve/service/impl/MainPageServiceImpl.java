/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.service.impl;

import com.skyeye.common.constans.ForumConstants;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.util.ToolUtil;
import com.skyeye.eve.dao.MainPageDao;
import com.skyeye.eve.service.MainPageService;
import com.skyeye.jedis.JedisClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainPageServiceImpl implements MainPageService {

    @Autowired
    private MainPageDao mainPageDao;

    @Autowired
    public JedisClientService jedisClient;

    /**
     * 获取本月考勤天数，我的文件数，我的论坛帖数
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryFourNumListByUserId(InputObject inputObject, OutputObject outputObject) {
        String userId = inputObject.getLogParams().get("id").toString();
        // 1.获取本月考勤天数
        String checkOnWorkNum = mainPageDao.queryCheckOnWorkNumByUserId(userId);
        // 2.获取我的文件数
        String diskCloudFileNum = mainPageDao.queryDiskCloudFileNumByUserId(userId);
        // 3.获取我的论坛帖数
        String forumNum = mainPageDao.queryForumNumByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("checkOnWorkNum", checkOnWorkNum);
        map.put("diskCloudFileNum", diskCloudFileNum);
        map.put("forumNum", forumNum);
        outputObject.setBean(map);
    }

    /**
     * 获取前八条热门论坛帖
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryHotForumList(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("userId", inputObject.getLogParams().get("id"));
        List<Map<String, Object>> beans = mainPageDao.queryHotForumList(map);
        for (Map<String, Object> m : beans) {
            String createTime = ToolUtil.timeFormat(m.get("createTime").toString());
            m.put("createTime", createTime);
            String key = ForumConstants.forumBrowseNumsByForumId(m.get("id").toString());
            if (ToolUtil.isBlank(jedisClient.get(key))) {
                // 浏览量
                m.put("browseNum", 0);
            } else {
                String browseNum = jedisClient.get(key);
                m.put("browseNum", browseNum);
            }
        }
        //按浏览量和评论数给集合排序
        beans.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                Integer m1num = Integer.parseInt(m1.get("browseNum").toString()) + Integer.parseInt(m1.get("commentNum").toString());
                Integer m2num = Integer.parseInt(m2.get("browseNum").toString()) + Integer.parseInt(m2.get("commentNum").toString());
                int flag = m1num.compareTo(m2num);
                return -flag; // 不取反，则按正序排列
            }
        });
        int count = beans.size();
        int pageMaxSize = 6;
        if (count < pageMaxSize) {
            pageMaxSize = count;
        }
        outputObject.setBeans(beans.subList(0, pageMaxSize));
        outputObject.settotal(beans.size());
    }

}
