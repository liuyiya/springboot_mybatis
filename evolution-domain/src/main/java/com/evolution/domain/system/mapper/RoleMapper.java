package com.evolution.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evolution.domain.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {

}
