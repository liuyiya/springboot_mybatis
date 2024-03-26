package com.evolution.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evolution.domain.system.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

}
