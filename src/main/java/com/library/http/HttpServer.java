package com.library.http;

import com.library.controller.FrontController;
import com.library.server.HttpRequest;
import com.library.server.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private final FrontController frontController;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;

    public HttpServer(int port, FrontController frontController) {
        this.port = port;
        this.frontController = frontController;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("HTTP 서버가 시작되었습니다!");
            System.out.println("브라우저에서 http://localhost:" + port + " 접속");
            System.out.println("API 테스트: http://localhost:" + port + "/api/books");
            System.out.println();

            // Handler Mappings 출력
            frontController.printMappings();

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("서버 오류: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true)) {

            // HTTP 요청 읽기
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            int contentLength = 0;

            // 헤더 읽기
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            requestBuilder.append("\r\n");

            // Body 읽기 (POST 요청의 경우)
            if (contentLength > 0) {
                char[] body = new char[contentLength];
                in.read(body, 0, contentLength);
                requestBuilder.append(body);
            }

            String rawRequest = requestBuilder.toString();

            // 요청 로깅 (한글도 제대로 표시)
            String[] requestLines = rawRequest.split("\r\n");
            if (requestLines.length > 0) {
                System.out.println("[요청] " + requestLines[0]);
            }

            // HTTP 요청 파싱 및 FrontController로 처리
            HttpRequest request = new HttpRequest(rawRequest);
            HttpResponse response = frontController.handleRequest(request);

            // 응답 전송
            out.print(response.toHttpString());
            out.flush();

        } catch (IOException e) {
            System.err.println("클라이언트 처리 오류: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // 무시
            }
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            System.out.println("서버가 종료되었습니다.");
        } catch (IOException e) {
            System.err.println("서버 종료 오류: " + e.getMessage());
        }
    }
}
