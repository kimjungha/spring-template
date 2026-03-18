package jung.api.erp.product.service;

import jung.api.erp.product.document.ProductDocument;
import jung.api.erp.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;

    public List<ProductDocument> search(int price){
       log.debug("{} 상품 elasticSearch 진입",price);
        return productSearchRepository.findByPrice(price);
    }
}
