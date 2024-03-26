package com.evolution.domain.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evolution.domain.system.mapper.MenuMapper;
import com.evolution.domain.system.mapper.MenuRoleMapper;
import com.evolution.domain.system.model.dto.SysMenuNode;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.service.MenuService;
import com.evolution.domain.system.service.SystemCacheService;
import com.evolution.types.enums.StatusEnum;
import com.evolution.types.exception.AppException;
import com.evolution.types.reponse.PageResult;
import com.evolution.types.request.PageParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, SysMenu> implements MenuService {

    private final MenuMapper menuMapper;
    private final MenuRoleMapper menuRoleMapper;
    private final SystemCacheService systemCacheService;

    @Override
    public void addMenu(SysMenu sysMenu) {
        sysMenu.setCreateTime(new Date());
        sysMenu.setStatus(1);
        updateLevel(sysMenu);
        menuMapper.insert(sysMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(SysMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            SysMenu parentMenu = getById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public List<SysMenuNode> getTreeMenu() {
        List<SysMenuNode> menuNodes = systemCacheService.getTreeMenu();
        if (CollectionUtil.isNotEmpty(menuNodes)) {
            return menuNodes;
        }
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysMenu::getStatus, StatusEnum.ENABLE.getStatus());
        List<SysMenu> sysMenus = menuMapper.selectList(wrapper);
        menuNodes = sysMenus.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, sysMenus)).collect(Collectors.toList());
        systemCacheService.setTreeMenu(menuNodes);
        return menuNodes;
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private SysMenuNode covertMenuNode(SysMenu menu, List<SysMenu> menuList) {
        SysMenuNode node = new SysMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<SysMenu> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

    @Override
    public List<SysMenu> getTreeMenuByRoleId(Long roleId) {
        return menuRoleMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public PageResult<SysMenu> getMenuPageList(Long parentId, Integer pageSize, Integer pageNum) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysMenu::getStatus, 1);
        wrapper.eq(SysMenu::getParentId, parentId);
        List<SysMenu> sysUsers = menuMapper.selectList(wrapper);
        return new PageResult<SysMenu>().initPage(new PageParam(pageSize, pageNum), sysUsers);
    }

    @Override
    public void delete(Long id) {
        LambdaQueryWrapper<SysMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysMenu::getStatus, 1);
        wrapper.eq(SysMenu::getParentId, id);
        List<SysMenu> sysUsers = menuMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(sysUsers)) {
            throw new AppException("有子类不能删除");
        }
        menuMapper.deleteById(id);
    }


}
