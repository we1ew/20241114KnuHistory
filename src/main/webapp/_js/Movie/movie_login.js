$(function() {
    $("#password").keyup(function(e) { // id가 password인 객체에서 키보드를 눌렀다 놓았을때 이벤트 발생
        if(e.keyCode === 13) { // 엔터키를 누르면
            $("#login_button").click(); // 로그인 버튼 클릭 이벤트 발생
        }
    });

    $("#login_button").click(function(e) { // 로그인 버튼 클릭시 이벤트 발생
        const username = $("#username").val(); // username이라는 id의 input의 value값 가져옴
        const password = $("#password").val(); // password라는 id의 input의 value값 가져옴
        $.ajax({
            method: "POST",
            url: "/Movie/ajax/login.do",
            data: {
                id: username,
                pw: password
            },
            dataType: "json",
            success: function(data) {
                if(data.result) {
                    location.href = "/Movie/main.do";
                } else {
                    alert('로그인에 실패하였습니다.');
                }
            },
            error: function(err) {
                console.log("movieList error : ", err);
            }
        });
    });
})