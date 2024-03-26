package com.evolution.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.model.entity.SysMenuRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuRoleMapper extends BaseMapper<SysMenuRole> {

    List<SysMenu> getMenuListByRoleId(Long roleId);

}
