package com.evolution.domain.system.model.param;

import com.evolution.types.request.PageParam;
import lombok.Data;

@Data
public class RoleListParam extends PageParam {

    private String keyword;

}
