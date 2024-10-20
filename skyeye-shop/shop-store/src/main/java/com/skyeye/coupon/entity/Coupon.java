package com.skyeye.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyeye.annotation.api.ApiModel;
import com.skyeye.annotation.api.ApiModelProperty;
import com.skyeye.common.entity.features.BaseGeneralInfo;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "shop_coupon")
@ApiModel(value = "优惠券/模版信息管理实体类")
public class Coupon extends BaseGeneralInfo {


    @TableField(value = "template_id")
    @ApiModelProperty(value = "模板id")
    private String templateId;

    @TableField(value = "enabled")
    @ApiModelProperty(value = "状态", required = "required")
    private Integer enabled;

    @TableField(value = "total_count")
    @ApiModelProperty(value = "发放数量，-1表示不限制发放数量")
    private Integer totalCount;

    @TableField(value = "take_limit_count")
    @ApiModelProperty(value = "每人限领个数，-1表示不限制")
    private Integer takeLimitCount;

    @TableField(value = "take_type")
    @ApiModelProperty(value = "领取方式")
    private Integer takeType;

    @TableField(value = "use_price")
    @ApiModelProperty(value = "是否设置满多少金额可用，单位：分。0 - 不限制，大于 0 - 多少金额可用")
    private String usePrice;

    @TableField(value = "product_scope")
    @ApiModelProperty(value = "商品范围")
    private Integer productScope;

    @TableField(value = "validity_type")
    @ApiModelProperty(value = "生效日期类型")
    private Integer validityType;

    @TableField(value = "valid_start_time   ")
    @ApiModelProperty(value = "固定日期 - 生效开始时间")
    private String validStartTime;

    @TableField(value = "valid_end_time")
    @ApiModelProperty(value = "固定日期 - 生效结束时间")
    private String validEndTime;

    @TableField(value = "fixed_start_term")
    @ApiModelProperty(value = "领取日期 - 领取几天后可以开始使用")
    private Integer fixedStartTerm;

    @TableField(value = "fixed_end_term")
    @ApiModelProperty(value = "领取日期 - 领取开始使用时几天后结束")
    private Integer fixedEndTerm;

    @TableField(value = "discount_type")
    @ApiModelProperty(value = "折扣类型")
    private Integer discountType;

    @TableField(value = "discount_percent")
    @ApiModelProperty(value = "折扣百分比，例如，80% 为 80")
    private Integer discountPercent;

    @TableField(value = "discount_price")
    @ApiModelProperty(value = "优惠金额，单位：分")
    private String discountPrice;

    @TableField(value = "discount_limit_price")
    @ApiModelProperty(value = "折扣上限，百分比折扣也受其约束")
    private String discountLimitPrice;

    @TableField(value = "take_count")
    @ApiModelProperty(value = "已经领取优惠券的数量", required = "required")
    private Integer takeCount;

    @TableField(value = "use_count")
    @ApiModelProperty(value = "使用优惠券的次数", required = "required")
    private Integer useCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "优惠券适用对象列表", required = "json")
    private List<CouponMaterial> couponMaterialList;
}