package jung.api.erp.book.service;

import jung.api.erp.book.controller.response.BookReadResponse;
import jung.api.erp.book.domain.entity.Book;
import jung.api.erp.book.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Cacheable(value = "BOOKSTORE", key = "#bookId")
    @Transactional(readOnly = true)  //단순 read 이기 때문에 read DB 바라 보게 함
    public BookReadResponse findBook(Long bookId) {

      return bookRepository.findById(bookId)
          .map(BookReadResponse::toModel)
          .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "찾는 책이 없습니다."));

    }
}
