package com.library.server;

import com.library.dto.BookDto;

import java.util.List;

public class JsonUtils {

    public static String toJson(BookDto book) {
        return String.format(
                "{\"id\":%d,\"title\":\"%s\",\"author\":\"%s\",\"isbn\":\"%s\",\"category\":\"%s\",\"createdAt\":\"%s\"}",
                book.getId(),
                escapeJson(book.getTitle()),
                escapeJson(book.getAuthor()),
                escapeJson(book.getIsbn()),
                escapeJson(book.getCategory()),
                escapeJson(book.getCreatedAt())
        );
    }

    public static String toJson(List<BookDto> books) {
        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < books.size(); i++) {
            json.append(toJson(books.get(i)));
            if (i < books.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        return json.toString();
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // 간단한 JSON 파싱 (POST 요청용)
    public static BookRequest parseBookRequest(String json) {
        BookRequest request = new BookRequest();

        // 매우 간단한 JSON 파싱 (실제로는 Jackson 등 사용)
        String cleanJson = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = cleanJson.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "title" -> request.setTitle(value);
                    case "author" -> request.setAuthor(value);
                    case "isbn" -> request.setIsbn(value);
                    case "category" -> request.setCategory(value);
                }
            }
        }

        return request;
    }

    public static class BookRequest {
        private String title;
        private String author;
        private String isbn;
        private String category;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
}