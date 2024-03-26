package com.evolution.domain.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evolution.domain.system.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {

}
