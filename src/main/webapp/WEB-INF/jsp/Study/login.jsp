<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8" />
    <title>#title</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <meta content="" name="description" />
    <meta content="SEEYA INSIGHT - Spirit" name="author" />

    <link rel="shortcut icon" href="/_images/favicon.png" type="image/x-icon">
    <link rel="icon" href="/_images/favicon.png" type="image/x-icon">

    <!-- ================== BEGIN BASE CSS STYLE ================== -->
    <link href="/_plugins/bootstrap/css/bootstrap.css"        rel="stylesheet" />
    <link href="/_plugins/font-awesome/css/font-awesome.css"  rel="stylesheet" />
    <link href="/_plugins/jquery-ui/jquery-ui.css"            rel="stylesheet" />
    <link href="/_plugins/datapicker/datepicker3.css"         rel="stylesheet" />
    <link href="/_plugins/gritter/css/jquery.gritter.css" rel="stylesheet" />

    <!-- ================== BEGIN JS ================== -->
    <script src="/_plugins/pace/pace.js" charset="utf-8"></script>

    <script src="/_js/jquery-2.1.1.js" charset="utf-8"></script>
    <script src="/_plugins/bootstrap/js/bootstrap.js" charset="utf-8"></script>
    <script src="/_plugins/jquery-ui/jquery-ui.js" charset="utf-8"></script>
    <script src="/_plugins/datapicker/bootstrap-datepicker.js" charset="utf-8"></script>

    <!--[if lt IE 9]>
    <script src="/_js/crossbrowserjs/html5shiv.js" charset="utf-8"></script>
    <script src="/_js/crossbrowserjs/respond.min.js" charset="utf-8"></script>
    <script src="/_js/crossbrowserjs/excanvas.min.j" charset="utf-8s"></script>
    <![endif]-->

    <script src="/_plugins/slimscroll/jquery.slimscroll.js" charset="utf-8"></script>
    <script src="/_plugins/gritter/js/jquery.gritter.js" charset="utf-8"></script>

    <script src="/_js/functions.js"></script>
    <script src="/_js/myFunctions.js"></script>
</head>
<body class="pace-top">
    <div style="height: 100vh; text-align: center; background: #d9e0e7">
        <div style="height: 33vh;">
        </div>
        <div style="height: 33vh; align-content: center;">
            <div style="height: 20%;">
                <img src="/_images/logo_img.png" style="height: 100%; margin-right: 300px;">
            </div>
            <div style="width:100vw; background: #2d353c; padding: 30px 500px 30px 500px;">
                <div class="form-group m-b-20">
                    <input type="text" class="form-control input-lg inverse-mode no-border" id="userID" name="userID" placeholder="User ID" value="" autocomplete="off">
                </div>
                <div class="form-group m-b-20">
                    <input type="password" class="form-control input-lg inverse-mode no-border" id="userPass" name="userPass" placeholder="Password" value="" autocomplete="off">
                </div>
                <div class="login-buttons">
                    <button type="submit" id="btnLogin" class="btn btn-blue btn-block btn-lg f-w-600">로그인</button>
                </div>
            </div>
        </div>
        <div style="height: 33vh;">
        </div>
    </div>
</body>
</html>


