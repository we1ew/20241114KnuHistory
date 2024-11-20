<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>영화 평점 공유 사이트</title>
    <link rel="stylesheet" href="/_css/Movie/styles.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>영화 평점 공유</h1>
            <nav>
                <ul>
                    <li><a href="main.do">홈</a></li>
                    <li><a href="list.do">영화 목록</a></li>
                    <li><a href="login.do">로그인</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <section class="search-section">
            <h2>영화 검색</h2>
            <div>
                <input type="text" id="search-input" placeholder="영화 제목 또는 감독 검색">
                <button>검색</button>
            </div>
        </section>

        <section class="add-rating-section">
            <h2>평점 추가</h2>
            <div id="add-rating-form">
                <input type="text" id="movie-title" placeholder="영화 제목" required>
                <input type="text" id="director" placeholder="감독" required>
                <select id="rating" required>
                    <option value="">평점 선택</option>
                    <option value="1">1점</option>
                    <option value="2">2점</option>
                    <option value="3">3점</option>
                    <option value="4">4점</option>
                    <option value="5">5점</option>
                </select>
                <button>추가</button>
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
