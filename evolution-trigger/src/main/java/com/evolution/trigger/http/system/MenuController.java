package com.evolution.trigger.http.system;

import com.evolution.domain.system.model.dto.SysMenuNode;
import com.evolution.domain.system.model.entity.SysMenu;
import com.evolution.domain.system.service.MenuService;
import com.evolution.types.reponse.PageResult;
import com.evolution.types.reponse.ResultBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu/")
public class MenuController {

    private final MenuService menuService;

    /**
     * 获取树菜单
     */
    @GetMapping("/treeList")
    public ResultBody<List<SysMenuNode>> getTreeMenu() {
        return new ResultBody<>(menuService.getTreeMenu());
    }

    /**
     * 列表
     */
    @GetMapping(value = "/list/{parentId}")
    public ResultBody<PageResult<SysMenu>> list(@PathVariable Long parentId,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageResult<SysMenu> menuPageList = menuService.getMenuPageList(parentId, pageSize, pageNum);
        return new ResultBody<>(menuPageList);
    }

    /**
     * 添加
     */
    @PostMapping(value = "/create")
    public ResultBody<Void> create(@RequestBody SysMenu sysMenu) {
        menuService.addMenu(sysMenu);
        return new ResultBody<>();

    }

    /**
     * 更新
     */
    @PostMapping(value = "/update/{id}")
    public ResultBody<Void> update(@PathVariable Long id, @RequestBody SysMenu umsMenu) {
        umsMenu.setId(id);
        menuService.updateById(umsMenu);
        return new ResultBody<>();
    }

    /**
     * 删除
     */
    @PostMapping(value = "/delete/{id}")
    public ResultBody<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return new ResultBody<>();
    }

}
