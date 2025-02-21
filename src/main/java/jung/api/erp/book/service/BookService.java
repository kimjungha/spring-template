package jung.api.erp.book.service;

import jung.api.erp.book.controller.response.BookReadResponse;


public interface BookService {

    BookReadResponse findBook(Long bookId);
}
