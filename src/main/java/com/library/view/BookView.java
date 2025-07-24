package com.library.view;

import com.library.dto.BookDto;

import java.util.List;

public class BookView {
    public void showMainMenu() {
        System.out.println("\n=== 도서 관리 시스템 ===");
        System.out.println("1. 도서 등록");
        System.out.println("2. 전체 도서 조회");
        System.out.println("3. 도서 검색");
        System.out.println("4. 작가별 도서 조회");
        System.out.println("5. 카테고리별 도서 조회");
        System.out.println("6. 도서 삭제");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }

    public void promptForTitle() {
        System.out.print("도서 제목: ");
    }

    public void promptForAuthor() {
        System.out.print("작가명: ");
    }

    public void promptForIsbn() {
        System.out.print("ISBN: ");
    }

    public void promptForCategory() {
        System.out.print("카테고리: ");
    }

    public void promptForKeyword() {
        System.out.print("검색어 (제목): ");
    }

    public void promptForAuthorSearch() {
        System.out.print("작가명: ");
    }

    public void promptForCategorySearch() {
        System.out.print("카테고리: ");
    }

    public void promptForBookId() {
        System.out.print("삭제할 도서 ID: ");
    }

    public void showBookRegistrationSuccess(BookDto book) {
        System.out.println("도서가 등록되었습니다!");
        System.out.println("   " + book);
    }

    public void showBooks(List<BookDto> books, String title) {
        System.out.println("\n=== " + title + " ===");
        if (books.isEmpty()) {
            System.out.println("등록된 도서가 없습니다.");
        } else {
            books.forEach(book -> System.out.println("   " + book));
            System.out.println("총 " + books.size() + "권");
        }
    }

    public void showError(String message) {
        System.err.println(message);
    }

    public void showSuccess(String message) {
        System.out.println(message);
    }

    public void showGoodbye() {
        System.out.println("\n도서 관리 시스템을 종료합니다. 감사합니다!");
    }
}
