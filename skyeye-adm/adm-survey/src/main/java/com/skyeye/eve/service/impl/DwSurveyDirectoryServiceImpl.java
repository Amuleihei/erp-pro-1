/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skyeye.common.constans.QuartzConstants;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.object.PutObject;
import com.skyeye.common.util.DataCommonUtil;
import com.skyeye.common.util.DateUtil;
import com.skyeye.common.util.IPSeeker;
import com.skyeye.common.util.ToolUtil;
import com.skyeye.common.util.question.CheckType;
import com.skyeye.common.util.question.QuType;
import com.skyeye.common.util.question.QuestionUtil;
import com.skyeye.eve.dao.DwSurveyDirectoryDao;
import com.skyeye.eve.rest.quartz.SysQuartzMation;
import com.skyeye.eve.service.DwSurveyDirectoryService;
import com.skyeye.eve.service.IQuartzService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DwSurveyDirectoryServiceImpl
 * @Description: 问卷功能服务类
 * @author: skyeye云系列--卫志强
 * @date: 2021/12/3 23:26
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
public class DwSurveyDirectoryServiceImpl implements DwSurveyDirectoryService {

    @Autowired
    private DwSurveyDirectoryDao dwSurveyDirectoryDao;

    @Autowired
    private IQuartzService iQuartzService;

    /**
     * 获取调查问卷列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryDwSurveyDirectoryList(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Page pages = PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString()));
        List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryDwSurveyDirectoryList(map);
        outputObject.setBeans(beans);
        outputObject.settotal(pages.getTotal());
    }

    /**
     * 新增调查问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertDwSurveyDirectoryMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("sId", ToolUtil.randomStr(6, 12));//用于短链接的ID
        map.put("dirType", 2);//2问卷
        map.put("surveyModel", 1);//问卷所属的问卷模块   1问卷模块
        map.put("surveyNote", "非常感谢您的参与！如有涉及个人信息，我们将严格保密。");
        DataCommonUtil.setCommonData(map, inputObject.getLogParams().get("id").toString());
        dwSurveyDirectoryDao.insertDwSurveyDirectoryMation(map);
    }

    /**
     * 获取调查问卷题目信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryDwSurveyDirectoryMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            List<Map<String, Object>> questions = dwSurveyDirectoryDao.queryQuestionListByBelongId(map);//获取问卷中的题
            List<Map<String, Object>> questionLeftList = new ArrayList<>();
            for (Map<String, Object> question : questions) {
                Map<String, Object> questionItem = new HashMap<>();
                questionItem.put("id", question.get("id"));
                questionItem.put("quTitle", question.get("quTitle"));
                questionItem.put("quType", question.get("quType"));
                questionLeftList.add(questionItem);
                getQuestionOptionListMation(question);
            }
            surveyMation.put("questionLeftList", questionLeftList);
            outputObject.setBean(surveyMation);
            outputObject.setBeans(questions);
            outputObject.settotal(1);
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

    /**
     * 获取问题项
     *
     * @param question
     * @return
     */
    public Map<String, Object> getQuestionOptionListMation(Map<String, Object> question) {
        String quType = QuType.getActionName(Integer.parseInt(question.get("quType").toString()));//获取题目类型
        if (quType.equals(QuType.RADIO.getActionName()) || quType.equals(QuType.COMPRADIO.getActionName())) {
            List<Map<String, Object>> questionRadio = dwSurveyDirectoryDao.queryQuestionRadioListByQuestionId(question);//获取多行填空题
            question.put("questionRadio", questionRadio);
        } else if (quType.equals(QuType.CHECKBOX.getActionName()) || quType.equals(QuType.COMPCHECKBOX.getActionName())) {
            List<Map<String, Object>> questionCheckBox = dwSurveyDirectoryDao.queryQuestionCheckBoxListByQuestionId(question);//获取多选题
            question.put("questionCheckBox", questionCheckBox);
        } else if (quType.equals(QuType.MULTIFILLBLANK.getActionName())) {
            List<Map<String, Object>> questionMultiFillBlank = dwSurveyDirectoryDao.queryQuestionMultiFillBlankListByQuestionId(question);//获取多行填空题
            question.put("questionMultiFillBlank", questionMultiFillBlank);
        } else if (quType.equals(QuType.BIGQU.getActionName())) {
            List<Map<String, Object>> childQuestions = dwSurveyDirectoryDao.queryChildQuestionListByBelongId(question);//获取问卷中的题
            for (Map<String, Object> item : childQuestions) {
                getQuestionOptionListMation(item);
            }
            question.put("option", childQuestions);
        } else if (quType.equals(QuType.CHENRADIO.getActionName()) || quType.equals(QuType.CHENCHECKBOX.getActionName()) || quType.equals(QuType.CHENSCORE.getActionName())
            || quType.equals(QuType.CHENFBK.getActionName()) || quType.equals(QuType.COMPCHENRADIO.getActionName())) {// 矩阵单选，矩阵多选，矩阵填空题，复合矩阵单选
            List<Map<String, Object>> questionChenRow = dwSurveyDirectoryDao.queryQuestionChenRowListByQuestionId(question);//获取行选项
            List<Map<String, Object>> questionChenColumn = dwSurveyDirectoryDao.queryQuestionChenColumnListByQuestionId(question);//获取列选项
            for (Map<String, Object> bean : questionChenRow) {
                for (Map<String, Object> item : questionChenColumn) {
                    item.put("rowId", bean.get("id"));
                }
                bean.put("questionChenColumn", questionChenColumn);
            }
            question.put("questionChenRow", questionChenRow);
            question.put("questionChenColumn", questionChenColumn);
            if (quType.equals(QuType.COMPCHENRADIO.getActionName())) {//如果是复合矩阵单选题， 则还有题选项
                List<Map<String, Object>> questionChenOption = dwSurveyDirectoryDao.queryQuestionChenOptionListByQuestionId(question);//获取选项
                question.put("questionChenOption", questionChenOption);
            }
        } else if (quType.equals(QuType.SCORE.getActionName())) {
            List<Map<String, Object>> questionScore = dwSurveyDirectoryDao.queryQuestionScoreListByQuestionId(question);//获取评分题
            question.put("quScores", questionScore);
        } else if (quType.equals(QuType.ORDERQU.getActionName())) {
            List<Map<String, Object>> questionOrderBy = dwSurveyDirectoryDao.queryQuestionOrderByListByQuestionId(question);//获取排序题
            question.put("questionOrderBy", questionOrderBy);
        }
        List<Map<String, Object>> questionLogic = dwSurveyDirectoryDao.queryQuestionLogicListByQuestionId(question);// 获取逻辑信息
        question.put("questionLogic", questionLogic);
        question.put("quTypeName", quType.toUpperCase());
        return question;
    }

