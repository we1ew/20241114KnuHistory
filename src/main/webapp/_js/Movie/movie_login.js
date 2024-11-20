$(function() {
    $("#login_button").click(function(e) {
        const username = $("#username").val();
        const password = $("#password").val();
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