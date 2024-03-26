package com.evolution.domain.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evolution.domain.system.model.dto.UserInfoDto;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.entity.SysRoleUser;
import com.evolution.domain.system.model.entity.SysUser;
import com.evolution.domain.system.model.param.UserListParam;
import com.evolution.types.reponse.PageResult;

import java.util.List;

public interface UserService extends IService<SysUser> {

    PageResult<SysUser> getUserList(UserListParam param);

    SysUser getUserByUsername(String username);

    void addUser(SysUser sysUser);

    UserInfoDto getUserInfo(Long userId);

    List<SysRole> getRoleListByUserId(Long userId);

    void updateRole(Long userId, List<Long> roleIds);
}
