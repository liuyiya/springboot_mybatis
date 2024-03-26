package com.evolution.trigger.http.system;

import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.model.entity.SysRole;
import com.evolution.domain.system.model.param.RoleListParam;
import com.evolution.domain.system.service.RoleService;
import com.evolution.types.reponse.PageResult;
import com.evolution.types.reponse.ResultBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/role/")
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表
     *
     * @param param 页面参数
     * @return {@link ResultBody}<{@link PageResult}<{@link SysRole}>>
     */
    @PostMapping("/list")
    public ResultBody<PageResult<SysRole>> getRoleList(@RequestBody RoleListParam param) {
        PageResult<SysRole> roleList = roleService.getRoleList(param);
        return new ResultBody<>(roleList);
    }

    /**
     * 获取所有菜单列表
     */
    @GetMapping("/listAll")
    public ResultBody<List<SysRole>> getAllMenuList() {
        List<SysRole> roleList = roleService.getAllRoleList();
        return new ResultBody<>(roleList);
    }

    /**
     * 创建角色
     */
    @PostMapping("/create")
    public ResultBody<Void> createRole(@RequestBody SysRole sysRole) {
        roleService.addRole(sysRole);
        return new ResultBody<>();
    }

    /**
     * 更新角色
     */
    @PostMapping("/update/{id}")
    public ResultBody<Void> updateRole(@PathVariable Long id, @RequestBody SysRole sysRole) {
        sysRole.setId(id);
        roleService.updateById(sysRole);
        return new ResultBody<>();
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete")
    public ResultBody<Void> deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return new ResultBody<>();
    }

    /**
     * “分配”菜单
     */
    @PostMapping("/assignMenu")
    public ResultBody<Void> assignMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        roleService.assignMenu(roleId, menuIds);
        return new ResultBody<>();
    }

    /**
     * 列表菜单
     */
    @GetMapping(value = "/listMenu/{roleId}")
    public ResultBody<List<SysMenu>> listMenu(@PathVariable Long roleId) {

        List<SysMenu> roleList = roleService.listMenu(roleId);
        return new ResultBody<>(roleList);
    }

    /**
     * alloc 菜单
     */
    @PostMapping(value = "/allocMenu")
    public ResultBody<Void> allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        roleService.allocMenu(roleId, menuIds);
        return new ResultBody<>();
    }


}
