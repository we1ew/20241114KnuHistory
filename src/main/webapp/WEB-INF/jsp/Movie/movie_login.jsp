<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <script src="/_js/jquery-2.1.1.js" charset="utf-8"></script>
    <link rel="stylesheet" href="/_css/Movie/styles.css">
    <script src="/_js/Movie/movie_login.js"></script>
</head>
<body>
    <header>
        <div class="container">
            <h1>영화 평점 공유</h1>
            <nav>
                <ul>
                    <li><a href="main.do">홈</a></li>
                    <li><a href="list.do">영화 목록</a></li>
                    <li><a href="login.do" class="active">로그인</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <main>
        <section class="login-section">
            <h2>로그인</h2>
            <div id="login-form">
                <div class="form-group">
                    <label for="username">아이디</label>
                    <input type="text" id="username" name="username" placeholder="아이디를 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
                </div>
                <button id="login_button" type="submit">로그인</button>
                <p class="register-link">계정이 없으신가요? <a href="signIn.do">회원가입</a></p>
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2024 영화 평점 공유 사이트</p>
    </footer>
</body>
</html>
