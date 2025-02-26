/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.forum.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.util.DataCommonUtil;
import com.skyeye.common.util.ToolUtil;
import com.skyeye.constans.ForumConstants;
import com.skyeye.eve.forum.dao.ForumTagDao;
import com.skyeye.eve.forum.service.ForumTagService;
import com.skyeye.jedis.JedisClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ForumTagServiceImpl
 * @Description: 论坛标签管理服务类
 * @author: skyeye云系列--卫志强
 * @date: 2021/7/6 22:52
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
public class ForumTagServiceImpl implements ForumTagService {

    @Autowired
    private ForumTagDao forumTagDao;

    @Autowired
    private JedisClientService jedisClient;

    /**
     * 查出所有论坛标签列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryForumTagList(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Page pages = PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString()));
        List<Map<String, Object>> beans = forumTagDao.queryForumTagList(map);
        outputObject.setBeans(beans);
        outputObject.settotal(pages.getTotal());
    }

    /**
     * 新增论坛标签
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertForumTagMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.queryForumTagMationByName(map);
        if (!CollectionUtils.isEmpty(bean)) {
            outputObject.setreturnMessage("该论坛标签名称已存在，请更换");
        } else {
            Map<String, Object> itemCount = forumTagDao.queryForumTagBySimpleLevel(map);
            int thisOrderBy = Integer.parseInt(itemCount.get("simpleNum").toString()) + 1;
            map.put("orderBy", thisOrderBy);
            // 默认新建
            map.put("state", "1");
            DataCommonUtil.setCommonData(map, inputObject.getLogParams().get("id").toString());
            forumTagDao.insertForumTagMation(map);
        }
    }

    /**
     * 删除论坛标签
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteForumTagById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.queryForumTagStateById(map);
        if ("1".equals(bean.get("state").toString()) || "3".equals(bean.get("state").toString())) {
            // 新建或者下线可以删除
            forumTagDao.deleteForumTagById(map);
        } else {
            outputObject.setreturnMessage("该数据状态已改变，请刷新页面！");
        }
    }

    /**
     * 上线论坛标签
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateUpForumTagById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.queryForumTagStateById(map);
        if ("1".equals(bean.get("state").toString()) || "3".equals(bean.get("state").toString())) {
            // 新建或者下线可以上线
            forumTagDao.updateUpForumTagById(map);
            // 删除上线论坛标签的redis
            jedisClient.del(ForumConstants.FORUM_TAG_UP_STATE_LIST);
        } else {
            outputObject.setreturnMessage("该数据状态已改变，请刷新页面！");
        }
    }

    /**
     * 下线论坛标签
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateDownForumTagById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.queryForumTagStateById(map);
        if ("2".equals(bean.get("state").toString())) {
            // 上线状态可以下线
            forumTagDao.updateDownForumTagById(map);
            // 删除上线论坛标签的redis
            jedisClient.del(ForumConstants.FORUM_TAG_UP_STATE_LIST);
        } else {
            outputObject.setreturnMessage("该数据状态已改变，请刷新页面！");
        }
    }

    /**
     * 通过id查找对应的论坛标签信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void selectForumTagById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.selectForumTagById(map);
        outputObject.setBean(bean);
        outputObject.settotal(1);
    }

    /**
     * 通过id编辑对应的论坛标签信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editForumTagMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = forumTagDao.queryForumTagStateById(map);
        if ("1".equals(bean.get("state").toString()) || "3".equals(bean.get("state").toString())) {
            // 新建或者下线可以编辑
            Map<String, Object> b = forumTagDao.queryForumTagMationByName(map);
            if (b != null && !b.isEmpty()) {
                outputObject.setreturnMessage("该论坛标签名称已存在，请更换");
            } else {
                forumTagDao.editForumTagMationById(map);
            }
        } else {
            outputObject.setreturnMessage("该数据状态已改变，请刷新页面！");
        }
    }

    /**
     * 论坛标签上移
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editForumTagMationOrderNumUpById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        // 获取当前数据的同级分类下的上一条数据
        Map<String, Object> bean = forumTagDao.queryForumTagUpMationById(map);
        if (CollectionUtils.isEmpty(bean)) {
            outputObject.setreturnMessage("当前标签已经是首位，无须进行上移。");
        } else {
            // 进行位置交换
            map.put("upOrderBy", bean.get("prevOrderBy"));
            bean.put("upOrderBy", bean.get("thisOrderBy"));
            forumTagDao.editForumTagMationOrderNumUpById(map);
            forumTagDao.editForumTagMationOrderNumUpById(bean);
            // 删除上线论坛标签的redis
            jedisClient.del(ForumConstants.FORUM_TAG_UP_STATE_LIST);
        }
    }

    /**
     * 论坛标签下移
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editForumTagMationOrderNumDownById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        // 获取当前数据的同级分类下的下一条数据
        Map<String, Object> bean = forumTagDao.queryForumTagDownMationById(map);
        if (CollectionUtils.isEmpty(bean)) {
            outputObject.setreturnMessage("当前标签已经是末位，无须进行下移。");
        } else {
            // 进行位置交换
            map.put("upOrderBy", bean.get("prevOrderBy"));
            bean.put("upOrderBy", bean.get("thisOrderBy"));
            forumTagDao.editForumTagMationOrderNumUpById(map);
            forumTagDao.editForumTagMationOrderNumUpById(bean);
            // 删除上线论坛标签的redis
            jedisClient.del(ForumConstants.FORUM_TAG_UP_STATE_LIST);
        }
    }

    /**
     * 获取已经上线的论坛标签列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryForumTagUpStateList(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        List<Map<String, Object>> beans;
        if (ToolUtil.isBlank(jedisClient.get(ForumConstants.FORUM_TAG_UP_STATE_LIST))) {
            beans = forumTagDao.queryForumTagUpStateList(map);
            jedisClient.set(ForumConstants.FORUM_TAG_UP_STATE_LIST, JSONUtil.toJsonStr(beans));
        } else {
            beans = JSONUtil.toList(jedisClient.get(ForumConstants.FORUM_TAG_UP_STATE_LIST), null);
        }
        if (!beans.isEmpty()) {
            outputObject.setBeans(beans);
            outputObject.settotal(beans.size());
        }
    }

}
