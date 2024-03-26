package com.evolution.types.reponse;

import com.evolution.types.request.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageResult<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer total;

    private List<T> list;

    public PageResult<T> initPage(PageParam pageParam, List<T> pageList) {
        this.pageNum = pageParam.getPageNum();
        this.pageSize = pageParam.getPageSize();
        this.total = pageList.size();
        this.list = pageList;
        return this;
    }

}
