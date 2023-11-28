package com.hanhan.javautil.sequence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * @author pl
 */
@Data
@TableName("n_sequence")
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序列key
     */
    @TableField("seq_name")
    private String seqName;

    /**
     * 序列当前值
     */
    @TableField("seq_value")
    private Long seqValue;

}
