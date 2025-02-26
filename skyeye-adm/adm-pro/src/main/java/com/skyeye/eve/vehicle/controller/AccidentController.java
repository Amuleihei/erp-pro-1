/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.eve.vehicle.controller;

import com.skyeye.annotation.api.Api;
import com.skyeye.annotation.api.ApiImplicitParam;
import com.skyeye.annotation.api.ApiImplicitParams;
import com.skyeye.annotation.api.ApiOperation;
import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.eve.vehicle.entity.Accident;
import com.skyeye.eve.vehicle.service.AccidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AccidentController
 * @Description: 车辆事故管理控制层
 * @author: skyeye云系列--卫志强
 * @date: 2022/8/9 10:15
 * @Copyright: 2022 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@RestController
@Api(value = "车辆事故管理", tags = "车辆事故管理", modelName = "车辆模块")
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    /**
     * 遍历车辆事故列表
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "accident001", value = "遍历车辆事故列表", method = "POST", allUse = "1")
    @ApiImplicitParams(classBean = CommonPageInfo.class)
    @RequestMapping("/post/AccidentController/queryAccidentList")
    public void queryAccidentList(InputObject inputObject, OutputObject outputObject) {
        accidentService.queryPageList(inputObject, outputObject);
    }

    /**
     * 新增/修改事故信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "writeVehicleAccident", value = "新增/修改事故信息", method = "POST", allUse = "1")
    @ApiImplicitParams(classBean = Accident.class)
    @RequestMapping("/post/AccidentController/writeVehicleAccident")
    public void writeVehicleAccident(InputObject inputObject, OutputObject outputObject) {
        accidentService.saveOrUpdateEntity(inputObject, outputObject);
    }

    /**
     * 根据id删除事故信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @ApiOperation(id = "accident003", value = "根据id删除事故信息", method = "DELETE", allUse = "1")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(id = "id", name = "id", value = "主键id", required = "required")})
    @RequestMapping("/post/AccidentController/deleteAccidentById")
    public void deleteAccidentById(InputObject inputObject, OutputObject outputObject) {
        accidentService.deleteById(inputObject, outputObject);
    }

}
