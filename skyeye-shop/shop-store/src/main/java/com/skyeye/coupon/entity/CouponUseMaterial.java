package com.skyeye.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyeye.annotation.api.ApiModel;
import com.skyeye.annotation.api.ApiModelProperty;
import com.skyeye.annotation.api.Property;
import com.skyeye.common.entity.CommonInfo;
import lombok.Data;

@Data
@TableName("shop_coupon_use_material")
@ApiModel(value = "用户领取的优惠券适用商品对象管理实体类")
public class CouponUseMaterial extends CommonInfo {

    @TableId("id")
    @ApiModelProperty("主键id。为空时新增，不为空时编辑")
    private String id;

    @TableField(value = "material_id")
    @ApiModelProperty(value = "商品id", required = "required")
    private String materialId;

    @TableField(value = "coupon_id")
    @Property(value = "优惠券/模版id")
    private String couponId;
}
