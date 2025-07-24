package com.library;

import com.library.controller.BookController;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.view.BookView;

import java.util.Scanner;

public class LibraryApplication {
    public static void main(String[] args) {
        System.out.println("== 순수 Java 도서 관리 APP ==");

        BookController controller = new BookController(new BookView(), new Scanner(System.in), new BookService(new BookRepository()));
        controller.start();

    }
}
