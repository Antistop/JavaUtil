package com.hanhan.javautil.sequence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hanhan.javautil.sequence.entity.Sequence;
import com.hanhan.javautil.sequence.mapper.SequenceMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author pl
 * 单机版Leaf号段式
 */
@Component
public class SequenceManager {

    @Getter
    @Setter
    private class SequenceHolder {
        private String name;
        private AtomicLong value;
        private long dbValue;
        private long step;

        public SequenceHolder(long step) {
            this.step = step;
        }

        public long next() {
            if (value == null) {
                init();
            }
            long seq = value.incrementAndGet();
            if (seq > step) {
                nextRound();
                return next();
            } else {
                return dbValue + seq;
            }
        }

        private synchronized void init() {
            if (value != null) {
                return;
            }
            dbValue = nextValue(name, step) - step;
            value = new AtomicLong(0);
        }

        private synchronized void nextRound() {
            if (value.get() > step) {
                dbValue = nextValue(name, step) - step;
                value.set(0);
            }
        }
    }

    @Resource
    private SequenceMapper sequenceMapper;
    private static final Map<String, SequenceHolder> holder = new HashMap<>();

    private Sequence querySequence(String sequenceName) {
        LambdaQueryWrapper<Sequence> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sequence::getSeqName, sequenceName);
        Sequence sequence = sequenceMapper.selectOne(wrapper);
        if (sequence == null) {
            insertSequence(sequenceName);
        }
        return sequenceMapper.selectOne(wrapper);
    }

    private void insertSequence(String sequenceName) {
        Sequence sequence = new Sequence();
        sequence.setSeqName(sequenceName);
        sequence.setSeqValue(0L);
        try {
            sequenceMapper.insert(sequence);
            //忽略并发主键冲突
        } catch (DuplicateKeyException ignore) {
        }
    }

    private long nextValue(String sequenceName, long step) {
        for (int i = 0; i < 10; i++) {
            Sequence sequence = querySequence(sequenceName);
            int update = sequenceMapper.nextValue(sequence.getSeqValue() + step, sequence.getSeqValue(), sequenceName);
            if (update == 1) {
                return sequence.getSeqValue() + step;
            }
        }
        throw new RuntimeException();
    }

    /**
     * 用于生成业务流水号
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long next(String sequenceName) {
        SequenceHolder sequenceHolder = holder.get(sequenceName);
        if (sequenceHolder == null) {
            synchronized (holder) {
                sequenceHolder = holder.get(sequenceName);
                if (sequenceHolder == null) {
                    sequenceHolder = new SequenceHolder(1);
                    sequenceHolder.setName(sequenceName);
                    sequenceHolder.init();
                    holder.put(sequenceName, sequenceHolder);
                }
            }
        }
        return sequenceHolder.next();
    }

    /**
     * 用于生成数据库ID
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long nextDbId(String sequenceName) {
        SequenceHolder sequenceHolder = holder.get(sequenceName);
        if (sequenceHolder == null) {
            synchronized (holder) {
                sequenceHolder = holder.get(sequenceName);
                if (sequenceHolder == null) {
                    sequenceHolder = new SequenceHolder(1000);
                    sequenceHolder.setName(sequenceName);
                    sequenceHolder.init();
                    holder.put(sequenceName, sequenceHolder);
                }
            }
        }
        return sequenceHolder.next();
    }

    public static void main(String[] args) {
        new SequenceManager().nextDbId(SequenceKey.LOCATION_STOCK_TABLE_ID);
    }

}
