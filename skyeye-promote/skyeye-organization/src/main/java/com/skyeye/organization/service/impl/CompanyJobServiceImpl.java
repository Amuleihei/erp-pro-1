/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.organization.service.impl;

import com.skyeye.common.constans.CommonNumConstants;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.util.DataCommonUtil;
import com.skyeye.common.util.ToolUtil;
import com.skyeye.organization.dao.CompanyJobDao;
import com.skyeye.organization.dao.CompanyJobScoreDao;
import com.skyeye.organization.service.CompanyJobService;
import com.skyeye.organization.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: CompanyJobServiceImpl
 * @Description: 公司部门职位信息管理服务类
 * @author: skyeye云系列--卫志强
 * @date: 2021/7/6 22:57
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
public class CompanyJobServiceImpl implements CompanyJobService {

    @Autowired
    private CompanyJobDao companyJobDao;

    @Autowired
    private CompanyJobScoreDao companyJobScoreDao;

    @Autowired
    private ICompanyService iCompanyService;

    /**
     * 获取公司部门职位信息列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryCompanyJobList(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        List<Map<String, Object>> beans = companyJobDao.queryCompanyJobList(map);
        iCompanyService.setName(beans, "companyId", "companyName");
        String[] s;
        for (Map<String, Object> bean : beans) {
            s = bean.get("pId").toString().split(",");
            if (s.length > 0) {
                bean.put("pId", s[s.length - 1]);
            } else {
                bean.put("pId", "0");
            }
        }
        outputObject.setBeans(beans);
        outputObject.settotal(beans.size());
    }

    /**
     * 添加公司部门职位信息信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertCompanyJobMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = companyJobDao.queryCompanyJobMationByName(map);
        if (bean == null) {
            DataCommonUtil.setCommonData(map, inputObject.getLogParams().get("id").toString());
            companyJobDao.insertCompanyJobMation(map);
        } else {
            outputObject.setreturnMessage("该公司部门职位名称已存在.");
        }
    }

    /**
     * 删除公司部门职位信息信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteCompanyJobMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        // 1.删除职位信息
        companyJobDao.deleteCompanyJobMationById(map);
        // 2.删除职位等级信息
        companyJobScoreDao.editCompanyJobScoreStateMationByJobId(map.get("id").toString(), CompanyJobScoreServiceImpl.State.START_DELETE.getState());
    }

    /**
     * 编辑公司部门职位信息信息时进行回显
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryCompanyJobMationToEditById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = companyJobDao.queryCompanyJobMationToEditById(map);
        outputObject.setBean(bean);
        outputObject.settotal(CommonNumConstants.NUM_ONE);
    }

    /**
     * 编辑公司部门职位信息信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editCompanyJobMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> bean = companyJobDao.queryCompanyJobMationByNameAndId(map);
        if (bean == null) {
            companyJobDao.editCompanyJobMationById(map);
        } else {
            outputObject.setreturnMessage("该公司部门职位名称已存在.");
        }
    }

    /**
     * 获取公司部门职位信息列表展示为树根据公司id
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryCompanyJobListTreeByDepartmentId(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        List<Map<String, Object>> beans = companyJobDao.queryCompanyJobListTreeByDepartmentId(map);
        String[] s;
        for (Map<String, Object> bean : beans) {
            s = bean.get("parentId").toString().split(",");
            bean.put("level", s.length);
            if (s.length > 0) {
                bean.put("parentId", s[s.length - 1]);
            } else {
                bean.put("parentId", "0");
            }
        }
        beans = ToolUtil.listToTree(beans, "id", "parentId", "children");
        if (!beans.isEmpty()) {
            ToolUtil.setLastIdentification(beans);
            outputObject.setBeans(beans);
            outputObject.settotal(beans.size());
        }
    }

    /**
     * 根据部门id获取职位列表展示为下拉选择框
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryCompanyJobListByToSelect(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        List<Map<String, Object>> beans = companyJobDao.queryCompanyJobListByToSelect(map);
        if (!beans.isEmpty()) {
            outputObject.setBeans(beans);
            outputObject.settotal(beans.size());
        }
    }

    /**
     * 根据部门id获取职位同级列表且不包含当前id的值展示为下拉选择框
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryCompanyJobSimpleListByToSelect(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        List<Map<String, Object>> beans = companyJobDao.queryCompanyJobSimpleListByToSelect(map);
        if (!beans.isEmpty()) {
            outputObject.setBeans(beans);
            outputObject.settotal(beans.size());
        }
    }

}
