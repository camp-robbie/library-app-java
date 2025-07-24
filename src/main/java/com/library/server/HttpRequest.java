package com.library.server;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(String rawRequest) {
        parseRequest(rawRequest);
    }

    private void parseRequest(String rawRequest) {
        String[] lines = rawRequest.split("\r\n");

        // 첫 번째 줄: GET /books HTTP/1.1
        String[] requestLine = lines[0].split(" ");
        this.method = requestLine[0];
        this.path = requestLine[1];
        this.version = requestLine[2];

        // 헤더 파싱
        this.headers = new HashMap<>();
        int i = 1;
        while (i < lines.length && !lines[i].isEmpty()) {
            String[] header = lines[i].split(": ", 2);
            if (header.length == 2) {
                headers.put(header[0].toLowerCase(), header[1]);
            }
            i++;
        }

        // Body 파싱 (POST 요청용)
        if (i < lines.length - 1) {
            StringBuilder bodyBuilder = new StringBuilder();
            for (int j = i + 1; j < lines.length; j++) {
                bodyBuilder.append(lines[j]);
                if (j < lines.length - 1) {
                    bodyBuilder.append("\r\n");
                }
            }
            this.body = bodyBuilder.toString();
        }
    }

    // Getters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }

    // 쿼리 파라미터 파싱 (한글 디코딩 추가)
    public Map<String, String> getQueryParams() {
        Map<String, String> params = new HashMap<>();
        if (path.contains("?")) {
            String[] parts = path.split("\\?", 2);
            String query = parts[1];
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        // URL 디코딩으로 한글 처리
                        String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                        params.put(key, value);
                    } catch (java.io.UnsupportedEncodingException e) {
                        // 디코딩 실패 시 원본 사용
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return params;
    }

    // 경로에서 쿼리 파라미터 제거
    public String getCleanPath() {
        return path.contains("?") ? path.split("\\?")[0] : path;
    }
}