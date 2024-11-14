<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    /*******************************************************
     * SOURCE NAME     : /error404.jsp
     * FIRST AUTHOR    : 이병덕
     * PROGRAMING DATE : 2017.09.15
     * DESCRIPTION     : 404 오류페이지
     *******************************************************
     * MODIFY_DATE      MODIFY_NAME     MODIFY_DESCRIPTION
     *
     *******************************************************/
%>
<%@ include file="/_include/header.jspf" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
    $(document).ready(function () {
        //
    });
</script>
</head>
<body>
    <!-- begin #page-loader -->
    <div id="page-loader" class="fade in"><span class="spinner"></span></div>
    <!-- end #page-loader -->

    <!-- begin #page-container -->
    <div id="page-container" class="fade">

        <div class="error">
            <div class="error-code m-b-10">ERROR <i class="fa fa-warning"></i></div>
            <div class="error-content">
                <div class="error-message"><c:out value="${err.errMessage }"/></div>
                <div class="error-desc m-b-20 <spring:message code='image.errorBg' />">
                    
                </div>
                <div>
                    <a href="/Dashboard.do" class="btn btn-success">홈으로 이동</a>
                </div>
            </div>
        </div>
    </div>
    <!-- end page container -->
</body>
</html>