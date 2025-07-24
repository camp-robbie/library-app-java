package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {
    public static void main(String[] args) {
        System.out.println("== SpringBoot 도서 관리 APP ==");
        SpringApplication.run(LibraryApplication.class, args);
    }
}
