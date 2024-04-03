package com.hanhan.javautil.basedao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

public interface BaseDAOMapper<T> extends BaseMapper<T> {
    int insertBatch(List<T> entities);

    int insertBatchWithKey(List<T> entities);
}
