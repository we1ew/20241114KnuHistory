<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>영화 목록</title>
    <script src="/_js/jquery-2.1.1.js" charset="utf-8"></script>
    <link rel="stylesheet" href="/_css/Movie/styles.css">
    <script src="/_js/Movie/movie_list.js"></script>
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
        <section class="filter-section">
            <h2>장르 필터</h2>
            <div class="filter-list">
                <label><input type="checkbox" name="genre" value="drama"> 드라마</label>
                <label><input type="checkbox" name="genre" value="action"> 액션</label>
                <label><input type="checkbox" name="genre" value="comedy"> 코미디</label>
                <label><input type="checkbox" name="genre" value="thriller"> 스릴러</label>
                <label><input type="checkbox" name="genre" value="animation"> 애니메이션</label>
            </div>
            <div class="filter-apply">
                <button class="apply-button">필터 적용</button>
            </div>
        </section>
        <section class="movie-list-section">
            <h2>영화 목록</h2>
            <div class="movie-grid">
                <div class="movie-card">
                    <img src="/_images/movie_01.png" alt="영화 포스터">
                    <h3>글레디에이터</h3>
                    <p><strong>감독:</strong> 리들리 스콧</p>
                    <p><strong>장르:</strong> 액션, 사극</p>
                    <button>평점 보기</button>
                </div>
                <div class="movie-card">
                    <img src="/_images/movie_02.png" alt="영화 포스터">
                    <h3>청설</h3>
                    <p><strong>감독:</strong> 조선호</p>
                    <p><strong>장르:</strong> 드라마, 멜로/로멘스</p>
                    <button>평점 보기</button>
                </div>
                <div class="movie-card">
                    <img src="/_images/movie_03.png" alt="영화 포스터">
                    <h3>사흘</h3>
                    <p><strong>감독:</strong> 현문섭</p>
                    <p><strong>장르:</strong> 공포(호러), 미스터리</p>
                    <button>평점 보기</button>
                </div>
                <!-- 추가적인 영화 카드 -->
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2024 영화 평점 공유 사이트</p>
    </footer>
</body>
</html>
