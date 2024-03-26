package com.evolution.domain.system.model.dto;

import com.evolution.domain.system.model.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String icon;

    private List<SysMenu> menus;

    private List<String> roles;

    private String username;

}
