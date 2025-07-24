package com.library.controller;

import com.library.server.HttpRequest;
import com.library.server.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FrontController {
    private final Map<String, Object> controllers = new HashMap<>();
    private final Map<String, Function<HttpRequest, HttpResponse>> handlerMappings = new HashMap<>();

    public void registerController(String name, Object controller) {
        controllers.put(name, controller);
        System.out.println("Controller 등록: " + name);
    }

    public void registerHandler(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        String key = method + " " + path;
        handlerMappings.put(key, handler);
        System.out.println("Handler 매핑: " + key);
    }

    public HttpResponse handleRequest(HttpRequest request) {
        try {
            // 1. Handler Mapping - 요청에 맞는 핸들러 찾기
            Function<HttpRequest, HttpResponse> handler = findHandler(request);

            if (handler != null) {
                // 2. Handler Execution - 핸들러 실행
                return handler.apply(request);
            } else {
                // 3. 매핑되지 않은 요청 처리
                return HttpResponse.notFound();
            }
        } catch (Exception e) {
            // 4. 글로벌 예외 처리
            System.err.println("FrontController 오류: " + e.getMessage());
            return HttpResponse.internalServerError(e.getMessage());
        }
    }

    private Function<HttpRequest, HttpResponse> findHandler(HttpRequest request) {
        String method = request.getMethod();
        String path = request.getCleanPath();

        // 정확한 매칭 시도
        String exactKey = method + " " + path;
        if (handlerMappings.containsKey(exactKey)) {
            return handlerMappings.get(exactKey);
        }

        // 패턴 매칭 시도 (간단한 구현)
        for (String mappingKey : handlerMappings.keySet()) {
            if (isPatternMatch(mappingKey, method, path)) {
                return handlerMappings.get(mappingKey);
            }
        }

        return null;
    }

    private boolean isPatternMatch(String mappingKey, String method, String path) {
        String[] parts = mappingKey.split(" ", 2);
        if (parts.length != 2) return false;

        String mappingMethod = parts[0];
        String mappingPath = parts[1];

        // 메서드가 다르면 false
        if (!mappingMethod.equals(method)) return false;

        // 경로 패턴 매칭 (간단한 {id} 패턴만 지원)
        if (mappingPath.contains("{")) {
            String pattern = mappingPath.replaceAll("\\{[^}]+\\}", "[^/]+");
            return path.matches(pattern);
        }

        return false;
    }

    public void printMappings() {
        System.out.println("\n등록된 Handler Mappings:");
        handlerMappings.keySet().forEach(key -> System.out.println("   " + key));
        System.out.println();
    }
}
