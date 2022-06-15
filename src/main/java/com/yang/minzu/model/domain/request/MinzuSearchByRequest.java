package com.yang.minzu.model.domain.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 民族资源请求体
 *
 * @author yang
 */
@Data
public class MinzuSearchByRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String minzuName;

    private String[] minzuType;

    private String minzuSource;
}
