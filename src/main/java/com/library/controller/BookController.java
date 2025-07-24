package com.library.controller;

import com.library.dto.BookDto;
import com.library.service.BookService;
import com.library.view.BookView;

import java.util.List;
import java.util.Scanner;

public class BookController {
    private final BookView view;
    private final Scanner scanner;
    private final BookService bookService;

    public BookController(BookView view, Scanner scanner, BookService bookService) {
        this.view = view;
        this.scanner = scanner;
        this.bookService = bookService;
    }

    public void start() {
        while(true) {
            try {
                view.showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> registerBook();
                    case 2 -> showAllBooks();
                    case 3 -> searchBooks();
//                    case 4 -> findBooksByAuthor();
//                    case 5 -> findBooksByCategory();
//                    case 6 -> deleteBook();
                    case 0 -> {
                        view.showGoodbye();
                        return;
                    }
                    default -> view.showError("잘못된 선택입니다. 다시 선택해주세요.");
                }
            } catch (Exception e) {
                view.showError(e.getMessage());
                scanner.nextLine();
            }

        }
    }

    private void registerBook() {
        try {
            view.promptForTitle();
            String title = scanner.nextLine();
            if(title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("도서 제목은 필수다.");
            }

            view.promptForAuthor();
            String author = scanner.nextLine();
            if(author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("도서 제목은 필수다.");
            }

            view.promptForIsbn();
            String isbn = scanner.nextLine();
            if(isbn == null || isbn.trim().isEmpty()) {
                throw new IllegalArgumentException("도서 제목은 필수다.");
            }

            view.promptForCategory();
            String category = scanner.nextLine();
            if(category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("도서 제목은 필수다.");
            }

            // 책을 등록
            BookDto bookDto = bookService.save(title, author, isbn, category);
            view.showBookRegistrationSuccess(bookDto);
        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    private void showAllBooks() {
        List<BookDto> bookList = bookService.findAll();
        view.showBooks(bookList, "전체 도서 목록");
    }

    private void searchBooks() {
        try {
            view.promptForKeyword();
            String keyword = scanner.nextLine();

            List<BookDto> bookList = bookService.findByKeyword(keyword);
            view.showBooks(bookList, "검색 결과");
        } catch (Exception e) {
            view.showError(e.getMessage());
        }
    }

}
