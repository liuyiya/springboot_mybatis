package com.evolution.domain.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.param.RoleListParam;
import com.evolution.types.reponse.PageResult;
import com.evolution.types.request.PageParam;

import java.util.List;

public interface RoleService extends IService<SysRole> {

    void addRole(SysRole sysRole);

    void updateRole(SysRole sysRole);

    void deleteRole(Long roleId);

    PageResult<SysRole> getRoleList(RoleListParam param);

    void assignMenu(Long roleId, List<Long> menuIds);

    List<SysRole> getAllRoleList();

    List<SysMenu> listMenu(Long roleId);

    int allocMenu(Long roleId, List<Long> menuIds);
}
