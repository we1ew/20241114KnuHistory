<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/_include/sample/header.jspf" %>
<%@ include file="/_include/sample/menu.jspf" %>

<div class="container">
    <h4>세션 테스트</h4>
    
    <div>
        <h4>session</h4>
        <input type="text" id="print_value" value="${foo}" readonly>
    </div>
    
    <br><br>
    
    <form method="POST" action="/sample/sample5_request.do">
        <input type="text" name="foo" id="foo">
        <button type="submit">submit</button>
    </form>
    
    <script>
    $(function() {
        var form = $("form");
        form.submit(function(e) {
            $.ajax({
                method: form.attr("method"),
                url: form.attr("action"),
                data: {
                    foo: $("#foo").val()
                },
                dataType: "json",
                success: function(resp) {
                    if (resp.foo) {
                        $("#print_value").val(resp.foo);
                    }
                }
            });
            e.preventDefault();
        });
    });
    </script>
</div>

<%@ include file="/_include/sample/footer.jspf" %>
