package com.hanhan.javautil.basedao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.collect.Lists;
import com.hanhan.javautil.exception.BizException;
import com.hanhan.javautil.exception.BizError;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseDAO {

    protected <T> void innerUpdate(BaseMapper<T> mapper, LambdaUpdateWrapper<T> wrapper, String message) {
        try {
            int i = mapper.update(null, wrapper);
            if (i < 1) {
                throw new BizException(BizError.OPTIMISTIC_LOCK_ERROR);
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(BizError.DATA_EXISTED, message);
        }
    }

    protected <T> void innerBatchInsert(BaseDAOMapper<T> mapper, List<T> entities, String message) {
        try {
            if (CollectionUtils.isEmpty(entities)) {
                return;
            }
            int i;
            if (entities.size() == 1) {
                i = mapper.insert(entities.get(0));
            } else {
                i = Lists.partition(entities, 50).stream().map(mapper::insertBatch).reduce(0, Integer::sum);
            }
            if (i != entities.size()) {
                throw new BizException(BizError.DB_EXCEPTION);
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(BizError.DATA_EXISTED, message);
        }
    }

    protected <T> void innerInsert(BaseMapper<T> mapper, T entity, String message) {
        try {
            int i = mapper.insert(entity);
            if (i != 1) {
                throw new BizException(BizError.DB_EXCEPTION);
            }
        } catch (DuplicateKeyException e) {
            throw new BizException(BizError.DATA_EXISTED, message);
        }
    }

    protected <T> void checkDuplicateData(BaseMapper<T> mapper, LambdaQueryWrapper<T> queryWrapper) {
        long isExistsCount = mapper.selectCount(queryWrapper);
        if (isExistsCount > 0) {
            throw new BizException(BizError.DB_DUPLICATE);
        }
    }

    protected <T> void checkDuplicateData(BaseMapper<T> mapper, LambdaQueryWrapper<T> queryWrapper, String message) {
        long isExistsCount = mapper.selectCount(queryWrapper);
        if (isExistsCount > 0) {
            throw new BizException(BizError.DATA_EXISTED, message);
        }
    }

    protected <T, E> void setBatchQuery(T single, Set<T> batch, LambdaQueryWrapper<E> wrapper, SFunction<E, T> function) {
        if (CollectionUtils.isEmpty(batch)) {
            if (isEmpty(single)) {
                return;
            }
            wrapper.eq(function, single);
        } else {
            if (isEmpty(single)) {
                if (batch.size() == 1) {
                    wrapper.eq(function, batch.iterator().next());
                } else {
                    wrapper.in(function, batch);
                }
            } else {
                Set<T> merge = new HashSet<>(batch);
                merge.add(single);
                wrapper.in(function, merge);
            }
        }
    }

    protected <T> boolean isEmpty(T data) {
        if (data == null) {
            return true;
        }
        if (data instanceof String) {
            return "".equals(data);
        }
        return false;
    }

}
