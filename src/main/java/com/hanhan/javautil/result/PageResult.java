package com.hanhan.javautil.result;

import com.hanhan.javautil.exception.Errors;
import lombok.Getter;
import java.util.List;

@Getter
public class PageResult<T> extends Result<List<T>> {
    private long count;
    private long totalPage;
    private long currentPage;
    private long pageSize;
    public PageResult(List<T> data, long count, long totalPage, long currentPage, long pageSize) {
        super(data);
        this.count = count;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    public PageResult(Errors errors) {
        super(errors);
    }

    public PageResult(Errors errors, String... arg) {
        super(errors, arg);
    }

}
