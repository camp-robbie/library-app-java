package com.library.controller;

import com.library.annotation.Controller;
import com.library.annotation.DeleteMapping;
import com.library.annotation.GetMapping;
import com.library.annotation.PostMapping;
import com.library.dto.BookDto;
import com.library.server.HttpRequest;
import com.library.server.HttpResponse;
import com.library.server.JsonUtils;
import com.library.service.BookService;

import java.util.Map;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/api/books")
    public HttpResponse registerBook(HttpRequest request) {
        try {
            JsonUtils.BookRequest bookRequest = JsonUtils.parseBookRequest(request.getBody());

            BookDto bookDto = bookService.save(bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getIsbn(), bookRequest.getCategory());

            return HttpResponse.created(JsonUtils.toJson(bookDto));
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/api/books")
    public HttpResponse searchBooks(HttpRequest request) {
        try {
            Map<String, String> params = request.getQueryParams();

            if(params.containsKey("search")) {
                // 제목 검색
                return HttpResponse.ok(JsonUtils.toJson(
                        bookService.findByKeyword(params.get("search"))
                ));
            } else if(params.containsKey("author")) {
                // 저자 검색
                return HttpResponse.ok(JsonUtils.toJson(
                        bookService.findByAuthor(params.get("author"))
                ));
            } else if(params.containsKey("category")) {
                // 카테고리 검색
                return HttpResponse.ok(JsonUtils.toJson(
                        bookService.findByCategory(params.get("category"))
                ));
            } else {
                // 전체 조회
                return HttpResponse.ok(JsonUtils.toJson(
                        bookService.findAll()
                ));
            }

        } catch (Exception e) {
            return HttpResponse.badRequest(e.getMessage());
        }
    }

    @DeleteMapping("/api/books/{id}")
    public HttpResponse deleteBook(HttpRequest request) {
        try {
            String path = request.getPath();
            Long id = Long.parseLong(path.substring("/api/books/".length()));

            bookService.deleteById(id);
            return HttpResponse.ok(id + "번의 도서를 정상적으로 삭제했습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
