<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/_include/sample/header.jspf" %>
<%@ include file="/_include/sample/menu.jspf" %>

<div class="container">
    <h4>DB 테스트2</h4>
    
    <div>
        <ul id="list"></ul>
    </div>
    
    <script>
    $(function() {
        $.ajax({
            method: "POST",
            url: "/sample/sample6_request.do",
            data: {foo:"bzr"},
            dataType: "json",
            success: function(data) {
                if (data.list) {
                    for (var i = 0; i < data.list.length; i++) {
                        var row = data.list[i];
                        var li = $("<li></li>");
                        li.html([row.user_id, row.name].join(" / "));
                        $("#list").append(li);
                    }
                }
            }
        });
    });
    </script>
</div>

<%@ include file="/_include/sample/footer.jspf" %>
