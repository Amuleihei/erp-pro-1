/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye-report
 ******************************************************************************/

package com.skyeye.sql.dbpool;

/**
 * @ClassName: DataSourcePoolFactory
 * @Description: 数据源连接池工厂
 * @author: skyeye云系列--卫志强
 * @date: 2021/5/17 21:01
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye-report Inc. All rights reserved.
 * 注意：本内容具体规则请参照readme执行，地址：https://gitee.com/doc_wei01/skyeye-report/blob/master/README.md
 */
public class DataSourcePoolFactory {
    public static DataSourcePoolWrapper create(final String className) {
        try {
            return (DataSourcePoolWrapper) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException("DataSourcePoolFactory Load Class Error", e);
        }
    }
}
