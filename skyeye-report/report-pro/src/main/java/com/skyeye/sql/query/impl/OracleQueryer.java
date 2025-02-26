/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye-report
 ******************************************************************************/

package com.skyeye.sql.query.impl;

import com.skyeye.eve.entity.ReportDataSource;
import com.skyeye.sql.query.AbstractQueryer;
import com.skyeye.sql.query.Queryer;

/**
 * @ClassName: OracleQueryer
 * @Description: Oracle数据库查询器类。
 * 在使用该查询器时,请先参考:http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html
 * 获取与相应版本的Oracle jdbc driver,然后把相关jdbc driver的jar包加入该系统的类路径下(如WEB-INF/lib)
 * @author: skyeye云系列--卫志强
 * @date: 2021/5/17 21:23
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye-report Inc. All rights reserved.
 * 注意：本内容具体规则请参照readme执行，地址：https://gitee.com/doc_wei01/skyeye-report/blob/master/README.md
 */
public class OracleQueryer extends AbstractQueryer implements Queryer {

    public OracleQueryer(ReportDataSource dataSource) {
        super(dataSource);
    }

}
