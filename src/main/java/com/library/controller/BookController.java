package com.library.controller;

import com.library.annotation.Controller;
import com.library.annotation.DeleteMapping;
import com.library.annotation.GetMapping;
import com.library.annotation.PostMapping;
import com.library.service.BookService;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/api/books")
    public void registerBook() {
        try {

        } catch (IllegalArgumentException e) {
        }
    }

    @GetMapping("/api/books")
    public void searchBooks() {
        try {
        } catch (Exception e) {
        }
    }

    @DeleteMapping("/api/books/{id}")
    public void deleteBook() {

    }

}
