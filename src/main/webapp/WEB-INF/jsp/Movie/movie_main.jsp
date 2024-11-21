<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>영화 평점 공유 사이트</title>
    <link rel="stylesheet" href="/_css/Movie/styles.css">
    <script src="/_js/jquery-2.1.1.js" charset="utf-8"></script>
    <script src="/_js/Movie/movie_main.js"></script>
</head>
<body>
    <header>
        <div class="container">
            <h1>영화 평점 공유</h1>
            <nav>
                <ul>
                    <li><a href="main.do">홈</a></li>
                    <li><a href="list.do">영화 목록</a></li>
                    <c:if test="${not empty name}">
                        <li><a href="#" id="logout_button">${name}님 로그아웃</a></li>
                    </c:if>
                    <c:if test="${empty name}">
                        <li><a href="login.do">로그인</a></li>
                    </c:if>
                    <input type="hidden" value="${user_id}" id="user_id">
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <section class="add-rating-section">
            <h2>평점 추가</h2>
            <div id="add-rating-form">
                <select id="movie-title" required>
                    <option value="">영화 선택</option>
                </select>
                <select id="rating" required>
                    <option value="">평점 선택</option>
                    <option value="1">1점</option>
                    <option value="2">2점</option>
                    <option value="3">3점</option>
                    <option value="4">4점</option>
                    <option value="5">5점</option>
                </select>
                <button id="rating_add_button">추가</button>
            </div>
        </section>

        <section class="rating-list-section">
            <h2>평점 목록</h2>
            <table>
                <thead>
                    <tr>
                        <th>번호</th>
                        <th>영화 제목</th>
                        <th>감독</th>
                        <th>평점</th>
                    </tr>
                </thead>
                <tbody id="rating-list">
                    <!-- 평점 목록이 여기에 추가됩니다 -->
                </tbody>
            </table>
        </section>
    </main>
    <footer>
        <p>&copy; 2024 영화 평점 공유 사이트</p>
    </footer>
</body>
</html>
