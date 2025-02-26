/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye-report
 ******************************************************************************/

package com.skyeye.sql.dbpool.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.skyeye.eve.entity.ReportDataSource;
import com.skyeye.sql.dbpool.DataSourcePoolWrapper;
import org.apache.commons.collections4.MapUtils;

import javax.sql.DataSource;

/**
 * @ClassName: C3p0DataSourcePool
 * @Description: c3p0数据源连接池包装类--参考http://www.mchange.com/projects/c3p0/#quickstart
 * @author: skyeye云系列--卫志强
 * @date: 2021/5/16 23:21
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye-report Inc. All rights reserved.
 * 注意：本内容具体规则请参照readme执行，地址：https://gitee.com/doc_wei01/skyeye-report/blob/master/README.md
 */
public class C3p0DataSourcePool implements DataSourcePoolWrapper {

    @Override
    public DataSource wrap(ReportDataSource rptDs) {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(rptDs.getDriverClass());
            dataSource.setJdbcUrl(rptDs.getJdbcUrl());
            dataSource.setUser(rptDs.getUser());
            dataSource.setPassword(rptDs.getPassword());
            dataSource.setInitialPoolSize(MapUtils.getInteger(rptDs.getOptions(), "initialPoolSize", 3));
            dataSource.setMinPoolSize(MapUtils.getInteger(rptDs.getOptions(), "minPoolSize", 1));
            dataSource.setMaxPoolSize(MapUtils.getInteger(rptDs.getOptions(), "maxPoolSize", 20));
            dataSource.setMaxStatements(MapUtils.getInteger(rptDs.getOptions(), "maxStatements", 50));
            dataSource.setMaxIdleTime(MapUtils.getInteger(rptDs.getOptions(), "maxIdleTime", 1800));
            dataSource.setAcquireIncrement(MapUtils.getInteger(rptDs.getOptions(), "acquireIncrement", 3));
            dataSource.setAcquireRetryAttempts(MapUtils.getInteger(rptDs.getOptions(), "acquireRetryAttempts", 30));
            dataSource.setIdleConnectionTestPeriod(
                MapUtils.getInteger(rptDs.getOptions(), "idleConnectionTestPeriod", 60));
            dataSource.setBreakAfterAcquireFailure(
                MapUtils.getBoolean(rptDs.getOptions(), "breakAfterAcquireFailure", false));
            dataSource.setTestConnectionOnCheckout(
                MapUtils.getBoolean(rptDs.getOptions(), "testConnectionOnCheckout", false));
            return dataSource;
        } catch (Exception ex) {
            throw new RuntimeException("C3p0DataSourcePool Create Error", ex);
        }
    }
}
