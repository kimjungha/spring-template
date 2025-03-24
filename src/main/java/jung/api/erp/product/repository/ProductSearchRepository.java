package jung.api.erp.product.repository;

import jung.api.erp.product.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument,String> {

    List<ProductDocument> findByNameContaining(String keyword); // 자동쿼리생성
    List<ProductDocument> findByPrice(int price);
}
