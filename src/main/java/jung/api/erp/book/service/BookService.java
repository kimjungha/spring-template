package jung.api.erp.book.service;

import jung.api.erp.book.controller.response.BookReadResponse;
import jung.api.erp.book.domain.entity.Book;


public interface BookService {

    BookReadResponse findBook(Long bookId);
}
