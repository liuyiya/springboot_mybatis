package com.evolution.domain.system.service.impl;


import cn.hutool.core.util.StrUtil;
import com.evolution.domain.system.model.constant.SystemRedisConstant;
import com.evolution.domain.system.model.dto.SysMenuNode;
import com.evolution.domain.system.model.dto.UserInfoDto;
import com.evolution.domain.system.service.SystemCacheService;
import com.evolution.types.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemCacheServiceImpl implements SystemCacheService {

    private final RedisService redisService;

    @Override
    public void delUserInfo(Long userId) {
        redisService.del("");
    }

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        return (UserInfoDto) redisService.get(StrUtil.format(SystemRedisConstant.evolution_string_user_info, userId));
    }

    @Override
    public void setUserInfo(Long userId, UserInfoDto user) {
        redisService.set(StrUtil.format(SystemRedisConstant.evolution_string_user_info, userId), user);
    }

    @Override
    public void setTreeMenu(List<SysMenuNode> menus) {
        redisService.set(SystemRedisConstant.evolution_list_menu, menus);
    }

    @Override
    public List<SysMenuNode> getTreeMenu() {
        return (List<SysMenuNode>) redisService.get(SystemRedisConstant.evolution_list_menu);
    }
}
