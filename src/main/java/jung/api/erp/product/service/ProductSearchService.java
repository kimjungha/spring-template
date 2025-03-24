package jung.api.erp.product.service;

import jung.api.erp.product.document.ProductDocument;
import jung.api.erp.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;

    public List<ProductDocument> search(int price){
        System.out.println("엘라스틱 상품 서비스 진입"+price);
        return productSearchRepository.findByPrice(price);
    }
}
