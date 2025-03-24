package jung.api.erp.product.document;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)   // text 타입의 경우 문자들이 쪼개져서 검색 가능
    private String name;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Keyword) // 전체 문자열을 하나의 값으로 취급하는 필드 타입 (match 검색에서만 사용)
    private String category;

}
