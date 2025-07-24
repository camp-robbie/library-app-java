package com.library.dto;

import lombok.Getter;

@Getter
public class BookCreateRequest {
    private String title;
    private String author;
    private String isbn;
    private String category;
}
