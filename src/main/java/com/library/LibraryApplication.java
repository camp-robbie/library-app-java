package com.library;

import com.library.container.SimpleContainer;
import com.library.controller.BookController;
import com.library.service.BookService;

public class LibraryApplication {
    public static void main(String[] args) {
        System.out.println("== 순수 Java 도서 관리 APP ==");

        try {
            SimpleContainer container = new SimpleContainer();
            container.initialize();

            BookService bookService = container.getBean(BookService.class);
            addDummyData(bookService);

            BookController bookController = container.getBean(BookController.class);
            bookController.start();

        } catch (Exception e) {
            System.out.println("애플리케이션 실행 오류 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addDummyData(BookService bookService) {
        bookService.save("도서 관리 시스템 Java To Spring", "최원빈", "999-99-9999-999-9", "프로그래밍");
        bookService.save("자바의 정석", "남궁성", "978-89-7914-519-7", "프로그래밍");
        bookService.save("스프링 부트와 AWS로 혼자 구현하는 웹 서비스", "이동욱", "978-89-6626-235-6", "프로그래밍");
        bookService.save("클린 코드", "로버트 C. 마틴", "978-89-6626-618-7", "소프트웨어공학");
        bookService.save("스프링 맛보기 세션", "최원빈", "978-89-1234-567-8", "세션");
        bookService.save("객체지향의 사실과 오해", "조영호", "978-89-9602-721-9", "프로그래밍");
        System.out.println("더미 데이터 등록 완료");
    }
}
