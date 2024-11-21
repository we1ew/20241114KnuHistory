$(function() {
    $("#signin_button").click(function(e) { // 회원가입 버튼 클릭시 이벤트 발생
        const user_id = $("#user_id").val(); // user_id이라는 id의 input의 value값 가져옴
        const name = $("#name").val(); // name라는 id의 input의 value값 가져옴
        const password = $("#password").val(); // password라는 id의 input의 value값 가져옴
        $.ajax({
            method: "POST",
            url: "/Movie/ajax/signIn.do",
            data: {
                id: user_id,
                name: name,
                pw: password
            },
            dataType: "json",
            success: function(data) {
                if(data.result) {
                    alert("회원가입에 성공하였습니다");
                    location.href = "/Movie/login.do"
                } else {
                    alert(data.msg);
                }
            },
            error: function(err) {
                console.log("movieSignIn error : ", err);
            }
        });
    });
})