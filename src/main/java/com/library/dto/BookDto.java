package com.library.dto;

import com.library.entity.Book;

import java.time.format.DateTimeFormatter;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String createdAt;

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.category = book.getCategory();
        this.createdAt = book.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("BookDto{id=%d, title='%s', author='%s', isbn='%s', category='%s', createdAt=%s}", id, title, author, isbn, category, createdAt);
    }
}
