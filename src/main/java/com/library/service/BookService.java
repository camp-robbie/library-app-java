package com.library.service;

import com.library.dto.BookDto;
import com.library.entity.Book;
import com.library.repository.BookRepository;

import java.util.List;
import java.util.Objects;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 도서 등록 작업
    public BookDto save(String title, String author, String isbn, String category) {
        // ISBN Duplicate Check
            // 전체 도서 조회
            // 순회하면서 현재 입력받은 isbn 값을 가진 책이 있는지 확인
        List<Book> bookList = bookRepository.findAll();

        boolean isDuplicate = false;
        for (Book book : bookList) {
            if(Objects.equals(book.getIsbn(), isbn)) {
                isDuplicate = true;
                break;
            }
        }

        if(isDuplicate) {
            throw new IllegalArgumentException("이미 등록된 ISBN : " + isbn);
        }

        // 신규 Book 객체 생성 및 등록
        Book book = new Book(title, author, isbn, category);
        Book savedBook = bookRepository.save(book);

        return new BookDto(savedBook);
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookDto::new)
                .toList();
    }

    public List<BookDto> findByKeyword(String keyword) {
        return bookRepository.findTitleContaining(keyword)
                .stream()
                .map(BookDto::new)
                .toList();
    }

    public List<BookDto> findByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(BookDto::new)
                .toList();
    }
}
