package com.evolution.trigger.http.system;

import com.evolution.domain.system.model.dto.UserInfoDto;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.entity.SysUser;
import com.evolution.domain.system.model.param.UserListParam;
import com.evolution.domain.system.service.UserService;
import com.evolution.types.reponse.PageResult;
import com.evolution.types.reponse.ResultBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {

    private final UserService userService;

    /**
     * 获取用户列表
     */
    @PostMapping("/list")
    public ResultBody<PageResult<SysUser>> getUserList(@RequestBody UserListParam param) {
        PageResult<SysUser> userList = userService.getUserList(param);
        return new ResultBody<>(userList);
    }

    /**
     * 获取用户信息
     */
    @GetMapping(value = "/info")
    @ResponseBody
    public ResultBody<UserInfoDto> getUserInfo(@RequestAttribute Long userId) {
        UserInfoDto userInfo = userService.getUserInfo(userId);
        return new ResultBody<>(userInfo);
    }

    /**
     * 按用户 ID 获取角色列表
     */
    @GetMapping("/role/{userId}")
    public ResultBody<List<SysRole>> getRoleListByUserId(@PathVariable Long userId) {
        List<SysRole> roleList = userService.getRoleListByUserId(userId);
        return new ResultBody<>(roleList);
    }

    /**
     * 更新角色
     */
    @PostMapping(value = "/role/update")
    @ResponseBody
    public ResultBody<Void> updateRole(@RequestParam("adminId") Long userId,
                                       @RequestParam("roleIds") List<Long> roleIds) {
        userService.updateRole(userId, roleIds);
        return new ResultBody<>();
    }

    /**
     * 更新用户
     */
    @PostMapping(value = "/update/{id}")
    @ResponseBody
    public ResultBody<Void> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        userService.updateById(user);
        return new ResultBody<>();
    }

    /**
     * 删除用户
     */
    @PostMapping(value = "/delete/{id}")
    @ResponseBody
    public ResultBody<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return new ResultBody<>();
    }

    /**
     * 更新用户状态
     */
    @PostMapping(value = "/updateStatus/{id}")
    @ResponseBody
    public ResultBody<Void> updateUserStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        SysUser sysUser = userService.getById(id);
        sysUser.setStatus(status);
        userService.updateById(sysUser);
        return new ResultBody<>();
    }

}
