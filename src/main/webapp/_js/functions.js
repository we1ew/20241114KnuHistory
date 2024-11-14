/*
***************************************************************************
'*  SOURCE NAME				: prototype.js
'*  FIRST AUTHOR				: 강현주
'*  PROGRAMING DATE		: 2006-07-19
'*  DESCRIPTION				: 자바 스크립트 기본 프로토타입 정의
'***************************************************************************
'***************************************************************************
'*  SYSTEM NAME   GUIDANCE		NAME		DATE			DESCRIPTION
'***************************************************************************
*/

/*
좌우 공백 제거 함수
- 사용방법 : 문자열.trim();
*/
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

/*
왼쪽 공백 제거 함수
- 사용방법 : 문자열.ltrim();
*/
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
}

/*
오른쪽 공백 제거 함수
- 사용방법 : 문자열.rtrim();
*/
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
}

/*
태그만 제거 함수
- 사용방법 : 문자열.stripTags();
*/
String.prototype.stripTags = function () {
    var str = this;
    var pos1 = str.indexOf('<');
    if (pos1 == -1) return str;
    else {
        var pos2 = str.indexOf('>', pos1);
        if (pos2 == -1) return str;
        return (str.substr(0, pos1) + str.substr(pos2 + 1)).stripTags();
    }
}

/*
전체 문자열 변경 함수
- 사용방법 : 문자열.replaceAll();
*/
String.prototype.replaceAll = function (str1, str2) {
    // var temp_str = "";
    // console.log(this.trim());
    // if (this.trim() != "" && str1 != str2) {
    //     temp_str = this.trim();
    //     while (temp_str.indexOf(str1) > -1) {
    //         temp_str = temp_str.replace(str1, str2);
    //     }
    //
    //     return temp_str;
    // } else {
    //     temp_str = this.trim();
    //
    //     return temp_str;
    // }
    return this.split(str1).join(str2);
}


/*
대소문자 구별하지 않고 단어 위치 찾기
- 사용방법 : 문자열.ipos('검색할문자'[, 검색시작할위치]);
*/
String.prototype.ipos = function (needle, offset) {
    var str = this;
    var offset = (typeof offset == "number") ? offset : 0;
    return str.toLowerCase().indexOf(needle.toLowerCase(), offset);
}

/*
대소문자 구별하지 않고 뒤에서부터 단어 위치 찾기
- 사용방법 : 문자열.ripos('검색할문자'[, 검색시작할위치]);
*/
String.prototype.ripos = function (needle, offset) {
    var str = this;
    var offset = (typeof offset == "number") ? offset : str.length;
    return str.toLowerCase().lastIndexOf(needle.toLowerCase(), offset);
}

/*
//길이 체크 함수(같은 경우=true, 같지 않은 경우=false)
- 사용방법 : 문자열.CheckLen(길이값);
*/
String.prototype.checkLen = function (offset) {
    var str = this;
    var offset = (typeof offset == "number") ? offset : 0;
    if (str.length != offset) return false;
    return true;
}

/*
//바이트 길이수 출력 함수
- 사용방법 : 문자열.byteLength();
*/
String.prototype.byteLength = function () {
    var str = this;
    if (typeof str == "undefined" || str == null) return "0";

    var result = 0
    for (var i = 0; i < str.length; i++) {
        result += str.charAt(i) >= 'ㄱ' ? 3 : 1
    }
    return result
}

/*
//바이트 길이수 제한 잘라내기 함수
- 사용방법 : 문자열.truncateString(최대길이수)
*/
String.prototype.truncateString = function (max) {

    var str = this;
    
    if (max > 0) {
        var blen = str.byteLength();
        if (blen > max) {
            return str.substring(0, max - 2) + "..";
        } else {
            return str;
        }
    }
}

/*
//문자열 자르기 (왼쪽 기준)
- 사용방법 : 문자열.Left(길이수)
*/
String.prototype.Left = function (n){
    var str = this;
    if (n <= 0){
       return "";
    }else if (n > String(str).length){
       return str;
    }else{
       return String(str).substring(0,n);
    }
}

/*
//문자열 자르기 (오른쪽 기준)
- 사용방법 : 문자열.Right(길이수)
*/

String.prototype.Right = function (n){
    var str = this;
    if (n <= 0){
       return "";
    }else if (n > String(str).length){
       return str;
    }else{
       var iLen = String(str).length;
       return String(str).substring(iLen, iLen - n);
    }
}

/*
//숫자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkNumeric();
*/
String.prototype.checkNumeric = function () {
    var str = this;
    var pattern = /^-?[0-9]+$/i
    return pattern.test(str)
}

/*
//숫자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkNumeric2();
*/
String.prototype.checkNumeric2 = function () {
    var str = this;
    var pattern = /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/i
    return pattern.test(str)
}

/*
// 영문자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkAlpha();
*/
String.prototype.checkAlpha = function () {
    var str = this;
    var pattern = /^[a-zA-Z]+$/i
    return pattern.test(str)
}

