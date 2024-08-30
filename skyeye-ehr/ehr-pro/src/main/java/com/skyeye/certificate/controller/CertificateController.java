/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.certificate.controller;

import com.skyeye.annotation.api.Api;
import com.skyeye.annotation.api.ApiImplicitParam;
import com.skyeye.annotation.api.ApiImplicitParams;
import com.skyeye.annotation.api.ApiOperation;
import com.skyeye.certificate.entity.Certificate;
import com.skyeye.certificate.service.CertificateService;
import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: CertificateController
 * @Description: 员工证书管理控制层
 * @author: skyeye云系列--卫志强
 * @date: 2023/5/17 17:25
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@RestController
@Api(value = "员工证书", tags = "员工证书", modelName = "员工证书")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    /**
     * 查询员工证书列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "queryCertificateList", value = "查询员工证书列表", method = "POST", allUse = "2")
    @ApiImplicitParams(classBean = CommonPageInfo.class)
    @RequestMapping("/post/CertificateController/queryCertificateList")
    public void queryCertificateList(InputObject inputObject, OutputObject outputObject) {
        certificateService.queryPageList(inputObject, outputObject);
    }

    /**
     * 新增/编辑员工证书信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "writeCertificate", value = "新增/编辑员工证书信息", method = "POST", allUse = "2")
    @ApiImplicitParams(classBean = Certificate.class)
    @RequestMapping("/post/CertificateController/writeCertificate")
    public void writeCertificate(InputObject inputObject, OutputObject outputObject) {
        certificateService.saveOrUpdateEntity(inputObject, outputObject);
    }

    /**
     * 根据id删除员工证书信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "deleteCertificateById", value = "根据id删除员工证书信息", method = "DELETE", allUse = "2")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(id = "id", name = "id", value = "主键id", required = "required")})
    @RequestMapping("/post/CertificateController/deleteCertificateById")
    public void deleteCertificateById(InputObject inputObject, OutputObject outputObject) {
        certificateService.deleteById(inputObject, outputObject);
    }

}
