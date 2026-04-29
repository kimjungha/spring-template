package jung.api.erp.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import jung.api.erp.book.controller.response.BookReadResponse;
import jung.api.erp.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/erp/read/book")
public class BookReadController {

    private final BookService bookService;

    @PreAuthorize("hasRole('USER') and hasAuthority('READ')")
    @GetMapping("/{bookId}")
    @Operation(summary = "책 조회", description = "책 상세 내용을 조회하는 API 입니다")
    public BookReadResponse findBook(@PathVariable Long bookId) {
        return bookService.findBook(bookId);
    }
}

