package com.evolution.domain.system.service;


import com.evolution.domain.system.model.dto.SysMenuNode;
import com.evolution.domain.system.model.dto.UserInfoDto;

import java.util.List;

/**
 * 后台用户缓存管理Service
 */
public interface SystemCacheService {

    /**
     * 删除后台用户缓存
     */
    void delUserInfo(Long userId);

    /**
     * 获取缓存后台用户信息
     */
    UserInfoDto getUserInfo(Long userId);

    /**
     * 设置缓存后台用户信息
     */
    void setUserInfo(Long userId, UserInfoDto user);

    /**
     * 设置菜单列表
     */
    void setTreeMenu(List<SysMenuNode> menus);

    /**
     * 获取菜单列表
     */
    List<SysMenuNode> getTreeMenu();

}
