package com.evolution.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.entity.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleUserMapper extends BaseMapper<SysRoleUser> {

    List<SysRole> getRoleListByUserId(Long userId);

}
