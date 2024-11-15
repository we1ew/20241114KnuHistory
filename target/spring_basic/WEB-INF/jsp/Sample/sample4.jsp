<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/_include/sample/header.jspf" %>
<%@ include file="/_include/sample/menu.jspf" %>

<div class="container">
    <h4>파라미터 테스트</h4>
    
    <div>
        <p><b>PARAM 'foo'</b></p>
        <input type="text" value="${foo}" readonly>
    </div>
</div>

<%@ include file="/_include/sample/footer.jspf" %>
