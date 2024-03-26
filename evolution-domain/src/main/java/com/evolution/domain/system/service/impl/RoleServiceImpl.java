package com.evolution.domain.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evolution.domain.system.mapper.MenuRoleMapper;
import com.evolution.domain.system.mapper.RoleMapper;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.model.entity.SysMenuRole;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.param.RoleListParam;
import com.evolution.domain.system.service.RoleService;
import com.evolution.types.exception.AppException;
import com.evolution.types.reponse.PageResult;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements RoleService {

    private final RoleMapper roleMapper;
    private final MenuRoleMapper menuRoleMapper;

    @Override
    public void addRole(SysRole sysRole) {
        this.checkRoleName(sysRole);
        roleMapper.insert(sysRole);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        this.checkRoleName(sysRole);
        roleMapper.updateById(sysRole);
    }

    private void checkRoleName(SysRole sysRole) {
        String roleName = sysRole.getName();
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRole::getName, roleName);
        SysRole role = roleMapper.selectOne(wrapper);
        if (BeanUtil.isNotEmpty(role)) {
            throw new AppException("角色名不能重复");
        }
        sysRole.setCreateTime(new Date());
    }

    @Override
    public void deleteRole(Long roleId) {
        //已关联用户的不能删除
        SysRole role = roleMapper.selectById(roleId);
        role.setDelFlag(1);
        role.setUpdateTime(new Date());
        roleMapper.updateById(role);
    }

    @Override
    public PageResult<SysRole> getRoleList(RoleListParam param) {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRole::getDelFlag, 0)
                .like(StrUtil.isNotBlank(param.getKeyword()), SysRole::getName, param.getKeyword());
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SysRole> sysRoles = roleMapper.selectList(wrapper);
        return new PageResult<SysRole>().initPage(param, sysRoles);
    }

    @Override
    @Transactional
    public void assignMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysMenuRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysMenuRole::getRoleId, roleId);
        menuRoleMapper.delete(wrapper);
        for (Long menuId : menuIds) {
            SysMenuRole sysMenuRole = new SysMenuRole();
            sysMenuRole.setRoleId(roleId);
            sysMenuRole.setMenuId(menuId);
            menuRoleMapper.insert(sysMenuRole);
        }
    }

    @Override
    public List<SysRole> getAllRoleList() {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRole::getDelFlag, 0);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public List<SysMenu> listMenu(Long roleId) {
        return menuRoleMapper.getMenuListByRoleId(roleId);
    }

    @Override
    @Transactional
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<SysMenuRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysMenuRole::getRoleId, roleId);
        menuRoleMapper.delete(wrapper);
        //批量插入新关系
        List<SysMenuRole> relationList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysMenuRole relation = new SysMenuRole();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            relationList.add(relation);
            menuRoleMapper.insert(relation);
        }
        return menuIds.size();
    }


}
