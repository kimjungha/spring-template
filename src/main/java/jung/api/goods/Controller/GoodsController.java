package jung.api.goods.Controller;

import jung.global.annotation.ApiTrackingCustom;
import jung.global.constants.ApiType;
import org.springframework.web.bind.annotation.GetMapping;


public class GoodsController {
    @GetMapping("/goods")
    @ApiTrackingCustom(type = ApiType.VIEW)
    public boolean findGoods() {
        return true;
    }
}
