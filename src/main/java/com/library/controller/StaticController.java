package com.library.controller;

import com.library.annotation.Controller;
import com.library.annotation.GetMapping;
import com.library.server.HttpRequest;
import com.library.server.HttpResponse;

@Controller
public class StaticController {

    // GET /- 메인 페이지
    @GetMapping("/")
    public HttpResponse getIndexPage(HttpRequest request) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>도서 관리 시스템</title>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; background: #f5f5f5; }
                    .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .book { border: 1px solid #ddd; margin: 10px 0; padding: 15px; border-radius: 5px; background: #fafafa; }
                    button { background: #007bff; color: white; border: none; padding: 10px 15px; border-radius: 3px; cursor: pointer; margin: 5px; }
                    button:hover { background: #0056b3; }
                    .delete-btn { background: #dc3545; }
                    .delete-btn:hover { background: #c82333; }
                    input { padding: 8px; margin: 5px; border: 1px solid #ddd; border-radius: 3px; min-width: 150px; }
                    .form-group { margin: 15px 0; }
                    h1 { color: #333; text-align: center; }
                    h2 { color: #555; border-bottom: 2px solid #007bff; padding-bottom: 5px; }
                    .status { text-align: center; margin: 20px 0; font-style: italic; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>도서 관리 시스템</h1>
        
                    <h2>도서 검색</h2>
                    <div class="form-group">
                        <div style="margin-bottom: 10px;">
                            <label style="display: inline-block; width: 80px; font-weight: bold;">검색 타입:</label>
                            <select id="searchType" style="padding: 8px; margin: 5px; border: 1px solid #ddd; border-radius: 3px;">
                                <option value="title">제목</option>
                                <option value="author">작가</option>
                                <option value="category">카테고리</option>
                            </select>
                        </div>
                        <div>
                            <input type="text" id="searchInput" placeholder="검색어를 입력하세요" style="width: 200px;">
                            <button onclick="searchBooks()">검색</button>
                            <button onclick="loadAllBooks()">전체 조회</button>
                        </div>
                        <div style="margin-top: 10px; font-size: 0.9em; color: #666;">
                            <strong>검색 예시:</strong> 
                            제목: "자바", 작가: "남궁성", 카테고리: "프로그래밍"
                        </div>
                    </div>
                    
                    <h2>도서 등록</h2>
                    <div class="form-group">
                        <input type="text" id="title" placeholder="제목" required>
                        <input type="text" id="author" placeholder="작가" required><br>
                        <input type="text" id="isbn" placeholder="ISBN" required>
                        <input type="text" id="category" placeholder="카테고리" required><br>
                        <button onclick="addBook()">등록</button>
                    </div>
                    
                    <h2>도서 목록</h2>
                    <div id="bookList">
                        <p class="status">로딩 중...</p>
                    </div>
                </div>
                
                <script>
                    async function loadAllBooks() {
                        try {
                            const response = await fetch('/api/books');
                            const books = await response.json();
                            displayBooks(books);
                        } catch (error) {
                            document.getElementById('bookList').innerHTML = '<p style="color: red;">도서 목록 로드 실패: ' + error.message + '</p>';
                        }
                    }
                    
                    async function searchBooks() {
                        const keyword = document.getElementById('searchInput').value;
                        const searchType = document.getElementById('searchType').value;
                        
                        if (!keyword.trim()) {
                            await loadAllBooks();
                            return;
                        }
                        
                        try {
                            // 검색 타입에 따라 다른 파라미터 사용
                            const encodedKeyword = encodeURIComponent(keyword);
                            let url = '';
                            
                            switch(searchType) {
                                case 'title':
                                    url = `/api/books?search=${encodedKeyword}`;
                                    break;
                                case 'author':
                                    url = `/api/books?author=${encodedKeyword}`;
                                    break;
                                case 'category':
                                    url = `/api/books?category=${encodedKeyword}`;
                                    break;
                                default:
                                    url = `/api/books?search=${encodedKeyword}`;
                            }
                            
                            console.log('검색 타입:', searchType, '검색어:', keyword, 'URL:', url);
                            
                            const response = await fetch(url);
                            const books = await response.json();
                            
                            // 검색 타입에 따른 결과 메시지
                            const typeNames = {
                                'title': '제목',
                                'author': '작가',
                                'category': '카테고리'
                            };
                            
                            displayBooks(books, `${typeNames[searchType]} 검색: "${keyword}"`);
                            
                            console.log('검색 결과:', books.length + '권');
                        } catch (error) {
                            console.error('검색 오류:', error);
                            alert('검색 실패: ' + error.message);
                        }
                    }
                    
                    async function addBook() {
                        const title = document.getElementById('title').value;
                        const author = document.getElementById('author').value;
                        const isbn = document.getElementById('isbn').value;
                        const category = document.getElementById('category').value;
                        
                        if (!title || !author || !isbn || !category) {
                            alert('모든 필드를 입력해주세요');
                            return;
                        }
                        
                        try {
                            const response = await fetch('/api/books', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ title, author, isbn, category })
                            });
                            
                            if (response.ok) {
                                alert('도서가 등록되었습니다!');
                                document.getElementById('title').value = '';
                                document.getElementById('author').value = '';
                                document.getElementById('isbn').value = '';
                                document.getElementById('category').value = '';
                                loadAllBooks();
                            } else {
                                const error = await response.json();
                                alert('등록 실패: ' + error.error);
                            }
                        } catch (error) {
                            alert('등록 실패: ' + error.message);
                        }
                    }
                    
                    function displayBooks(books, headerText = "도서 목록") {
                        const bookList = document.getElementById('bookList');
                        
                        if (books.length === 0) {
                            bookList.innerHTML = `
                                <div style="text-align: center; margin: 20px 0;">
                                    <h3>${headerText}</h3>
                                    <p class="status">검색 결과가 없습니다.</p>
                                </div>`;
                            return;
                        }
                        
                        bookList.innerHTML = `
                            <div style="text-align: center; margin-bottom: 20px;">
                                <h3>${headerText}</h3>
                                <p class="status">총 ${books.length}권의 도서를 찾았습니다.</p>
                            </div>
                        ` + books.map(book => `
                            <div class="book">
                                <h3>${book.title}</h3>
                                <p><strong>작가:</strong> ${book.author}</p>
                                <p><strong>ISBN:</strong> ${book.isbn}</p>
                                <p><strong>카테고리:</strong> ${book.category}</p>
                                <p><strong>등록일:</strong> ${book.createdAt}</p>
                                <button class="delete-btn" onclick="deleteBook(${book.id})">삭제</button>
                            </div>
                        `).join('');
                    }
                    
                    async function deleteBook(id) {
                        if (!confirm('정말 삭제하시겠습니까?')) return;
                        
                        try {
                            const response = await fetch(`/api/books/${id}`, { method: 'DELETE' });
                            if (response.ok) {
                                alert('도서가 삭제되었습니다');
                                loadAllBooks();
                            } else {
                                const error = await response.json();
                                alert('삭제 실패: ' + error.error);
                            }
                        } catch (error) {
                            alert('삭제 실패: ' + error.message);
                        }
                    }
                    
                    // 검색 타입 변경 시 플레이스홀더 업데이트
                    document.getElementById('searchType').addEventListener('change', function() {
                        const searchInput = document.getElementById('searchInput');
                        const placeholders = {
                            'title': '예: 자바의 정석, 스프링 부트',
                            'author': '예: 최원빈, 남궁성, 이동욱',
                            'category': '예: 프로그래밍, 세션'
                        };
                        searchInput.placeholder = placeholders[this.value] || '검색어를 입력하세요';
                    });
                    
                    // 페이지 로드 시 전체 도서 목록 조회
                    window.onload = function() {
                        // 초기 플레이스홀더 설정
                        document.getElementById('searchInput').placeholder = '예: 자바의 정석, 스프링 부트';
                        loadAllBooks();
                    };
                </script>
            </body>
            </html>
            """;

        HttpResponse response = new HttpResponse();
        response.statusCode = 200;
        response.statusMessage = "OK";
        response.body = html;
        response.headers.put("Content-Type", "text/html; charset=UTF-8");
        return response;
    }
}
