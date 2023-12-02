package com.hanhan.javautil.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import static com.hanhan.javautil.check.CheckUtil.assertNotEmpty;

public abstract class InventoryOperateDetailDTO implements ParamChecked {
    private static final long serialVersionUID = 1L;

    //明细公共属性
    @Setter
    @Getter
    @ApiModelProperty("单据行号")
    private String rwid;

    @Override
    public void check() {
        assertNotEmpty(rwid, "明细行号不能为空");
    }
}
