package com.library.container;

import com.library.annotation.Controller;
import com.library.annotation.Repository;
import com.library.annotation.Service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimpleContainer {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void initialize() {
        try {
            Scanner scanner = new Scanner(System.in);
            beans.put(Scanner.class, scanner);

            // Service bean 생성
            createBean("com.library.service.BookService");

            // Repository bean 생성
            createBean("com.library.repository.BookRepository");

            // View bean 생성
            createView("com.library.view.BookView");

            // Controller bean 생성
            createBean("com.library.controller.BookController");

        } catch (Exception e) {
            throw new RuntimeException("Container 초기화 실패", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    private void createBean(String className) throws Exception {
        Class<?> clazz = Class.forName(className);

        if (!clazz.isAnnotationPresent(Repository.class) &&
                !clazz.isAnnotationPresent(Service.class) &&
                !clazz.isAnnotationPresent(Controller.class)) {
            return;
        }

        // 생성자 찾기
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];

        // 의존성 주입
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            dependencies[i] = beans.get(parameterTypes[i]);
            if(dependencies[i] == null) {
                throw new RuntimeException("의존성을 찾을 수 없습니다.: " + parameterTypes[i]);
            }
        }

        // 인스턴스 생성 및 등록
        Object instance = constructor.newInstance(dependencies);
        beans.put(clazz, instance);
    }

    private void createView(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        beans.put(clazz, instance);
        System.out.println("View 새성: " + clazz.getSimpleName());
    }
}
