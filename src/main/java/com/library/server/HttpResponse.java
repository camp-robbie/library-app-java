package com.library.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    public int statusCode;
    public String statusMessage;
    public Map<String, String> headers;
    public String body;

    public HttpResponse() {
        this.headers = new HashMap<>();
        // 기본 헤더 설정
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
    }

    public static HttpResponse ok(String body) {
        HttpResponse response = new HttpResponse();
        response.statusCode = 200;
        response.statusMessage = "OK";
        response.body = body;
        return response;
    }

    public static HttpResponse created(String body) {
        HttpResponse response = new HttpResponse();
        response.statusCode = 201;
        response.statusMessage = "Created";
        response.body = body;
        return response;
    }

    public static HttpResponse badRequest(String message) {
        HttpResponse response = new HttpResponse();
        response.statusCode = 400;
        response.statusMessage = "Bad Request";
        response.body = "{\"error\": \"" + message + "\"}";
        return response;
    }

    public static HttpResponse notFound() {
        HttpResponse response = new HttpResponse();
        response.statusCode = 404;
        response.statusMessage = "Not Found";
        response.body = "{\"error\": \"Not Found\"}";
        return response;
    }

    public static HttpResponse internalServerError(String message) {
        HttpResponse response = new HttpResponse();
        response.statusCode = 500;
        response.statusMessage = "Internal Server Error";
        response.body = "{\"error\": \"" + message + "\"}";
        return response;
    }

    public String toHttpString() {
        StringBuilder response = new StringBuilder();

        // 상태 라인
        response.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(statusMessage)
                .append("\r\n");

        // Content-Length 자동 계산
        if (body != null) {
            headers.put("Content-Length", String.valueOf(body.getBytes().length));
        }

        // 헤더
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }

        // 빈 줄
        response.append("\r\n");

        // 바디
        if (body != null) {
            response.append(body);
        }

        return response.toString();
    }
}