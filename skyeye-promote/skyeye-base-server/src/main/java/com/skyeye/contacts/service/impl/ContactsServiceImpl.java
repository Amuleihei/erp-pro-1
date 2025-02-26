/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.contacts.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.skyeye.annotation.service.SkyeyeService;
import com.skyeye.base.business.service.impl.SkyeyeTeamAuthServiceImpl;
import com.skyeye.common.constans.CommonNumConstants;
import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.common.enumeration.DeleteFlagEnum;
import com.skyeye.common.enumeration.IsDefaultEnum;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.util.mybatisplus.MybatisPlusUtil;
import com.skyeye.contacts.classenum.ContactsAuthEnum;
import com.skyeye.contacts.dao.ContactsDao;
import com.skyeye.contacts.entity.Contacts;
import com.skyeye.contacts.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ContactsServiceImpl
 * @Description: 联系人管理
 * @author: skyeye云系列--卫志强
 * @date: 2021/7/6 22:18
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
@SkyeyeService(name = "联系人管理", groupName = "基础模块", teamAuth = true)
public class ContactsServiceImpl extends SkyeyeTeamAuthServiceImpl<ContactsDao, Contacts> implements ContactsService {

    @Autowired
    private ContactsDao contactsDao;

    @Override
    public Class getAuthEnumClass() {
        return ContactsAuthEnum.class;
    }

    @Override
    public List<String> getAuthPermissionKeyList() {
        return Arrays.asList(ContactsAuthEnum.ADD.getKey(), ContactsAuthEnum.EDIT.getKey(), ContactsAuthEnum.DELETE.getKey());
    }

    @Override
    public List<Map<String, Object>> queryPageDataList(InputObject inputObject) {
        CommonPageInfo pageInfo = inputObject.getParams(CommonPageInfo.class);
        pageInfo.setDeleteFlag(DeleteFlagEnum.NOT_DELETE.getKey());
        List<Map<String, Object>> beans = contactsDao.queryContactsList(pageInfo);
        return beans;
    }

    @Override
    public void writePostpose(Contacts entity, String userId) {
        super.writePostpose(entity, userId);
        if (entity.getIsDefault().equals(IsDefaultEnum.IS_DEFAULT.getKey())) {
            // 如果设置为默认联系人，则修改之前的联系人为非默认
            contactsDao.setContactsIsNotDefault(entity.getObjectId(), IsDefaultEnum.NOT_DEFAULT.getKey());
            contactsDao.setContactsIsDefault(entity.getId(), IsDefaultEnum.IS_DEFAULT.getKey());
        }
    }

    /**
     * 根据业务数据id获取联系人列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryContactsListByObject(InputObject inputObject, OutputObject outputObject) {
        String objectId = inputObject.getParams().get("objectId").toString();
        QueryWrapper<Contacts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(Contacts::getObjectId), objectId);
        queryWrapper.eq(MybatisPlusUtil.toColumns(Contacts::getDeleteFlag), DeleteFlagEnum.NOT_DELETE.getKey());
        List<Contacts> contactsList = list(queryWrapper);
        outputObject.setBeans(contactsList);
        outputObject.settotal(contactsList.size());
    }

    @Override
    public void queryContactsListByObjectIds(InputObject inputObject, OutputObject outputObject) {
        String objectIdsStr = inputObject.getParams().get("objectIds").toString();
        List<String> objectIds = JSONUtil.toList(objectIdsStr, null);
        if (CollectionUtil.isEmpty(objectIds)) {
            return;
        }
        objectIds = objectIds.stream().distinct().collect(Collectors.toList());
        QueryWrapper<Contacts> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(MybatisPlusUtil.toColumns(Contacts::getObjectId), objectIds);
        queryWrapper.eq(MybatisPlusUtil.toColumns(Contacts::getDeleteFlag), DeleteFlagEnum.NOT_DELETE.getKey());
        List<Contacts> contactsList = list(queryWrapper);
        Map<String, List<Contacts>> result = contactsList.stream().collect(Collectors.groupingBy(Contacts::getObjectId));
        outputObject.setBean(result);
        outputObject.settotal(CommonNumConstants.NUM_ONE);
    }
}
