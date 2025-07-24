package com.library.repository;

import com.library.entity.Book;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class BookRepository {
    private static final Map<Long, Book> BOOK_STORE = new HashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public List<Book> findAll() {
        return new ArrayList<>(BOOK_STORE.values());
    }

    public Book save(Book book) {
        if(book.getId() == null) {
            book.setId(ID_GENERATOR.getAndIncrement());
        }
        BOOK_STORE.put(book.getId(), book);
        return book;
    }

    public List<Book> findTitleContaining(String keyword) {
        return BOOK_STORE.values().stream()
                .filter(book -> book.getTitle() != null && book.getTitle().contains(keyword))
                .toList();
    }

    public List<Book> findByAuthor(String author) {
        return BOOK_STORE.values().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }
}
