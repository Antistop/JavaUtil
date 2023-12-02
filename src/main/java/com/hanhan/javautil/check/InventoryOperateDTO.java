package com.hanhan.javautil.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import static com.hanhan.javautil.check.CheckUtil.assertNotEmpty;

@Getter
@Setter
public abstract class InventoryOperateDTO<T extends InventoryOperateDetailDTO> implements ParamChecked {

    private static final long serialVersionUID = 1L;

    //主公共属性
    @ApiModelProperty("仓库编码")
    protected String whco;
    @ApiModelProperty("单号")
    protected String biid;

    @ApiModelProperty("库存操作明细")
    protected List<T> details;

    @Override
    public void check() {
        assertNotEmpty(whco, "仓库编码不能为空");
        assertNotEmpty(biid, "库存增加单号不能为空");
        assertNotEmpty(details, "库存操作明细不能为空");
        for (T detail : details) {
            detail.check();
        }
    }
}
