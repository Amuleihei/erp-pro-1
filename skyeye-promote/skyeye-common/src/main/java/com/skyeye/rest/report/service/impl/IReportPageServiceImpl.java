/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.rest.report.service.impl;

import com.skyeye.base.rest.service.impl.IServiceImpl;
import com.skyeye.common.client.ExecuteFeignClient;
import com.skyeye.common.constans.CacheConstants;
import com.skyeye.rest.report.rest.IReportPageRest;
import com.skyeye.rest.report.service.IReportPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

/**
 * @ClassName: IReportPageServiceImpl
 * @Description: 报表管理公共的一些操作
 * @author: skyeye云系列--卫志强
 * @date: 2023/8/15 10:32
 * @Copyright: 2023 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目
 */
@Service
public class IReportPageServiceImpl extends IServiceImpl implements IReportPageService {

    @Autowired
    private IReportPageRest iReportPageRest;

    @Override
    public Map<String, Object> queryEntityMationById(String id) {
        return ExecuteFeignClient.get(() -> iReportPageRest.queryReportPageById(id)).getBean();
    }

    @Override
    public String queryCacheKeyById(String id) {
        return String.format(Locale.ROOT, "%s:%s", CacheConstants.REPORT_PAGE_CACHE_KEY, id);
    }

}
