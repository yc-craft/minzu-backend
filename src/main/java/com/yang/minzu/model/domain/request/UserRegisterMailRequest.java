package com.yang.minzu.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户邮箱请求体
 *
 * @author yang
 */
@Data
public class UserRegisterMailRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String email;
}
