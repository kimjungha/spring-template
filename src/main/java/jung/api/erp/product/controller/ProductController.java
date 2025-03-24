package jung.api.erp.product.controller;

import jung.api.erp.product.document.ProductDocument;
import jung.api.erp.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductSearchService productSearchService;
    @GetMapping("/search")
    public List<ProductDocument> search(@RequestParam int price) {
        return productSearchService.search(price);
    }
}
