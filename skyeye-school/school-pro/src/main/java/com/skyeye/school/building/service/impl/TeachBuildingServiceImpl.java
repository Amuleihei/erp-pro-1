/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.school.building.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skyeye.annotation.service.SkyeyeService;
import com.skyeye.base.business.service.impl.SkyeyeBusinessServiceImpl;
import com.skyeye.common.entity.search.CommonPageInfo;
import com.skyeye.common.object.InputObject;
import com.skyeye.common.object.OutputObject;
import com.skyeye.common.util.mybatisplus.MybatisPlusUtil;
import com.skyeye.eve.service.SchoolService;
import com.skyeye.exception.CustomException;
import com.skyeye.school.building.dao.TeachBuildingDao;
import com.skyeye.school.building.entity.Floor;
import com.skyeye.school.building.entity.LocationServe;
import com.skyeye.school.building.entity.TeachBuilding;
import com.skyeye.school.building.service.FloorService;
import com.skyeye.school.building.service.LocationServeService;
import com.skyeye.school.building.service.TeachBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TeachBuildingServiceImpl
 * @Description: 地点管理服务层
 * @author: skyeye云系列--lqy
 * @date: 2024/8/7 20:48
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
@SkyeyeService(name = "地点管理", groupName = "地点管理")
public class TeachBuildingServiceImpl extends SkyeyeBusinessServiceImpl<TeachBuildingDao, TeachBuilding> implements TeachBuildingService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private LocationServeService locationServeService;

    @Override
    public List<Map<String, Object>> queryPageDataList(InputObject inputObject) {
        List<Map<String, Object>> beans = super.queryPageDataList(inputObject);
        schoolService.setMationForMap(beans, "schoolId", "schoolMation");
        return beans;
    }

    @Override
    public TeachBuilding selectById(String id) {
        TeachBuilding teachBuilding = super.selectById(id);
        schoolService.setDataMation(teachBuilding, TeachBuilding::getSchoolId);
        return teachBuilding;
    }

    @Override
    public List<TeachBuilding> selectByIds(String...ids){
        List<TeachBuilding> teachBuildingList = super.selectByIds(ids);
        schoolService.setDataMation(teachBuildingList,TeachBuilding::getSchoolId);
        return teachBuildingList;
    }

    /**
     * 根据学校id获取地点信息
     *
     * @param inputObject  入参以及用户信息等获取对象
     * @param outputObject 出参以及提示信息的返回值对象
     */
    @Override
    public void queryTeachBuildingBySchoolId(InputObject inputObject, OutputObject outputObject) {
        Map<String, Object> map = inputObject.getParams();
        String schoolId = map.get("schoolId").toString();
        if (StrUtil.isEmpty(schoolId)) {
            return;
        }
        QueryWrapper<TeachBuilding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(TeachBuilding::getSchoolId), schoolId);
        List<TeachBuilding> teachBuildingList = list(queryWrapper);
        outputObject.setBeans(teachBuildingList);
        outputObject.settotal(teachBuildingList.size());
    }

    @Override
    public void queryTeachBuildingByHolderId(InputObject inputObject, OutputObject outputObject) {
        CommonPageInfo commonPageInfo = inputObject.getParams(CommonPageInfo.class);
        String typeId = commonPageInfo.getHolderId();
        if(StringUtils.isEmpty(typeId)){
            throw new CustomException("地点分类id不能为空");
        }
        Page page = PageHelper.startPage(commonPageInfo.getPage(), commonPageInfo.getLimit());
        QueryWrapper<TeachBuilding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(TeachBuilding::getTypeId), typeId);
        List<TeachBuilding> teachBuildingList = list(queryWrapper);
        schoolService.setDataMation(teachBuildingList,TeachBuilding::getSchoolId);
        outputObject.setBeans(teachBuildingList);
        outputObject.settotal(page.getTotal());
    }

    @Transactional
    @Override
    public void deleteById(InputObject inputObject, OutputObject outputObject) {
        String id = inputObject.getParams().get("id").toString();
        if(StringUtils.isEmpty(id)){
            return;
        }
        deleteById(id);
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(Floor::getLocationId), id);
        List<Floor> floorList = floorService.list(queryWrapper);
        floorService.remove(queryWrapper);

        for(Floor floor : floorList){
            QueryWrapper<LocationServe> serveQueryWrapper = new QueryWrapper<>();
            serveQueryWrapper.eq(MybatisPlusUtil.toColumns(LocationServe::getFloorId), floor.getId());
            locationServeService.remove(serveQueryWrapper);
        }
    }

}
