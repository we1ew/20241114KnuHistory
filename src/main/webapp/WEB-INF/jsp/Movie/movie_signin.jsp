<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
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
        <section class="register-section">
            <h2>회원가입</h2>
            <form id="register-form">
                <div class="form-group">
                    <label for="username">아이디</label>
                    <input type="text" id="username" name="username" placeholder="아이디를 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" id="email" name="email" placeholder="이메일을 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
                </div>
                <div class="form-group">
                    <label for="confirm-password">비밀번호 확인</label>
                    <input type="password" id="confirm-password" name="confirm-password" placeholder="비밀번호를 다시 입력하세요" required>
                </div>
                <button type="submit">회원가입</button>
                <p class="login-link">이미 계정이 있으신가요? <a href="login.do">로그인</a></p>
            </form>
        </section>
    </main>
    <footer>
        <p>&copy; 2024 영화 평점 공유 사이트</p>
    </footer>
</body>
</html>