    /**
     * 获取调查问卷信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryDwSurveyMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            outputObject.setBean(surveyMation);
            outputObject.settotal(1);
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

    /**
     * 编辑调查问卷信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editDwSurveyMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        dwSurveyDirectoryDao.editDwSurveyMationById(map);//编辑问卷信息
    }

    /**
     * 编辑题目信息
     * map-> belongId:试卷id
     * quTitle:问题标题
     * orderById:序号
     * tag:表示题目是试卷题还是题库中题
     * answerInputWidth:填空的input宽度
     * answerInputRow:填空的input行
     * contactsAttr:1关联到联系人属性  0不关联到联系人属性
     * contactsField:关联的联系人字段
     * checkType:说明的验证方式
     * hv:1水平显示 2垂直显示
     * randOrder:选项随机排列  1随机排列 0不随机排列
     * cellCount:按列显示时，列数
     * logic:逻辑设置json串
     * quId:问题id------非必填
     *
     * @return
     */
    public String compileQuestion(Map<String, Object> question) {
        String quId = "";
        //判断题目id是否为空，为空则新增，不为空则修改
        if (ToolUtil.isBlank(question.get("quId").toString())) {
            quId = ToolUtil.getSurFaceId();
            question.put("id", quId);
            question.put("quTag", 1);
            question.put("visibility", 1);
            question.put("createTime", DateUtil.getTimeAndToString());
            dwSurveyDirectoryDao.addQuestionMation(question);
        } else {
            quId = question.get("quId").toString();
            dwSurveyDirectoryDao.editQuestionMationById(question);
        }
        return quId;
    }

    /**
     * 设置问题逻辑
     *
     * @param quId
     * @param logicStr
     * @param userId
     * @return
     */
    public List<Map<String, Object>> setLogics(String quId, String logicStr, String userId) {
        List<Map<String, Object>> insertList = new ArrayList<>();
        List<Map<String, Object>> editList = new ArrayList<>();
        QuestionUtil.setLogics(quId, logicStr, userId, insertList, editList);
        if (!insertList.isEmpty()) {
            dwSurveyDirectoryDao.addQuestionLogicsMationList(insertList);
        }
        if (!editList.isEmpty()) {
            dwSurveyDirectoryDao.editQuestionLogicsMationList(editList);
        }
        insertList.addAll(editList);
        return insertList;
    }

