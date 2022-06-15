package com.yang.minzu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.minzu.mapper.MinzuMapper;
import com.yang.minzu.model.domain.Minzu;
import com.yang.minzu.service.MinzuService;
import org.springframework.stereotype.Service;

/**
* @author yang
* @description 针对表【minzu(nation)】的数据库操作Service实现
* @createDate 2022-05-25 20:13:43
*/
@Service
public class MinzuServiceImpl extends ServiceImpl<MinzuMapper, Minzu>
    implements MinzuService{

}




