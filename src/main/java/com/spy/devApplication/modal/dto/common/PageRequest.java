package com.spy.devApplication.modal.dto.common;

import com.spy.devApplication.constant.CommonConstant;
import lombok.Data;

/**
 * 分页查询
 */

@Data
public class PageRequest {

    /**
     * 当前页数
     */
    private Integer current = 1;

    /**
     * 页面数据大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
