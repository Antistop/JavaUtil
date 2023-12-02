package com.hanhan.javautil.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import static com.hanhan.javautil.check.CheckUtil.assertNotEmpty;

@Getter
@Setter
public class InventoryAddDTO extends InventoryOperateDTO<InventoryAddDetailDTO> {
    private static final long serialVersionUID = 1L;

    //主明细
    @ApiModelProperty("货主编码")
    private String owco;

    @Override
    public void check() {
        super.check();
        assertNotEmpty(owco, "货主编码不能为空");
        for (InventoryAddDetailDTO detail : details) {
            assertNotEmpty(detail.getWhid(), "库位编码不能为空");
        }
    }
}
