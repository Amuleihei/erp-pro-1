/*******************************************************************************
 * Copyright 卫志强 QQ：598748873@qq.com Inc. All rights reserved. 开源地址：https://gitee.com/doc_wei01/skyeye
 ******************************************************************************/

package com.skyeye.common.constans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Constants
 * @Description: 系统公共常量类
 * @author: skyeye云系列--卫志强
 * @date: 2021/6/6 23:22
 * @Copyright: 2021 https://gitee.com/doc_wei01/skyeye Inc. All rights reserved.
 * 注意：本内容仅限购买后使用.禁止私自外泄以及用于其他的商业目的
 */
public class Constants {

    /**
     * 保存模板说明的redis的key
     */
    public static final String REDIS_CODEMODEL_EXPLAIN_EXEXPLAIN = "exexplaintocodemodel";

    public static String getSysExplainExexplainRedisKey(Integer type) {
        return REDIS_CODEMODEL_EXPLAIN_EXEXPLAIN + "_" + type;
    }

    // win系统桌面图片列表的redis的key
    public static final String SYS_WIN_BG_PIC_REDIS_KEY = "sys_win_bg_pic_redis_key";

    // win系统锁屏桌面图片列表的redis的key
    public static final String SYS_WIN_LOCK_BG_PIC_REDIS_KEY = "sys_win_lock_bg_pic_redis_key";

    // win系统主题颜色列表的redis的key
    public static final String SYS_WIN_THEME_COLOR_REDIS_KEY = "sys_win_theme_color_redis_key";

    // 开发文档获取一级分类列表的redis的key
    public static final String SYS_DEVE_LOP_DOC_FIRST_TYPE = "sys_deve_lop_doc_first_type";

    public static String getSysDeveLopDocFirstType() {
        return SYS_DEVE_LOP_DOC_FIRST_TYPE;
    }

    // 开发文档获取二级分类列表的redis的key
    public static final String SYS_DEVE_LOP_DOC_SECOND_TYPE = "sys_deve_lop_doc_second_type_";

    public static String getSysDeveLopDocSecondType(String parentId) {
        return SYS_DEVE_LOP_DOC_SECOND_TYPE + parentId;
    }

    // 开发文档获取文档标题列表的redis的key
    public static final String SYS_DEVE_LOP_DOC_TITLE_LIST = "sys_deve_lop_doc_title_list_";

    public static String getSysDeveLopDocTitleList(String parentId) {
        return SYS_DEVE_LOP_DOC_TITLE_LIST + parentId;
    }

    // 开发文档获取文档内容的redis的key
    public static final String SYS_DEVE_LOP_DOC_CONTENT = "sys_deve_lop_doc_content_";

    public static String getSysDeveLopDocContent(String id) {
        return SYS_DEVE_LOP_DOC_CONTENT + id;
    }

    // 聊天获取当前登陆用户信息在redis中的key
    public static final String SYS_TALK_USER_THIS_MATN_MATION = "sys_talk_user_this_matn_mation_";

    public static String getSysTalkUserThisMainMationById(String id) {
        return SYS_TALK_USER_THIS_MATN_MATION + id;
    }

    // 聊天获取当前登陆用户拥有的群组列表在redis中的key
    public static final String SYS_TALK_USER_HAS_GROUP_LIST_MATION = "sys_talk_user_has_group_list_mation_";

    public static String getSysTalkUserHasGroupListMationById(String id) {
        return SYS_TALK_USER_HAS_GROUP_LIST_MATION + id;
    }

    // 聊天获取分组下的用户列表信息在redis中的key
    public static final String SYS_TALK_GROUP_USER_LIST_MATION = "sys_talk_group_user_list_mation_";

    public static String getSysTalkGroupUserListMationById(String id) {
        return SYS_TALK_GROUP_USER_LIST_MATION + id;
    }

    // 获取我的附件0级列表
    public static final List<Map<String, Object>> getSysEnclosureZeroList() {
        List<Map<String, Object>> beans = new ArrayList<>();
        Map<String, Object> favorites = new HashMap<>();
        favorites.put("id", "1");
        favorites.put("name", "我的附件");
        favorites.put("pId", "0");
        favorites.put("isParent", 1);// 是否是文件夹 0否1是
        favorites.put("icon", "../../assets/images/my-folder-icon.png");// 图标
        beans.add(favorites);
        return beans;
    }

    // 获取群组成员列表
    public static final String SYS_EVE_TALK_GROUP_USER_LIST = "sys_eve_talk_group_user_list_";

    public static String checkSysEveTalkGroupUserListByGroupId(String groupId) {
        return SYS_EVE_TALK_GROUP_USER_LIST + groupId;
    }

}
