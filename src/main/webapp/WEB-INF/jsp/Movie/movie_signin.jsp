<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <link rel="stylesheet" href="/_css/Movie/styles.css">
    <script src="/_js/jquery-2.1.1.js" charset="utf-8"></script>
    <script src="/_js/Movie/movie_signin.js"></script>
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
        <section class="register-section">
            <h2>회원가입</h2>
            <div id="register-form">
                <div class="form-group">
                    <label for="user_id">아이디</label>
                    <input type="text" id="user_id" name="user_id" placeholder="아이디를 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input type="text" id="name" name="name" placeholder="이름을 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="confirm-password">비밀번호 확인</label>
                    <input type="password" id="confirm-password" name="confirm-password" placeholder="비밀번호를 다시 입력하세요" required>
                </div>
                <button type="submit" id="signin_button">회원가입</button>
                <p class="login-link">이미 계정이 있으신가요? <a href="login.do">로그인</a></p>
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2024 영화 평점 공유 사이트</p>
    </footer>
</body>
</html>