/*
//숫자, 영문자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkAlphaNumeric();
*/
String.prototype.checkAlphaNumeric = function () {
    var str = this;
    var pattern = /^[a-zA-Z0-9]+$/i
    return pattern.test(str)
}

/*
//숫자, 소문자 영문자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkLowerAlphaNumeric();
*/
String.prototype.checkLowerAlphaNumeric = function () {
    var str = this;
    var pattern = /^[a-z0-9]+$/i
    return pattern.test(str)
}

/*
//첫문자는 무조건 영문 나머지는 영문,숫자인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkAlphaNumeric2();
*/
String.prototype.checkAlphaNumeric2 = function () {
    var str = this;
    var pattern = /^[a-zA-z]{1}[0-9a-zA-Z]*$/i
    return pattern.test(str)
}

/*
//첫문자는 무조건 영문 나머지는 영문,숫자. 6자~12자 제한.
- 사용방법 : 문자열.checkUserID();
*/
String.prototype.checkUserID = function () {
    var str = this;
    var pattern = /^[a-zA-z]+[0-9a-zA-Z]{5,11}$/g
    return pattern.test(str)
}



/**
 * 비번유효성 검증. 영문, 숫자, 특수문자, 8자 ~ 15자
 * @param str
 * @returns {boolean}
 */
checkPassword = function(str) {
    var pattern = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&()_]).*$/;

    if(pattern.test(str)){
        return true;
    }else{
        return false;
    }
}

/**
 * 비밀번호는 영문 대문자, 소문자 ,숫자가 모두 포함된 최소 13자리이상이여야 합니다.
 * @param str
 * @returns {boolean}
 */
checkPasswordRegular = function(str) {
    //var pattern = /^.*(?=^.{10,}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*$/;
    var pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{10,}$/;
    if(pattern.test(str)){
        return true;
    }else{
        return false;
    }
}

/*
//한글인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkHan();
*/
String.prototype.checkHan = function () {
    var str = this;
    var result = true;
    for (var i = 0; i < str.length; i++) {
        var chr = str.charCodeAt(i);
        if (chr > 255) continue;
        else {
            if (chr == 32) continue;
            result = false;
            break;
        }
    }
    return result;
}

/*
//도메인인지 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.domainCheck();
*/
String.prototype.domainCheck = function () {
    var str = this;
    var strDomain1;
    strDomain1 = str.replace(/^[\s]+/g, "");
    strDomain1 = strDomain1.replace(/[\s]+$/g, "");
    if (strDomain1.search(/[\w\~\-]+(\.[\w\~\-]+)+\s*$/g) < 0 || strDomain1.search(/^(www|WWW)\.+/g) > -1) {
        return false;
    } else return true;
}

/*
유효한 이메일 주소 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkEmail();
*/
String.prototype.checkEmail = function () {
    var str = this;
    var pattern = /[a-zA-Z0-9-_]+@[a-zA-Z0-9-_.]+\.[a-zA-Z]{2,}/;
    if (!pattern.test(str)){
      return false;
    }else{
      return true;
    }
}

/*
유효한 전화번호 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkPhone();
*/
String.prototype.checkPhone = function () {
    var str = this;
    var pattern = /^([0]{1}[0-9]{1,3}) ?([1-9]{1}[0-9]{2,4}) ?([0-9]{2,4})$/;
    return pattern.test(str)
}


/*
유효한 전화번호 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkTel();
*/
String.prototype.replacePhone = function () {
    var str = this;

    return str.replace(/^([0]{1}[0-9]{1,2})-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/, "$1-$2-$3");
}


/*
유효한 전화번호 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkHand();
*/
String.prototype.checkHand = function () {
    var str = this;
    var pattern = /^(?:(010-\d{4})|(01[1|6|7|8|9]-\d{3,4}))-(\d{4})$/;
    return pattern.test(str)
}


/*
유효한 주민 번호 여부를 부울값으로 반환합니다.
- 사용방법 : checkJumin('주민등록번호값1', '주민등록번호값2');
*/
var checkJumin = function (jumin1, jumin2) {
    if (!jumin1.checkNumeric()) {
        return false;
    }
    if (!jumin2.checkNumeric()) {
        return false;
    }

    if (!jumin1.checkLen(6)) {
        return false;
    }

    if (!jumin2.checkLen(7)) {
        return false;
    }

    var jumin = jumin1 + jumin2;
    var check_sum = parseInt(jumin.charAt(0)) * 2 + parseInt(jumin.charAt(1)) * 3
               	  + parseInt(jumin.charAt(2)) * 4 + parseInt(jumin.charAt(3)) * 5
               	  + parseInt(jumin.charAt(4)) * 6 + parseInt(jumin.charAt(5)) * 7
               	  + parseInt(jumin.charAt(6)) * 8 + parseInt(jumin.charAt(7)) * 9
               	  + parseInt(jumin.charAt(8)) * 2 + parseInt(jumin.charAt(9)) * 3
               	  + parseInt(jumin.charAt(10)) * 4 + parseInt(jumin.charAt(11)) * 5;
    check_sum = check_sum % 11;
    check_sum = 11 - check_sum;
    var check_digit = parseInt(jumin.charAt(12));

    if (check_sum >= 10) check_sum = check_sum - 10;
    if (check_digit != check_sum) {
        return false;
    }
    return true;
}


