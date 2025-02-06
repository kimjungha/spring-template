package jung.api.OMS.goods.controller;

import jung.global.annotation.ApiTrackingCustom;
import jung.global.constants.ApiType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @GetMapping("/goods")
    @ApiTrackingCustom(type = ApiType.VIEW)
    public boolean findGoods() {
        return true;
    }
}
