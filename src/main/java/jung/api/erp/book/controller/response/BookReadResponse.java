package jung.api.erp.book.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jung.api.erp.book.domain.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookReadResponse {
    @Schema(description = "책 분야")  // swagger 문서에 사용되는 애노테이션
    private String field;

    @Schema(description = "책 제목")
    private String title;

    public static BookReadResponse toModel(Book book){
        return BookReadResponse.builder()
            .field(book.getField())
            .title(book.getTitle())
            .build();
    }
}
