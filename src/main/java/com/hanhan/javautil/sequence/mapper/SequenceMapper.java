package com.hanhan.javautil.sequence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanhan.javautil.sequence.entity.Sequence;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author pl
 */
public interface SequenceMapper extends BaseMapper<Sequence> {

    @Update("update n_sequence set seq_value=#{newValue} where seq_name=#{name} and seq_value=#{oldValue}")
    int nextValue(@Param("newValue") long newValue, @Param("oldValue") long oldValue, @Param("name") String name);
}
