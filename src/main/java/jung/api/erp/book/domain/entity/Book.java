package jung.api.erp.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "field", nullable = false, length = 20)
    private String field;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Override
    public String toString() {
        return "Book{" + "bookId='" + bookId + '\'' + "," +
            "field='" + field + '\'' + "," +
            " title='" + title + '\'' + '}';
    }
}