    /**
     * 添加填空题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuFillblankMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.FILLBLANK.getIndex());
        String checkType = map.get("checkType").toString();
        if ("undefined".equals(checkType)) {
            map.put("checkType", "");
            checkType = "";
        }
        if (!ToolUtil.isBlank(checkType) && !ToolUtil.isNumeric(checkType)) {
            map.put("checkType", CheckType.valueOf(checkType).getIndex());
        }
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加评分题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuScoreMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.SCORE.getIndex());
        // 添加问题并返回问题id
        String quId = compileQuestion(map);

        // 获取模板绑定信息
        List<Map<String, Object>> score = JSONUtil.toList(map.get("scoreTd").toString(), null);
        if (score.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            String userId = inputObject.getLogParams().get("id").toString();
            QuestionUtil.setQuestionOptionMation(quId, score, insertList, editList, userId, -1);
            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionScoreMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionScoreMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quItems", insertList);
        }

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加排序题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuOrderquMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.ORDERQU.getIndex());
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        List<Map<String, Object>> orderqu = JSONUtil.toList(map.get("orderquTd").toString(), null);//获取模板绑定信息
        if (orderqu.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            String userId = inputObject.getLogParams().get("id").toString();
            QuestionUtil.setQuestionOptionMation(quId, orderqu, insertList, editList, userId, -1);

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionOrderquMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionOrderquMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quItems", insertList);
        }

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加分页标记
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuPagetagMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.PAGETAG.getIndex());
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加单选题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuRadioMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.RADIO.getIndex());
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        List<Map<String, Object>> radio = JSONUtil.toList(map.get("radioTd").toString(), null);//获取模板绑定信息
        if (CollectionUtils.isNotEmpty(radio)) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            String userId = inputObject.getLogParams().get("id").toString();
            QuestionUtil.setQuestionOptionMation(quId, radio, insertList, editList, userId, QuType.RADIO.getIndex());

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionRadioMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionRadioMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quItems", insertList);
        }

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加多选题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuCheckBoxMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.CHECKBOX.getIndex());
        // 添加问题并返回问题id
        String quId = compileQuestion(map);

        List<Map<String, Object>> checkbox = JSONUtil.toList(map.get("checkboxTd").toString(), null);//获取模板绑定信息
        if (checkbox.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            String userId = inputObject.getLogParams().get("id").toString();
            QuestionUtil.setQuestionOptionMation(quId, checkbox, insertList, editList, userId, QuType.CHECKBOX.getIndex());

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionCheckBoxMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionCheckBoxMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quItems", insertList);
        }

        // 设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加多选填空题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuMultiFillblankMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.MULTIFILLBLANK.getIndex());
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        List<Map<String, Object>> multiFillblank = JSONUtil.toList(map.get("multiFillblankTd").toString(), null);//获取模板绑定信息
        if (multiFillblank.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            String userId = inputObject.getLogParams().get("id").toString();
            QuestionUtil.setQuestionOptionMation(quId, multiFillblank, insertList, editList, userId, -1);

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionMultiFillblankMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionMultiFillblankMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quItems", insertList);
        }

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加段落题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuParagraphMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("quType", QuType.PARAGRAPH.getIndex());
        //添加问题并返回问题id
        String quId = compileQuestion(map);

        //设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), inputObject.getLogParams().get("id").toString()));
        outputObject.setBean(map);
    }

    /**
     * 添加矩阵单选题,矩阵多选题,矩阵评分题,矩阵填空题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addQuChenMation(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        int quType = QuType.getIndex(map.get("quType").toString());
        if (-1 == quType) {
            outputObject.setreturnMessage("参数值错误！");
            return;
        } else {
            map.put("quType", quType);
        }
        String userId = inputObject.getLogParams().get("id").toString();
        //添加问题并返回问题id
        String quId = compileQuestion(map);
        List<Map<String, Object>> column = JSONUtil.toList(map.get("column").toString(), null);//获取模板绑定信息
        if (column.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            QuestionUtil.setQuestionOptionMation(quId, column, insertList, editList, userId, -1);

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionColumnMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionColumnMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quColumnItems", insertList);
        }

        List<Map<String, Object>> row = JSONUtil.toList(map.get("row").toString(), null);//获取模板绑定信息
        if (row.size() > 0) {
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> editList = new ArrayList<>();
            QuestionUtil.setQuestionOptionMation(quId, row, insertList, editList, userId, -1);

            if (!insertList.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionRowMationList(insertList);
            }
            if (!editList.isEmpty()) {
                dwSurveyDirectoryDao.editQuestionRowMationList(editList);
            }
            insertList.addAll(editList);
            map.put("quRowItems", insertList);
        }

        // 设置问题逻辑
        map.put("quLogics", setLogics(quId, map.get("logic").toString(), userId));
        outputObject.setBean(map);
    }

    /**
     * 删除问题
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> question = dwSurveyDirectoryDao.queryQuestionMationById(map);
        if (question != null) {
            if (question.get("surveyState").toString().equals("0")) {//设计状态
                String tableName = QuType.getTableName(Integer.parseInt(question.get("quType").toString()));
                if (!ToolUtil.isBlank(tableName)) {
                    String[] questionId = QuType.getQuestionId(Integer.parseInt(question.get("quType").toString())).split(",");
                    String[] str = tableName.split(",");
                    for (int i = 0; i < str.length; i++) {
                        map.put("tableName", str[i]);
                        map.put("key", questionId[i]);
                        dwSurveyDirectoryDao.deleteQuestionOptionMationByQuId(map);
                    }
                }
                dwSurveyDirectoryDao.deleteQuestionMationById(map);//执行物理删除问题
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionMationById(map);//执行逻辑删除问题
            }
            dwSurveyDirectoryDao.updateQuestionOrderByIdByQuId(question);//修改该问题排序后面的序号减1
        }
    }

    /**
     * 删除矩阵单选题,矩阵多选题,矩阵评分题,矩阵填空题列选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionChenColumnMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionChenColumnById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionChenColumnMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionChenColumnMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除矩阵单选题,矩阵多选题,矩阵评分题,矩阵填空题行选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionChenRowMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionChenRowById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionChenRowMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionChenRowMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除单选题选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionRadioOptionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionRadioOptionById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionRadioOptionMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionRadioOptionMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除多选题选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionChedkBoxOptionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionChedkBoxOptionById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionChedkBoxOptionMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionChedkBoxOptionMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除评分题选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionScoreOptionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionScoreOptionById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionScoreOptionMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionScoreOptionMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除排序选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionOrderOptionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionOrderOptionById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionOrderOptionMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionOrderOptionMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 删除多项填空题选项
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteQuestionMultiFillblankOptionMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> option = dwSurveyDirectoryDao.queryQuestionMultiFillblankOptionById(map);
        if (option != null) {
            if (option.get("surveyState").toString().equals("0")) {//设计状态
                dwSurveyDirectoryDao.deleteQuestionMultiFillblankOptionMationById(map);//执行物理删除
            } else {//执行中或者结束
                dwSurveyDirectoryDao.deleteLogicQuestionMultiFillblankOptionMationById(map);//执行逻辑删除问题
            }
        }
    }

    /**
     * 发布问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void editSurveyStateToReleaseById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            if ("0".equals(surveyMation.get("surveyState").toString())) {//设计状态可以发布问卷
                List<Map<String, Object>> questions = dwSurveyDirectoryDao.queryQuestionListByBelongId(map);//获取问卷中的题
                if (!questions.isEmpty() && questions.size() > 0) {
                    map.put("startTime", DateUtil.getTimeAndToString());
                    map.put("questionNum", questions.size());
                    dwSurveyDirectoryDao.editSurveyStateToReleaseById(map);
                    if ("1".equals(surveyMation.get("ynEndTime").toString())) {
                        // 是否依据时间结束
                        this.startUpTaskQuartz(surveyMation.get("id").toString(), surveyMation.get("surveyName").toString(), surveyMation.get("endTime").toString());
                    }
                } else {
                    outputObject.setreturnMessage("该问卷没有调查项，无法发布问卷。");
                }
            } else {
                outputObject.setreturnMessage("该问卷已发布，请刷新数据。");
            }
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

    private void startUpTaskQuartz(String name, String title, String delayedTime) {
        SysQuartzMation sysQuartzMation = new SysQuartzMation();
        sysQuartzMation.setName(name);
        sysQuartzMation.setTitle(title);
        sysQuartzMation.setDelayedTime(delayedTime);
        sysQuartzMation.setGroupId(QuartzConstants.QuartzMateMationJobType.END_SURVEY_MATION.getTaskType());
        iQuartzService.startUpTaskQuartz(sysQuartzMation);
    }

    /**
     * 获取调查问卷题目信息用来生成html页面
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryDwSurveyDirectoryMationByIdToHTML(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            List<Map<String, Object>> questions = dwSurveyDirectoryDao.queryQuestionListByBelongId(map);//获取问卷中的题
            int pageNo = 1;
            for (Map<String, Object> question : questions) {
                String quType = QuType.getActionName(Integer.parseInt(question.get("quType").toString()));
                if (quType.equals(QuType.PAGETAG.getActionName())) {
                    pageNo++;
                }
            }
            for (Map<String, Object> question : questions) {
                question.put("pageNo", pageNo);
                getQuestionOptionListMation(question);
            }
            surveyMation.put("pageNo", pageNo);
            outputObject.setBean(surveyMation);
            outputObject.setBeans(questions);
            outputObject.settotal(1);
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

    /**
     * 删除问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void deleteSurveyMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        dwSurveyDirectoryDao.deleteSurveyMationById(map);//删除问卷
    }

    /**
     * 分析报告问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void querySurveyFxMationById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            List<Map<String, Object>> questions = dwSurveyDirectoryDao.queryQuestionListByBelongId(map);//获取问卷中的题
            for (Map<String, Object> question : questions) {
                question.put("quTypeName", QuType.getCName(Integer.parseInt(question.get("quType").toString())));
                getQuestionOptionListMation(question);
                getQuestionOptionReportListMation(question);
            }
            outputObject.setBean(surveyMation);
            outputObject.setBeans(questions);
            outputObject.settotal(1);
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

    /**
     * 统计获取数量
     *
     * @param question
     * @return
     */
    public Map<String, Object> getQuestionOptionReportListMation(Map<String, Object> question) {
        String quType = QuType.getActionName(Integer.parseInt(question.get("quType").toString()));//获取题目类型
        if (quType.equals(QuType.RADIO.getActionName()) || quType.equals(QuType.COMPRADIO.getActionName())) {//单选  复合单选
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryRadioGroupStat(question);
            List<Map<String, Object>> radios = (List<Map<String, Object>>) question.get("questionRadio");
            int count = 0;
            for (Map<String, Object> radio : radios) {
                radio.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quItemId").toString().equals(radio.get("id").toString())) {
                        radio.put("anCount", bean.get("count"));
                    }
                }
                count += Integer.parseInt(radio.get("anCount").toString());
            }
            for (Map<String, Object> radio : radios) {
                radio.put("anAllCount", count);
            }
        } else if (quType.equals(QuType.CHECKBOX.getActionName()) || quType.equals(QuType.COMPCHECKBOX.getActionName())) {//多选 复合多选
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryCheckBoxGroupStat(question);
            List<Map<String, Object>> checkBoxs = (List<Map<String, Object>>) question.get("questionCheckBox");
            int count = 0;
            for (Map<String, Object> checkBox : checkBoxs) {
                checkBox.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quItemId").toString().equals(checkBox.get("id").toString())) {
                        checkBox.put("anCount", bean.get("count"));
                    }
                }
                count += Integer.parseInt(checkBox.get("anCount").toString());
            }
            for (Map<String, Object> checkBox : checkBoxs) {
                checkBox.put("anAllCount", count);
            }
        } else if (quType.equals(QuType.FILLBLANK.getActionName())) {//填空题
            Map<String, Object> bean = dwSurveyDirectoryDao.queryFillBlankGroupStat(question);
            question.put("rowContent", bean.get("emptyCount"));
            question.put("optionContent", bean.get("blankCount"));
            question.put("anCount", bean.get("blankCount"));
        } else if (quType.equals(QuType.ANSWER.getActionName())) {//多行填空题
            Map<String, Object> bean = dwSurveyDirectoryDao.queryAnswerGroupStat(question);
            question.put("rowContent", bean.get("emptyCount"));
            question.put("optionContent", bean.get("blankCount"));
            question.put("anCount", bean.get("blankCount"));
        } else if (quType.equals(QuType.MULTIFILLBLANK.getActionName())) {//组合填空
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryMultiFillBlankGroupStat(question);
            List<Map<String, Object>> multiFillBlanks = (List<Map<String, Object>>) question.get("questionMultiFillBlank");
            int count = 0;
            for (Map<String, Object> multiFillBlank : multiFillBlanks) {
                multiFillBlank.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quItemId").toString().equals(multiFillBlank.get("id").toString())) {
                        multiFillBlank.put("anCount", bean.get("count"));
                    }
                }
                count += Integer.parseInt(multiFillBlank.get("anCount").toString());
            }
            for (Map<String, Object> multiFillBlank : multiFillBlanks) {
                multiFillBlank.put("anAllCount", count);
            }
        } else if (quType.equals(QuType.ENUMQU.getActionName())) {//枚举题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryEnumQuGroupStat(question);
            if (beans.isEmpty()) {
                question.put("anCount", 0);
            } else {
                question.put("anCount", beans.size());
            }
        } else if (quType.equals(QuType.CHENRADIO.getActionName())) {//矩阵单选题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryChenRadioGroupStat(question);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) question.get("questionChenRow");
            int count = 0;
            for (Map<String, Object> row : rows) {
                row.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quRowId").toString().equals(row.get("id").toString())) {
                        row.put("anCount", Integer.parseInt(row.get("anCount").toString()) + Integer.parseInt(bean.get("count").toString()));
                    }
                }
                count += Integer.parseInt(row.get("anCount").toString());
            }
            for (Map<String, Object> bean : beans) {
                bean.put("anAllCount", count);
                for (Map<String, Object> row : rows) {
                    if (row.get("id").toString().equals(bean.get("quRowId").toString())) {
                        bean.put("anCount", row.get("anCount").toString());
                    }
                }
            }
            question.put("anChenRadios", beans);
        } else if (quType.equals(QuType.CHENFBK.getActionName())) {//矩阵填空题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryChenFbkGroupStat(question);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) question.get("questionChenRow");
            int count = 0;
            for (Map<String, Object> row : rows) {
                row.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quRowId").toString().equals(row.get("id").toString())) {
                        row.put("anCount", Integer.parseInt(row.get("anCount").toString()) + Integer.parseInt(bean.get("count").toString()));
                    }
                }
                count += Integer.parseInt(row.get("anCount").toString());
            }
            for (Map<String, Object> bean : beans) {
                bean.put("anAllCount", count);
                for (Map<String, Object> row : rows) {
                    if (row.get("id").toString().equals(bean.get("quRowId").toString())) {
                        bean.put("anCount", row.get("anCount").toString());
                    }
                }
            }
            question.put("anChenFbks", beans);
        } else if (quType.equals(QuType.CHENCHECKBOX.getActionName())) {//矩阵多选题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryChenCheckBoxGroupStat(question);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) question.get("questionChenRow");
            int count = 0;
            for (Map<String, Object> row : rows) {
                row.put("anCount", 0);
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quRowId").toString().equals(row.get("id").toString())) {
                        row.put("anCount", Integer.parseInt(row.get("anCount").toString()) + Integer.parseInt(bean.get("count").toString()));
                    }
                }
                count += Integer.parseInt(row.get("anCount").toString());
            }
            for (Map<String, Object> bean : beans) {
                bean.put("anAllCount", count);
                for (Map<String, Object> row : rows) {
                    if (row.get("id").toString().equals(bean.get("quRowId").toString())) {
                        bean.put("anCount", row.get("anCount").toString());
                    }
                }
            }
            question.put("anChenCheckboxs", beans);
        } else if (quType.equals(QuType.CHENSCORE.getActionName())) {//矩阵评分题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryChenScoreGroupStat(question);
            question.put("anChenScores", beans);
        } else if (quType.equals(QuType.SCORE.getActionName())) {//评分题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryScoreGroupStat(question);
            List<Map<String, Object>> scores = (List<Map<String, Object>>) question.get("quScores");
            int count = 0;
            for (Map<String, Object> score : scores) {
                score.put("anCount", 0);
                score.put("avgScore", "0.00");
                for (Map<String, Object> bean : beans) {
                    if (bean.get("quRowId").toString().equals(score.get("id").toString())) {
                        score.put("anCount", bean.get("count"));
                        score.put("avgScore", Float.parseFloat(bean.get("avgScore").toString()));
                    }
                }
                count += Integer.parseInt(score.get("anCount").toString());
            }
            for (Map<String, Object> score : scores) {
                score.put("anAllCount", count);
            }
        } else if (quType.equals(QuType.ORDERQU.getActionName())) {//排序题
            List<Map<String, Object>> beans = dwSurveyDirectoryDao.queryOrderQuGroupStat(question);
            List<Map<String, Object>> orderQus = (List<Map<String, Object>>) question.get("questionOrderBy");
            for (Map<String, Object> bean : beans) {
                for (Map<String, Object> orderQu : orderQus) {
                    if (bean.get("quRowId").toString().equals(orderQu.get("id").toString())) {
                        orderQu.put("anOrderSum", bean.get("sumOrderNum"));
                    }
                }
            }
        }
        return question;
    }

    /**
     * 复制问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertSurveyMationCopyById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        map.put("sId", ToolUtil.randomStr(6, 12));//用于短链接的ID
        map.put("dirType", 2);//2问卷
        map.put("surveyModel", 1);//问卷所属的问卷模块   1问卷模块
        DataCommonUtil.setCommonData(map, inputObject.getLogParams().get("id").toString());
        // 复制问卷
        dwSurveyDirectoryDao.insertSurveyMationCopyById(map);
        List<Map<String, Object>> questions = dwSurveyDirectoryDao.queryQuestionMationCopyById(map);
        String surveyId = map.get("id").toString();
        for (Map<String, Object> question : questions) {
            question.put("copyFormId", question.get("id"));
            question.put("id", ToolUtil.getSurFaceId());
            question.put("createTime", DateUtil.getTimeAndToString());
            question.put("belongId", surveyId);
            copyQuestionOptionListMation(question);
        }
        dwSurveyDirectoryDao.addQuestionMationCopyBySurveyId(questions);
    }

    /**
     * 复制题目选项
     *
     * @param question
     */
    public void copyQuestionOptionListMation(Map<String, Object> question) {
        String quType = QuType.getActionName(Integer.parseInt(question.get("quType").toString()));//获取题目类型
        if (quType.equals(QuType.RADIO.getActionName()) || quType.equals(QuType.COMPRADIO.getActionName())) {
            List<Map<String, Object>> questionRadios = dwSurveyDirectoryDao.queryQuestionRadioListByCopyId(question);//获取多行填空题
            setBaseMation(question, questionRadios);
            if (!questionRadios.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionRadioMationCopyList(questionRadios);
            }
        } else if (quType.equals(QuType.CHECKBOX.getActionName()) || quType.equals(QuType.COMPCHECKBOX.getActionName())) {
            List<Map<String, Object>> questionCheckBoxs = dwSurveyDirectoryDao.queryQuestionCheckBoxListByCopyId(question);//获取多选题
            setBaseMation(question, questionCheckBoxs);
            if (!questionCheckBoxs.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionCheckBoxMationCopyList(questionCheckBoxs);
            }
        } else if (quType.equals(QuType.MULTIFILLBLANK.getActionName())) {
            List<Map<String, Object>> questionMultiFillBlanks = dwSurveyDirectoryDao.queryQuestionMultiFillBlankListByCopyId(question);//获取多行填空题
            setBaseMation(question, questionMultiFillBlanks);
            if (!questionMultiFillBlanks.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionMultiFillBlankMationCopyList(questionMultiFillBlanks);
            }
        } else if (quType.equals(QuType.BIGQU.getActionName())) {
        } else if (quType.equals(QuType.CHENRADIO.getActionName()) || quType.equals(QuType.CHENCHECKBOX.getActionName()) || quType.equals(QuType.CHENSCORE.getActionName())
            || quType.equals(QuType.CHENFBK.getActionName()) || quType.equals(QuType.COMPCHENRADIO.getActionName())) {// 矩阵单选，矩阵多选，矩阵填空题，复合矩阵单选
            List<Map<String, Object>> questionChenRows = dwSurveyDirectoryDao.queryQuestionChenRowListByCopyId(question);//获取行选项
            List<Map<String, Object>> questionChenColumns = dwSurveyDirectoryDao.queryQuestionChenColumnListByCopyId(question);//获取列选项
            setBaseMation(question, questionChenRows);
            if (!questionChenRows.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionChenRowMationCopyList(questionChenRows);
            }
            setBaseMation(question, questionChenColumns);
            if (!questionChenColumns.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionChenColumnMationCopyList(questionChenColumns);
            }
            if (quType.equals(QuType.COMPCHENRADIO.getActionName())) {//如果是复合矩阵单选题， 则还有题选项
                List<Map<String, Object>> questionChenOptions = dwSurveyDirectoryDao.queryQuestionChenOptionListByCopyId(question);//获取选项
                setBaseMation(question, questionChenOptions);
            }
        } else if (quType.equals(QuType.SCORE.getActionName())) {
            List<Map<String, Object>> questionScores = dwSurveyDirectoryDao.queryQuestionScoreListByCopyId(question);//获取评分题
            setBaseMation(question, questionScores);
            if (!questionScores.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionScoreMationCopyList(questionScores);
            }
        } else if (quType.equals(QuType.ORDERQU.getActionName())) {
            List<Map<String, Object>> questionOrderBys = dwSurveyDirectoryDao.queryQuestionOrderByListByCopyId(question);//获取排序题
            setBaseMation(question, questionOrderBys);
            if (!questionOrderBys.isEmpty()) {
                dwSurveyDirectoryDao.addQuestionOrderByMationCopyList(questionOrderBys);
            }
        }
    }

    private void setBaseMation(Map<String, Object> question, List<Map<String, Object>> list) {
        for (Map<String, Object> bean : list) {
            bean.put("id", ToolUtil.getSurFaceId());
            bean.put("createTime", DateUtil.getTimeAndToString());
            bean.put("quId", question.get("id"));
        }
    }

    /**
     * 判断该ip的用户是否回答过此问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryAnswerSurveyMationByIp(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        if (judgeWhetherExaming(map, PutObject.getRequest(), outputObject)) {
            String id = map.get("id").toString();
            // 获取问卷信息
            Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
            outputObject.setBean(surveyMation);
        }
    }

    /**
     * 用户回答问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void insertAnswerSurveyMationByIp(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        Map<String, Object> data = JSONUtil.toBean(map.get("jsonData").toString(), null);
        map.put("id", data.get("surveyId"));
        HttpServletRequest request = PutObject.getRequest();
        if (judgeWhetherExaming(map, request, outputObject)) {
            //问卷调查ip
            String ipAddr = ToolUtil.getIpByRequest(request);
            Map<String, Map<String, Object>> quMaps = buildSaveSurveyMap(request, data);
            //问卷答卷信息
            Map<String, Object> surveyAnswer = new HashMap<>();
            String addr = IPSeeker.getCountry(ipAddr);
            String city = IPSeeker.getCurCityByCountry(addr);
            surveyAnswer.put("ipAddr", ipAddr);
            surveyAnswer.put("addr", addr);
            surveyAnswer.put("city", city);
            surveyAnswer.put("id", data.get("surveyId"));
            surveyAnswer.put("dataSource", 0);
            surveyAnswer.put("bgAnDate", map.get("bgAnDate"));
            saveAnswer(surveyAnswer, quMaps);
        }
    }

    /**
     * 是否可以参加调研
     *
     * @param map
     * @param request
     * @param outputObject 出参以及提示信息的返回值对象
     * @return
     */
    public boolean judgeWhetherExaming(Map<String, Object> map, HttpServletRequest request, OutputObject outputObject) {
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        //问卷是否存在
        if (surveyMation != null && !surveyMation.isEmpty()) {
            if ("1".equals(surveyMation.get("surveyState").toString())) {
                //每台电脑或手机只能答一次
                if ("4".equals(surveyMation.get("effective").toString()) || "1".equals(surveyMation.get("effectiveIp").toString())) {
                    //获取答卷ip
                    map.put("ip", ToolUtil.getIpByRequest(request));
                    Map<String, Object> answerMation = dwSurveyDirectoryDao.querySurveyAnswerMationByIp(map);
                    if (answerMation != null && !answerMation.isEmpty()) {
                        outputObject.setreturnMessage("您已参与过该问卷，请休息一会儿。");
                        return false;
                    }
                } else {
                    //不做ip限制，则默认每五分钟才能答一次
                    List<Map<String, Object>> answerMation = dwSurveyDirectoryDao.querySurveyAnswerMationOverFiveMinByIp(map);
                    if (answerMation != null && !answerMation.isEmpty()) {
                        outputObject.setreturnMessage("您已参与过该问卷，请休息一会儿。");
                        return false;
                    }
                }
                //是否依据收到的份数结束
                if ("1".equals(surveyMation.get("ynEndNum").toString())) {
                    if (Integer.parseInt(surveyMation.get("answerNum").toString()) + 1 > Integer.parseInt(surveyMation.get("endNum").toString())) {
                        outputObject.setreturnMessage("问卷已结束。");
                        return false;
                    }
                }
                //是否依据时间结束
                if ("1".equals(surveyMation.get("ynEndTime").toString())) {
                    //当前时间和设置的结束时间作比较
                    if (DateUtil.compare(surveyMation.get("endTime").toString(), DateUtil.getTimeAndToString())) {
                        outputObject.setreturnMessage("问卷已结束。");
                        return false;
                    }
                }
            } else {
                outputObject.setreturnMessage("问卷已结束。");
                return false;
            }
        } else {
            outputObject.setreturnMessage("该问卷信息已不存在");
        }
        return true;
    }

    /**
     * 用户回答问卷时获取key-value值
     *
     * @param request
     * @param params
     * @return
     */
    public Map<String, Map<String, Object>> buildSaveSurveyMap(HttpServletRequest request, Map<String, Object> params) {
        Map<String, Map<String, Object>> quMaps = new HashMap<String, Map<String, Object>>();
        Map<String, Object> yesnoMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.YESNO.getIndex() + "_");// 是非
        quMaps.put("yesnoMaps", yesnoMaps);
        Map<String, Object> radioMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.RADIO.getIndex() + "_");// 单选
        Map<String, Object> checkboxMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.CHECKBOX.getIndex() + "_");// 多选
        Map<String, Object> fillblankMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.FILLBLANK.getIndex() + "_");// 填空
        quMaps.put("fillblankMaps", fillblankMaps);
        //dfillValue；quItemId问题选项id；otherText其他文本信息
        String dfillValue, quItemId, otherText;
        //map中间参数；anCheckbox选项对象；mapRow矩阵题中间对象
        Map<String, Object> map, anCheckbox, mapRow;
        Map<String, Object> dfillblankMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.MULTIFILLBLANK.getIndex() + "_");// 多项填空
        for (String key : dfillblankMaps.keySet()) {
            dfillValue = dfillblankMaps.get(key).toString();
            map = WebUtils.getParametersStartingWith(request, dfillValue);
            dfillblankMaps.put(key, map);
        }
        quMaps.put("multifillblankMaps", dfillblankMaps);
        Map<String, Object> answerMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.ANSWER.getIndex() + "_");// 多行填空
        quMaps.put("answerMaps", answerMaps);
        Map<String, Object> compRadioMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.COMPRADIO.getIndex() + "_");// 复合单选
        Map<String, Object> anRadio;
        for (String key : compRadioMaps.keySet()) {
            quItemId = compRadioMaps.get(key).toString();
            otherText = params.get("text_qu_" + QuType.COMPRADIO.getIndex() + "_" + key + "_" + quItemId).toString();
            anRadio = new HashMap<>();
            anRadio.put("quId", key);
            anRadio.put("quItemId", quItemId);
            anRadio.put("otherText", otherText);
            compRadioMaps.put(key, anRadio);
        }
        quMaps.put("compRadioMaps", compRadioMaps);
        Map<String, Object> compCheckboxMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.COMPCHECKBOX.getIndex() + "_");// 复合多选

        for (String key : compCheckboxMaps.keySet()) {
            dfillValue = compCheckboxMaps.get(key).toString();
            map = WebUtils.getParametersStartingWith(request, "tag_" + dfillValue);
            for (String key2 : map.keySet()) {
                quItemId = map.get(key2).toString();
                otherText = params.get("text_" + dfillValue + quItemId).toString();
                anCheckbox = new HashMap<>();
                anCheckbox.put("quItemId", quItemId);
                anCheckbox.put("otherText", otherText);
                map.put(key2, anCheckbox);
            }
            compCheckboxMaps.put(key, map);
        }
        quMaps.put("compCheckboxMaps", compCheckboxMaps);
        Map<String, Object> enumMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.ENUMQU.getIndex() + "_");// 枚举
        quMaps.put("enumMaps", enumMaps);
        Map<String, Object> scoreMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.SCORE.getIndex() + "_");// 分数
        for (String key : scoreMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, scoreMaps.get(key).toString());
            scoreMaps.put(key, map);
        }
        quMaps.put("scoreMaps", scoreMaps);
        Map<String, Object> quOrderMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.ORDERQU.getIndex() + "_");// 排序
        for (String key : quOrderMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, quOrderMaps.get(key).toString());
            quOrderMaps.put(key, map);
        }
        quMaps.put("quOrderMaps", quOrderMaps);
        Map<String, Object> chenRadioMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.CHENRADIO.getIndex() + "_");
        for (String key : chenRadioMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, chenRadioMaps.get(key).toString());
            chenRadioMaps.put(key, map);
        }
        quMaps.put("chenRadioMaps", chenRadioMaps);
        Map<String, Object> chenCheckboxMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.CHENCHECKBOX.getIndex() + "_");
        for (String key : chenCheckboxMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, chenCheckboxMaps.get(key).toString());
            for (String keyRow : map.keySet()) {
                mapRow = WebUtils.getParametersStartingWith(request, map.get(keyRow).toString());
                map.put(keyRow, mapRow);
            }
            chenCheckboxMaps.put(key, map);
        }
        quMaps.put("chenCheckboxMaps", chenCheckboxMaps);
        Map<String, Object> chenScoreMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.CHENSCORE.getIndex() + "_");
        for (String key : chenScoreMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, chenScoreMaps.get(key).toString());
            for (String keyRow : map.keySet()) {
                mapRow = WebUtils.getParametersStartingWith(request, map.get(keyRow).toString());
                map.put(keyRow, mapRow);
            }
            chenScoreMaps.put(key, map);
        }
        quMaps.put("chenScoreMaps", chenScoreMaps);
        Map<String, Object> chenFbkMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.CHENFBK.getIndex() + "_");
        for (String key : chenFbkMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, chenFbkMaps.get(key).toString());
            for (String keyRow : map.keySet()) {
                mapRow = WebUtils.getParametersStartingWith(request, map.get(keyRow).toString());
                map.put(keyRow, mapRow);
            }
            chenFbkMaps.put(key, map);
        }
        quMaps.put("chenFbkMaps", chenFbkMaps);
        for (String key : radioMaps.keySet()) {
            quItemId = radioMaps.get(key).toString();
            otherText = params.get("text_qu_" + QuType.RADIO.getIndex() + "_" + key + "_" + quItemId).toString();
            anRadio = new HashMap<>();
            anRadio.put("quId", key);
            anRadio.put("quItemId", quItemId);
            anRadio.put("otherText", otherText);
            radioMaps.put(key, anRadio);
        }
        quMaps.put("compRadioMaps", radioMaps);
        for (String key : checkboxMaps.keySet()) {
            dfillValue = checkboxMaps.get(key).toString();
            map = WebUtils.getParametersStartingWith(request, dfillValue);
            for (String key2 : map.keySet()) {
                quItemId = map.get(key2).toString();
                otherText = params.get("text_" + dfillValue + quItemId).toString();
                anCheckbox = new HashMap<>();
                anCheckbox.put("quItemId", quItemId);
                anCheckbox.put("otherText", otherText);
                map.put(key2, anCheckbox);
            }
            checkboxMaps.put(key, map);
        }
        quMaps.put("compCheckboxMaps", checkboxMaps);
        Map<String, Object> chenCompChenRadioMaps = WebUtils.getParametersStartingWith(request, "qu_" + QuType.COMPCHENRADIO.getIndex() + "_");
        for (String key : chenCompChenRadioMaps.keySet()) {
            map = WebUtils.getParametersStartingWith(request, chenCompChenRadioMaps.get(key).toString());
            for (String keyRow : map.keySet()) {
                mapRow = WebUtils.getParametersStartingWith(request, map.get(keyRow).toString());
                map.put(keyRow, mapRow);
            }
            chenCompChenRadioMaps.put(key, map);
        }
        quMaps.put("compChenRadioMaps", chenCompChenRadioMaps);
        return quMaps;
    }

    /**
     * 保存答案
     *
     * @param surveyAnswer
     * @param quMaps       参数
     */
    public void saveAnswer(Map<String, Object> surveyAnswer, Map<String, Map<String, Object>> quMaps) {
        String id = surveyAnswer.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        surveyMation.put("answerNum", Integer.parseInt(surveyMation.get("answerNum").toString()) + 1);
        dwSurveyDirectoryDao.editSurveyAnswerNumById(surveyMation);//修改回答数量
        if ("1".equals(surveyMation.get("ynEndNum").toString())) {//是否依据收到的份数结束
            if (Integer.parseInt(surveyMation.get("answerNum").toString()) + 1 >= Integer.parseInt(surveyMation.get("endNum").toString())) {
                surveyMation.put("realEndTime", DateUtil.getTimeAndToString());
                dwSurveyDirectoryDao.editSurveyStateToEndNumById(surveyMation);//结束调查
            }
        }
        //问卷答案
        surveyAnswer.put("bgAnDate", surveyAnswer.get("bgAnDate"));
        surveyAnswer.put("endAnDate", DateUtil.getTimeAndToString());
        surveyAnswer.put("totalTime", DateUtil.getDistanceMinute(surveyAnswer.get("bgAnDate").toString(), surveyAnswer.get("endAnDate").toString()));//分钟
        surveyAnswer.put("answerId", ToolUtil.getSurFaceId());

        int anCount = 0;//回答的题目数，包含多个答案的数量
        // 保存答案
        // 是非题
        Map<String, Object> yesnoMaps = quMaps.get("yesnoMaps");
        anCount += saveAnYesnoMaps(surveyAnswer, yesnoMaps);
        // 单选题
        Map<String, Object> radioMaps = quMaps.get("radioMaps");
        anCount += saveAnRadioMaps(surveyAnswer, radioMaps);
        // 多选题
        Map<String, Object> checkboxMaps = quMaps.get("checkboxMaps");
        anCount += saveAnCheckboxMaps(surveyAnswer, checkboxMaps);
        // 填空题
        Map<String, Object> fillblankMaps = quMaps.get("fillblankMaps");
        anCount += saveAnFillMaps(surveyAnswer, fillblankMaps);
        // 多项填空题
        Map<String, Object> multifillblankMaps = quMaps.get("multifillblankMaps");
        anCount += saveAnMultiFillMaps(surveyAnswer, multifillblankMaps);
        // 问答题
        Map<String, Object> answerMaps = quMaps.get("answerMaps");
        anCount += saveAnAnswerMaps(surveyAnswer, answerMaps);
        // 复合单选题
        Map<String, Object> compRadioMaps = quMaps.get("compRadioMaps");
        anCount += saveCompAnRadioMaps(surveyAnswer, compRadioMaps);
        // 复合多选题
        Map<String, Object> compCheckboxMaps = quMaps.get("compCheckboxMaps");
        anCount += saveCompAnCheckboxMaps(surveyAnswer, compCheckboxMaps);
        // 枚举题
        Map<String, Object> enumMaps = quMaps.get("enumMaps");
        anCount += saveEnumMaps(surveyAnswer, enumMaps);
        // 评分题
        Map<String, Object> scoreMaps = quMaps.get("scoreMaps");
        anCount += saveScoreMaps(surveyAnswer, scoreMaps);
        // 排序题 quOrderMaps
        Map<String, Object> quOrderMaps = quMaps.get("quOrderMaps");
        anCount += saveQuOrderMaps(surveyAnswer, quOrderMaps);
        // 矩阵单选题
        Map<String, Object> chehRadioMaps = quMaps.get("chenRadioMaps");
        anCount += saveChenRadioMaps(surveyAnswer, chehRadioMaps);
        // 矩阵多选题
        Map<String, Object> chehCheckboxMaps = quMaps.get("chenCheckboxMaps");
        anCount += saveChenCheckboxMaps(surveyAnswer, chehCheckboxMaps);
        // 矩阵填空题
        Map<String, Object> chenFbkMaps = quMaps.get("chenFbkMaps");
        anCount += saveChenFbkMaps(surveyAnswer, chenFbkMaps);
        // 复合矩阵单选题
        Map<String, Object> compChehRadioMaps = quMaps.get("compChenRadioMaps");
        anCount += saveCompChehRadioMaps(surveyAnswer, compChehRadioMaps);
        // 矩阵填空题
        Map<String, Object> chenScoreMaps = quMaps.get("chenScoreMaps");
        anCount += saveChenScoreMaps(surveyAnswer, chenScoreMaps);

        surveyAnswer.put("completeItemNum", anCount);
        int isComplete = 0;
        if (anCount >= Integer.parseInt(surveyMation.get("anItemLeastNum").toString())) {
            isComplete = 1;
        }
        surveyAnswer.put("isComplete", isComplete);

        int isEffective = 0;
        if (anCount >= 0) {//只要回答一题即为有效
            isEffective = 1;
        }
        surveyAnswer.put("isEffective", isEffective);
        surveyAnswer.put("completeNum", surveyMation.get("surveyQuNum"));
        surveyAnswer.put("quNum", surveyMation.get("surveyQuNum"));
        dwSurveyDirectoryDao.insertSurveyAnswer(surveyAnswer);
    }

    /**
     * 保存是非题答案
     *
     * @param surveyAnswer
     * @param yesnoMaps
     * @return
     */
    public int saveAnYesnoMaps(Map<String, Object> surveyAnswer, Map<String, Object> yesnoMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        List<Map<String, Object>> beans = new ArrayList<>();
        int answerQuCount = 0;
        if (yesnoMaps != null) {
            for (String key : yesnoMaps.keySet()) {
                answerQuCount++;
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("yesnoAnswer", yesnoMaps.get(key).toString());
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnYesnoMaps(beans);
        }
        return answerQuCount;
    }

    private Map<String, Object> setCommonMation(String surveyId, String surveyAnswerId, String key) {
        Map<String, Object> bean = new HashMap<>();
        bean.put("id", ToolUtil.getSurFaceId());
        bean.put("surveyId", surveyId);
        bean.put("surveyAnswerId", surveyAnswerId);
        bean.put("quId", key);
        return bean;
    }

    /**
     * 保存单选题答案
     *
     * @param surveyAnswer
     * @param radioMaps
     * @return
     */
    private int saveAnRadioMaps(Map<String, Object> surveyAnswer, Map<String, Object> radioMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (radioMaps != null) {
            for (String key : radioMaps.keySet()) {
                answerQuCount++;
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("quItemId", radioMaps.get(key).toString());
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnRadioMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存多项填空题答案
     *
     * @param surveyAnswer
     * @param dfillMaps
     * @return
     */
    private int saveAnMultiFillMaps(Map<String, Object> surveyAnswer, Map<String, Object> dfillMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (dfillMaps != null) {
            Map<String, Object> map;
            for (String key : dfillMaps.keySet()) {
                map = (Map<String, Object>) dfillMaps.get(key);
                if (map != null && map.size() > 0) {
                    for (String keyMap : map.keySet()) {
                        answerQuCount++;
                        Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                        bean.put("quItemId", keyMap);
                        bean.put("answerValue", !map.containsKey(keyMap) ? "" : map.get(keyMap).toString());
                        beans.add(bean);
                    }
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnMultiFillMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存评分题
     *
     * @param surveyAnswer
     * @param scoreMaps
     */
    private int saveScoreMaps(Map<String, Object> surveyAnswer, Map<String, Object> scoreMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (scoreMaps != null) {
            Map<String, Object> mapRows;
            for (String key : scoreMaps.keySet()) {
                mapRows = (Map<String, Object>) scoreMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    answerQuCount++;
                    Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                    bean.put("rowId", keyRow);
                    bean.put("scoreValue", mapRows.get(keyRow).toString());
                    beans.add(bean);
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveScoreMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存矩阵多选题
     *
     * @param surveyAnswer
     * @param chenCheckboxMaps
     */
    private int saveChenCheckboxMaps(Map<String, Object> surveyAnswer, Map<String, Object> chenCheckboxMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (chenCheckboxMaps != null) {
            Map<String, Object> mapRows, mapRow;
            for (String key : chenCheckboxMaps.keySet()) {
                mapRows = (Map<String, Object>) chenCheckboxMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    mapRow = (Map<String, Object>) mapRows.get(keyRow);
                    for (String keyCol : mapRow.keySet()) {
                        answerQuCount++;
                        Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                        bean.put("rowId", keyRow);
                        bean.put("colId", keyCol);
                        beans.add(bean);
                    }
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveChenCheckboxMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 复合单选题
     *
     * @param surveyAnswer
     * @param compRadioMaps
     */
    private int saveCompAnRadioMaps(Map<String, Object> surveyAnswer, Map<String, Object> compRadioMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (compRadioMaps != null) {
            Map<String, Object> tempAnRadio;
            for (String key : compRadioMaps.keySet()) {
                answerQuCount++;
                tempAnRadio = (Map<String, Object>) compRadioMaps.get(key);
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("quItemId", tempAnRadio.get("quItemId"));
                bean.put("otherText", tempAnRadio.get("otherText"));
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveCompAnRadioMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 复合矩阵单选题
     *
     * @param surveyAnswer
     * @param compChenRadioMaps
     */
    private int saveCompChehRadioMaps(Map<String, Object> surveyAnswer, Map<String, Object> compChenRadioMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (compChenRadioMaps != null) {
            Map<String, Object> mapRows, mapRow;
            for (String key : compChenRadioMaps.keySet()) {
                mapRows = (Map<String, Object>) compChenRadioMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    mapRow = (Map<String, Object>) mapRows.get(keyRow);
                    for (String keyCol : mapRow.keySet()) {
                        answerQuCount++;
                        Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                        bean.put("rowId", keyRow);
                        bean.put("colId", keyCol);
                        bean.put("optionId", mapRow.get(keyCol).toString());
                        beans.add(bean);
                    }
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveCompChehRadioMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 矩陈评分题
     *
     * @param surveyAnswer
     * @param chenScoreMaps
     * @return
     */
    private int saveChenScoreMaps(Map<String, Object> surveyAnswer, Map<String, Object> chenScoreMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (chenScoreMaps != null) {
            Map<String, Object> mapRows, mapRow;
            for (String key : chenScoreMaps.keySet()) {
                mapRows = (Map<String, Object>) chenScoreMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    mapRow = (Map<String, Object>) mapRows.get(keyRow);
                    for (String keyCol : mapRow.keySet()) {
                        answerQuCount++;
                        Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                        bean.put("rowId", keyRow);
                        bean.put("colId", keyCol);
                        bean.put("answerValue", mapRow.get(keyCol).toString());
                        beans.add(bean);
                    }
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveChenScoreMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存多选题答案
     *
     * @param surveyAnswer
     * @param checkboxMaps
     */
    private int saveAnCheckboxMaps(Map<String, Object> surveyAnswer, Map<String, Object> checkboxMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (checkboxMaps != null) {
            Map<String, Object> map;
            for (String key : checkboxMaps.keySet()) {
                map = (Map<String, Object>) checkboxMaps.get(key);
                for (String keyMap : map.keySet()) {
                    answerQuCount++;
                    Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                    bean.put("quItemId", map.get(keyMap).toString());
                    beans.add(bean);
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnCheckboxMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存单项填空题答案
     *
     * @param surveyAnswer
     * @param fillMaps
     */
    private int saveAnFillMaps(Map<String, Object> surveyAnswer, Map<String, Object> fillMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (fillMaps != null) {
            for (String key : fillMaps.keySet()) {
                answerQuCount++;
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("answerValue", fillMaps.get(key).toString());
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnFillMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存判断题答案
     *
     * @param surveyAnswer
     * @param anAnswerMaps
     */
    private int saveAnAnswerMaps(Map<String, Object> surveyAnswer, Map<String, Object> anAnswerMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (anAnswerMaps != null) {
            for (String key : anAnswerMaps.keySet()) {
                answerQuCount++;
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("answerValue", anAnswerMaps.get(key).toString());
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveAnAnswerMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存复合多选题答案
     *
     * @param surveyAnswer
     * @param compCheckboxMaps
     */
    private int saveCompAnCheckboxMaps(Map<String, Object> surveyAnswer, Map<String, Object> compCheckboxMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (compCheckboxMaps != null) {
            Map<String, Object> map, tempAnCheckbox;
            for (String key : compCheckboxMaps.keySet()) {
                map = (Map<String, Object>) compCheckboxMaps.get(key);
                for (String keyMap : map.keySet()) {
                    answerQuCount++;
                    tempAnCheckbox = (Map<String, Object>) map.get(keyMap);
                    Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                    bean.put("quItemId", tempAnCheckbox.get("quItemId"));
                    bean.put("otherText", tempAnCheckbox.get("otherText"));
                    beans.add(bean);
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveCompAnCheckboxMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存枚举题
     *
     * @param surveyAnswer
     * @param enumMaps
     */
    private int saveEnumMaps(Map<String, Object> surveyAnswer, Map<String, Object> enumMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (enumMaps != null) {
            for (String key : enumMaps.keySet()) {
                answerQuCount++;
                Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                bean.put("quItemNum", Integer.parseInt(key.split("_")[1]));
                bean.put("answerValue", enumMaps.get(key).toString());
                beans.add(bean);
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveEnumMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存排序题
     *
     * @param surveyAnswer
     * @param quOrderMaps
     */
    private int saveQuOrderMaps(Map<String, Object> surveyAnswer, Map<String, Object> quOrderMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (quOrderMaps != null) {
            Map<String, Object> mapRows;
            for (String key : quOrderMaps.keySet()) {
                mapRows = (Map<String, Object>) quOrderMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    answerQuCount++;
                    Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                    bean.put("rowId", keyRow);
                    bean.put("orderNumValue", mapRows.get(keyRow).toString());
                    beans.add(bean);
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveQuOrderMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存矩阵单选题
     *
     * @param surveyAnswer
     * @param chenRadioMaps
     */
    private int saveChenRadioMaps(Map<String, Object> surveyAnswer, Map<String, Object> chenRadioMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (chenRadioMaps != null) {
            Map<String, Object> mapRows;
            for (String key : chenRadioMaps.keySet()) {
                mapRows = (Map<String, Object>) chenRadioMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    answerQuCount++;
                    Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                    bean.put("rowId", keyRow);
                    bean.put("colId", mapRows.get(keyRow).toString());
                    beans.add(bean);
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveChenRadioMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 保存矩阵填空题
     *
     * @param surveyAnswer
     * @param chenFbkMaps
     */
    private int saveChenFbkMaps(Map<String, Object> surveyAnswer, Map<String, Object> chenFbkMaps) {
        String surveyId = surveyAnswer.get("id").toString();
        String surveyAnswerId = surveyAnswer.get("answerId").toString();
        int answerQuCount = 0;
        List<Map<String, Object>> beans = new ArrayList<>();
        if (chenFbkMaps != null) {
            Map<String, Object> mapRows, mapRow;
            for (String key : chenFbkMaps.keySet()) {
                mapRows = (Map<String, Object>) chenFbkMaps.get(key);
                for (String keyRow : mapRows.keySet()) {
                    mapRow = (Map<String, Object>) mapRows.get(keyRow);
                    for (String keyCol : mapRow.keySet()) {
                        answerQuCount++;
                        Map<String, Object> bean = setCommonMation(surveyId, surveyAnswerId, key);
                        bean.put("rowId", keyRow);
                        bean.put("colId", keyCol);
                        bean.put("answerValue", mapRow.get(keyCol).toString());
                        beans.add(bean);
                    }
                }
            }
        }
        if (!beans.isEmpty()) {
            dwSurveyDirectoryDao.saveChenFbkMaps(beans);
        }
        return answerQuCount;
    }

    /**
     * 手动结束问卷
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateSurveyMationEndById(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String id = map.get("id").toString();
        // 获取问卷信息
        Map<String, Object> surveyMation = dwSurveyDirectoryDao.querySurveyMationById(id);
        if (surveyMation != null && !surveyMation.isEmpty()) {
            if ("1".equals(surveyMation.get("surveyState").toString())) {//执行中
                map.put("realEndTime", DateUtil.getTimeAndToString());
                dwSurveyDirectoryDao.updateSurveyMationEndById(map);
            }
        } else {
            outputObject.setreturnMessage("该试卷信息不存在。");
        }
    }

}
