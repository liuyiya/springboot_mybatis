package com.evolution.domain.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evolution.domain.system.model.dto.SysMenuNode;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.types.reponse.PageResult;

import java.util.List;

public interface MenuService extends IService<SysMenu> {

    void addMenu(SysMenu sysMenu);

    List<SysMenuNode> getTreeMenu();

    List<SysMenu> getTreeMenuByRoleId(Long roleId);

    PageResult<SysMenu> getMenuPageList(Long parentId, Integer pageSize, Integer pageNum);

    void delete(Long id);
}
