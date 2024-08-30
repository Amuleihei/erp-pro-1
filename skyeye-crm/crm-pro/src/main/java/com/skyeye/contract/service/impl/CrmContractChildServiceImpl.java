/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.skyeye.annotation.service.SkyeyeService;
import com.skyeye.base.business.service.impl.SkyeyeBusinessServiceImpl;
import com.skyeye.common.util.CalculationUtil;
import com.skyeye.common.util.mybatisplus.MybatisPlusUtil;
import com.skyeye.contract.dao.CrmContractChildDao;
import com.skyeye.contract.entity.CrmContractChild;
import com.skyeye.contract.service.CrmContractChildService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: CrmContractChildServiceImpl
 * @Description: 客户合同-商品明细服务层
 * @author: skyeye云系列--卫志强
 * @date: 2024/6/12 14:50
 * @Copyright: 2024 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
@Service
@SkyeyeService(name = "客户合同-商品明细", groupName = "合同管理", manageShow = false)
public class CrmContractChildServiceImpl extends SkyeyeBusinessServiceImpl<CrmContractChildDao, CrmContractChild> implements CrmContractChildService {

    @Override
    public void saveList(String parentId, List<CrmContractChild> beans) {
        deleteByParentId(parentId);
        if (CollectionUtil.isNotEmpty(beans)) {
            for (CrmContractChild supplierContractChild : beans) {
                supplierContractChild.setParentId(parentId);
            }
            createEntity(beans, StrUtil.EMPTY);
        }
    }

    @Override
    public void deleteByParentId(String parentId) {
        QueryWrapper<CrmContractChild> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(CrmContractChild::getParentId), parentId);
        remove(queryWrapper);
    }

    @Override
    public List<CrmContractChild> selectByParentId(String parentId) {
        QueryWrapper<CrmContractChild> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MybatisPlusUtil.toColumns(CrmContractChild::getParentId), parentId);
        List<CrmContractChild> list = list(queryWrapper);
        return list;
    }

    @Override
    public Map<String, List<CrmContractChild>> selectByParentId(List<String> parentIds) {
        List<CrmContractChild> list = getCrmContractChildList(parentIds);
        return list.stream().collect(Collectors.groupingBy(CrmContractChild::getParentId));
    }

    @Override
    public List<CrmContractChild> getCrmContractChildList(List<String> parentIds) {
        QueryWrapper<CrmContractChild> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(MybatisPlusUtil.toColumns(CrmContractChild::getParentId), parentIds);
        List<CrmContractChild> list = list(queryWrapper);
        return list;
    }

    @Override
    public String calcOrderAllTotalPrice(List<CrmContractChild> supplierContractChildList) {
        String totalPrice = "0";
        for (CrmContractChild supplierContractChild : supplierContractChildList) {
            // 计算子单据总价：单价 * 数量
            BigDecimal itemAllPrice = new BigDecimal(supplierContractChild.getUnitPrice());
            itemAllPrice = itemAllPrice.multiply(new BigDecimal(supplierContractChild.getOperNumber()));
            supplierContractChild.setAllPrice(itemAllPrice.toString());

            // 计算子单据价税合计：含税单价 * 数量
            BigDecimal taxUnitPrice = new BigDecimal(supplierContractChild.getTaxUnitPrice());
            taxUnitPrice = taxUnitPrice.multiply(new BigDecimal(supplierContractChild.getOperNumber()));
            supplierContractChild.setTaxLastMoney(taxUnitPrice.toString());
            totalPrice = CalculationUtil.add(totalPrice, supplierContractChild.getTaxLastMoney());
        }
        return totalPrice;
    }

}