/*
특수문자사용 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.specialChar();
*/
String.prototype.specialChar = function () {
    var str = this;
    for (i = 0; i < str.length; i++) {
        var ch = str.charCodeAt(i);
        if ((ch >= 0 && ch <= 47) || (ch >= 58 && ch <= 64) || (ch >= 91 && ch <= 94) || (ch == 96) || (ch >= 123 && ch <= 255) || (ch == 95))
            return false;
    }
    return true;
}

/*
숫자를 통화형으로 변환 (1000 => 1,000)
- 사용방법 : 문자열.get_currency();
*/
String.prototype.get_currency = function () {
    var str = this;
    var strCurrency = "";

    str = str.get_numeric();

    if (str.checkNumeric()) {
      var num = parseFloat(str);
      var minus = '';
      if(num < 0) {minus = '-'; }
      num = Math.abs(num);
      str = new String(num);
      for (i = 0; i < str.length; i++) {
          if (i > 0 && (i % 3) == 0)
              strCurrency = str.charAt(str.length - i - 1) + "," + strCurrency;
          else
              strCurrency = str.charAt(str.length - i - 1) + strCurrency;
      }

      strCurrency = minus + strCurrency;
    }else{
      strCurrency = str;
    }

    return strCurrency;
}

/*
통화형을 숫자로 변환 (1,000 => 1000)
- 사용방법 : 문자열.get_numeric();
*/
String.prototype.get_numeric = function () {
    var str = this;
    var strNumeric = str;

    strNumeric = strNumeric.replaceAll(",", "");

    if(isNaN(strNumeric)) strNumeric = "";

    return strNumeric;
}

/*
문자열을 encode화 (by 강현주 2006-07-21)
- 사용방법 : 문자열.encode();
*/
String.prototype.encode = function () {
    var str = this;
    if (encodeURIComponent) {
        return encodeURIComponent(str);
    }
    else if (escape) {
        return escape(str);
    }
    else {
        return null;
    }
}


/*
문자열을 decode화 (by 강현주 2006-07-21)
- 사용방법 : 문자열.decode();
*/
String.prototype.decode = function () {
    var str = this;
    str = str.replace(/\+/g, ' ');
    if (decodeURIComponent) {
        return decodeURIComponent(str);
    }
    else if (unescape) {
        return unescape(str);
    }
    else {
        return null;
    }
}


/*
유효한 아이피 여부를 부울값으로 반환합니다.
- 사용방법 : 문자열.checkIP();
*/
String.prototype.checkIP = function () {
    var strIP = this;
    var result = false;

    if(strIP.search(/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/) >= 0) {
        var arrIP = strIP.split('.');
        if(arrIP.length == 4) {
            if(arrIP[0] < 256 && arrIP[1] < 256 && arrIP[2] < 256 && arrIP[3] < 256) {
                result = true;
            }
        }
    }
    return result;
}
/*
 * 유효한 IPv4 검증
 */
String.prototype.checkIPv4 = function(){
    var str = this;
    var pattern = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    
    return pattern.test(str);
}

/*
 * 유효한 IPv6 검증
 */
String.prototype.checkIPv6 = function(){
    var str = this;
    var pattern = /^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$/;
 
    return pattern.test(str);
}

/*
 * 유효한 IPvBoth 검증
 */
String.prototype.checkIPvBoth = function(){
    var str = this;
    var pattern = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$/;
    
    return pattern.test(str);
}

/*
 * 유효한 맥주소 검증
 */
String.prototype.checkMacAddr = function(){
    var str = this;
    var pattern = /^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$/;
    
    return pattern.test(str);
}

/*
 * 유요한 날짜 시간 여부를 부울값으로 반환
 * 사용방법 : 문자열.checkDateTime();
 */
String.prototype.checkDateTime = function(){
    var str = this;
    
    var pattern = /^(19|20)[0-9]{2}[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$/;
    
    return pattern.test(str);
}

/*
 * byte크기에 따른 변환
 */
String.prototype.byteConvertor = function() {

    bytes = parseInt(this);

    var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];

    var e = Math.floor(Math.log(bytes)/Math.log(1024));

    if(e == "-Infinity") return "0 "+s[0]; 

    else return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];

}


function getKeyCode(e){
    var result;
    // IE
    if(window.event){
        result = event.keyCode;
    // Firefox
    } else if(e){
        result = e.which;
    }
    return result;
}