package com.library;

import com.library.annotation.DeleteMapping;
import com.library.annotation.GetMapping;
import com.library.annotation.PostMapping;
import com.library.container.SimpleContainer;
import com.library.controller.FrontController;
import com.library.http.HttpServer;
import com.library.server.HttpRequest;
import com.library.server.HttpResponse;
import com.library.service.BookService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

public class LibraryApplication {
    public static void main(String[] args) {
        System.out.println("== HTTP 서버 Java 도서 관리 APP ==");

        try {
            // 의존성 주입
            SimpleContainer container = new SimpleContainer();
            container.initialize();

            // 더미 데이터 생성
            BookService bookService = container.getBean(BookService.class);
            addDummyData(bookService);

            // FrontController 생성
            FrontController frontController = setupFrontController(container);

            // HTTP 서버 시작
            HttpServer httpServer = new HttpServer(8088, frontController);

            // 종류 훅 설정
            Runtime.getRuntime().addShutdownHook(new Thread(httpServer::stop));

            httpServer.start();

        } catch (Exception e) {
            System.out.println("애플리케이션 실행 오류 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static FrontController setupFrontController(SimpleContainer container) {
        FrontController frontController = new FrontController();

        // Controller 등록
        Map<String, Object> controllerBeans = container.getControllerMap();
        controllerBeans.forEach(frontController::registerController);

        // HandlerMapping 등록
        for (Object controller : controllerBeans.values()) {
            for (Method method : controller.getClass().getMethods()) {
                Function<HttpRequest, HttpResponse> handler = (request) -> {
                    try {
                        return (HttpResponse) method.invoke(controller, request);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                };

                // 메서드 확인해서 경로 정보 가와서 등록
                if(method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = method.getAnnotation(GetMapping.class);
                    frontController.registerHandler("GET", annotation.value(), handler);
                } else if(method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping annotation = method.getAnnotation(PostMapping.class);
                    frontController.registerHandler("POST", annotation.value(), handler);
                } else if(method.isAnnotationPresent(DeleteMapping.class)) {
                    DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
                    frontController.registerHandler("DELETE", annotation.value(), handler);
                }

            }
        }

        return frontController;
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
