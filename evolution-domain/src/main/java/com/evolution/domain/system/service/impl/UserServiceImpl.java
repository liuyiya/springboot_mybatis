package com.evolution.domain.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evolution.domain.system.mapper.RoleMapper;
import com.evolution.domain.system.mapper.RoleUserMapper;
import com.evolution.domain.system.mapper.UserMapper;
import com.evolution.domain.system.model.dto.UserInfoDto;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.entity.SysRoleUser;
import com.evolution.domain.system.model.entity.SysUser;
import com.evolution.domain.system.model.param.UserListParam;
import com.evolution.domain.system.service.SystemCacheService;
import com.evolution.domain.system.service.UserService;
import com.evolution.types.exception.AppException;
import com.evolution.types.reponse.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RoleUserMapper roleUserMapper;
    private final MenuServiceImpl menuServiceImpl;
    private final SystemCacheService systemCacheService;

    @Override
    public PageResult<SysUser> getUserList(UserListParam param) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getDelFlag, 0);
        if (StrUtil.isNotBlank(param.getKeyword())) {
            wrapper.and(keyword -> keyword.like(SysUser::getUsername, param.getKeyword())
                    .or().like(SysUser::getNickName, param.getKeyword()));
        }
        List<SysUser> sysUsers = userMapper.selectList(wrapper);
        return new PageResult<SysUser>().initPage(param, sysUsers);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        Wrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void addUser(SysUser sysUser) {
        sysUser.setStatus(1);
        sysUser.setDelFlag(0);
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        this.save(sysUser);
    }

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        UserInfoDto userInfoDto = systemCacheService.getUserInfo(userId);
        if (BeanUtil.isNotEmpty(userInfoDto)) {
            return userInfoDto;
        }
        //获取用户信息
        SysUser user = userMapper.selectById(userId);
        //获取关联角色
        LambdaQueryWrapper<SysRoleUser> roleWrapper = Wrappers.lambdaQuery();
        roleWrapper.eq(SysRoleUser::getUserId, userId);
        SysRoleUser sysRoleUser = roleUserMapper.selectOne(roleWrapper);
        if (BeanUtil.isEmpty(sysRoleUser)) {
            throw new AppException("该用户还未关联角色信息");
        }
        SysRole sysRole = roleMapper.selectById(sysRoleUser.getRoleId());
        //获取关联菜单
        List<SysMenu> menus = menuServiceImpl.getTreeMenuByRoleId(sysRoleUser.getRoleId());
        userInfoDto = UserInfoDto.builder()
                .username(user.getUsername())
                .icon(user.getIcon())
                .menus(menus)
                .roles(Convert.toList(String.class, sysRole.getName()))
                .build();
        systemCacheService.setUserInfo(userId, userInfoDto);
        return userInfoDto;
    }

    @Override
    public List<SysRole> getRoleListByUserId(Long userId) {
        return roleUserMapper.getRoleListByUserId(userId);
    }

    @Override
    public void updateRole(Long userId, List<Long> roleIds) {
        //先删除原来的关系
        QueryWrapper<SysRoleUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysRoleUser::getUserId, userId);
        roleUserMapper.delete(wrapper);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                SysRoleUser roleRelation = new SysRoleUser();
                roleRelation.setUserId(userId);
                roleRelation.setRoleId(roleId);
                roleUserMapper.insert(roleRelation);
            }
        }
        systemCacheService.delUserInfo(userId);
    }

}
