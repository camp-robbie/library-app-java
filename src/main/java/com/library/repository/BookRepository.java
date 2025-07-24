package com.library.repository;


import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Object> findByIsbn(String isbn);
    List<Book> findByTitleContainingIgnoreCase(String keyword);
    List<Book> findByAuthorIgnoreCase(String author);
    List<Book> findByCategoryIgnoreCase(String category);
}
