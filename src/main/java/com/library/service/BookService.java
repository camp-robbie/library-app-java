package com.library.service;


import com.library.dto.BookDto;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

        if(bookRepository.findByIsbn(isbn).isPresent()) {
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
        return bookRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(BookDto::new)
                .toList();
    }

    public List<BookDto> findByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author)
                .stream()
                .map(BookDto::new)
                .toList();
    }

    public List<BookDto> findByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(BookDto::new)
                .toList();
    }

    public void deleteById(Long id) {
        // 삭제하려는 도서가 없는지 확인
        if(!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 도서입니다 : " + id);
        }
        bookRepository.deleteById(id);
    }
}
