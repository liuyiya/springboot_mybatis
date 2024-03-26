package com.evolution.domain.system.model.dto;

import com.evolution.domain.system.model.entity.SysMenu;
import lombok.Data;

import java.util.List;

@Data
public class SysMenuNode extends SysMenu {

    private List<SysMenu> children;

}
