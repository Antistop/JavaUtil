package com.hanhan.javautil.check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAddDetailDTO extends InventoryOperateDetailDTO {

    private static final long serialVersionUID = 1L;

    //明细数据
    @ApiModelProperty("库位编码")
    private String whid;

}
