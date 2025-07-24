package com.library.controller;


import com.library.dto.BookCreateRequest;
import com.library.dto.BookDto;
import com.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> registerBook(
            @RequestBody BookCreateRequest request
    ) {
        BookDto bookDto = bookService.save(request.getTitle(), request.getAuthor(), request.getIsbn(), request.getCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto);
    }

    // ?keyword=
    @GetMapping
    public ResponseEntity<List<BookDto>> searchBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category
    ) {
        List<BookDto> bookDtoList;
        if(search != null && !search.trim().isEmpty()) {
            bookDtoList = bookService.findByKeyword(search);
        }else if(author != null && !author.trim().isEmpty()) {
            bookDtoList = bookService.findByAuthor(author);
        }else if(category != null && !category.trim().isEmpty()) {
            bookDtoList = bookService.findByCategory(category);
        } else {
            bookDtoList = bookService.findAll();
        }

        return ResponseEntity.ok(bookDtoList);
    }

    // 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
