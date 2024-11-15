package seeya.common.util;

import seeya.excel.util.ExcelUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 공통함수 모음
 */
public class Common {

    private static final Random RANDOM = new SecureRandom();
    
    public static boolean isRead = false;     // 읽기 권한
    public static boolean isWrite = false;    // 쓰기 권한
    public static boolean isModify = false;   // 수정 권한
    public static boolean isDelete = false;   // 삭제 권한
    public static boolean isComment = false;  // 댓글 권한 - 댓글 권한이 있으면 본인이 작성한 댓글에 한해 수정, 삭제 권한이 부여된다.
    public static boolean isManager = false;  // 관리 권한 - 읽기권한이 있어야 한다. 쓰기, 수정, 삭제 권한을 포함한다.

    /**
     * permInit                                    권한초기화
     * @throws Exception
     */
    public static void permInit() throws Exception{
        isRead    = false;
        isWrite   = false;
        isModify  = false;
        isDelete  = false;
        isComment = false;
        isManager = false;
    }
    
    /**
     * setPerm                                     메뉴별 권한 세팅
     * @param map
     * @throws Exception
     */
    public static void setPerm(Map<String, Object> map) throws Exception {
        // Auto-generated method stub
        permInit(); // 권한초기화
        isRead    = !isEmpty(map.get("READ_YN")) && map.get("READ_YN").equals("Y");
        isWrite   = !isEmpty(map.get("REG_YN")) && map.get("REG_YN").equals("Y");
        isModify  = !isEmpty(map.get("MDFCN_YN")) && map.get("MDFCN_YN").equals("Y");
        isDelete  = !isEmpty(map.get("DEL_YN")) && map.get("DEL_YN").equals("Y");
        isComment = !isEmpty(map.get("COMMENT_YN")) && map.get("COMMENT_YN").equals("Y");
        isManager = !isEmpty(map.get("MANAGE_YN")) && map.get("MANAGE_YN").equals("Y");
    }

    /**
     * getPerm
     * 권한설정값  
     * @return
     * @throws Exception
     */
    public static Map<String,Object> getPerm() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("isRead", isRead);
        map.put("isWrite", isWrite);
        map.put("isModify", isModify);
        map.put("isDelete", isDelete);
        map.put("isComment", isComment);
        map.put("isManager", isManager);

        return map;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * 
     * str1에 값이 없으면 value 리턴, 값이 있으면 str1 리턴
     * @param str1
     * @param value
     * @return
     */
    public static String getCheckString(String str1 , String value) {
        String resultStr = "";
        
        if (isEmpty(str1) || "null".equals(str1)) {
            resultStr = value;
        }else{
            resultStr = str1;
        }
        
        return resultStr;
    }
    
    public static String getClientIP(HttpServletRequest request) throws Exception {

        String ip = request.getHeader("X-FORWARDED-FOR"); 
        
        if(ip == null || ip.length() == 0) {
            
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
        }
        if(ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }
        return ip;
    }
    
    /**
     *                          두 날짜사이의 시간 계산 : 분 반환
     * @param startStr
     * @return
     * @throws Exception
     */
    public static Integer gapLoginTime(String startStr) throws Exception{     
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                  
        
        //System.out.println("--------------------------- gapLoginTime.startStr : "+ startStr);
        //String x = "2015-08-11 10:50:00";
        //String y = "2015-08-11 10:55:00";

        String endStr = dataFormat.format(new Date());

        Date startDate = dataFormat.parse(startStr);
        Date endDate = dataFormat.parse(endStr);
        
        //System.out.println("--------------------------- gapLoginTime.startDate : "+ startDate);
        //System.out.println("--------------------------- gapLoginTime.endStr : "+ endStr);
 
        long duration = Math.abs(endDate.getTime() - startDate.getTime()) / 1000 / 60;
        //System.out.println("--------------------------- gapLoginTime.duration : "+ duration);

        if (duration >= 25) {
            duration = 25;
        }
        return (int)duration;
    }

    /**
     * 기준일(strD)로부터 입력된 날수(day) 만큼 이후 또는 이전 날짜 리턴
     * @return
     * @throws Exception
     */
    public static String getCalDate(String strD , Integer day) throws Exception {
        Log log = LogFactory.getLog(ExcelUtil.class);
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        try{
            if (isEmpty(strD)) {
                date = new Date();
            }else{
                strD = strD.replaceAll("-" , "");
                date = dateFormat.parse(strD);
            }
        }catch (ParseException e) {
            //e.printStackTrace();
            log.error("ERROR : 기준일(strD)로부터 입력된 날수(day) 만큼 이후 또는 이전 날짜 리턴");
        }
        
        Calendar cal = Calendar.getInstance();
        
        cal.setTime(date);
        
        cal.add(Calendar.DATE , day);

        return dateFormat.format(cal.getTime());
    }
    
    /**
     * getDate                                  현재 날짜
     * @return
     * @throws Exception
     */
    public static String getDate() throws Exception {
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String regDate = dateFormat.format(d);
        
        return regDate;
    }

    /**
     * getTime                                  현재 시간
     * @return
     * @throws Exception
     */
    public static String getTime() throws Exception {
        Date d = new Date();
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        String regTime = timeFormat.format(d);
        
        return regTime;
    }

    /**
     * getDateTime    현재날짜와 시간을 YYYYMMDDHH24MISS 형식으로 리턴
     * @return
     * @throws Exception
     */
    public static String getDateTime() throws Exception {
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

        String result = dateFormat.format(d) + timeFormat.format(d);

        return result;
    }
    /**
     * getDateTime    현재날짜와 시간 + 20분을 YYYYMMDDHH24MISS 형식으로 리턴
     * @return
     * @throws Exception
     */
    public static String getDateTimelater20() throws Exception {
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 20);
        String today = null;
        today = dateFormat.format(cal.getTime());

        return today;
    }

    /**
     * getDateTime    현재날짜와 시간 + 20분을 YYYYMMDDHH24MISS 형식으로 리턴
     * @return
     * @throws Exception
     */
    public static String getDateTimelater1day(String strDate) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();
        Date dt = dateFormat.parse(strDate);
        cal.setTime(dt);
        cal.add(Calendar.DATE , 1);

        return dateFormat.format(cal.getTime());
    }
    /**
     * 숫자를 입력된 자리 수만큼 문자로 변경
     * @return
     * @throws Exception
     */
    public static String changeIntToStr(Integer num, Integer len) throws Exception {
        
        String result = num.toString();
        Integer maxlen = len - result.length();
        
        for (int i = 0; i<maxlen; i++) {
            result = "0" + result;
        }

        return result;
    }
    /**
     * 문자를 입력된 자리 수만큼 뒤에 0 추가
     * @return
     * @throws Exception
     */
    public static String changeStrLength(String str, Integer maxLen) throws Exception {
        Integer strLen = str.length();
        String result = "";

        if (strLen < maxLen) {
            for (int i = 0; i < (maxLen - strLen); i++) {
                result = result + "0";
            }
        }

        return str + result;
    }
    
    /**
     * 
     * 문자열의 숫자 체크
     * @param str
     * @return
     * @throws Exception
     */
    public static boolean isNumber(String str) throws Exception {
        return str.matches("^[0-9]*$");
    }

    /**
     *                      코드 규칙 정규식
     *                      영문으로 시작, 숫자 + 영문으로 구성
     * @param str
     * @param type
     * @return
     */
    public static boolean CodeRegex(String str , String type) {
        String regex = "";
        // String pattern = "";
        if (type.equals("code")) {
            regex = "^[a-zA-z]{1}[0-9a-zA-Z]{1,8}$";
        }else if (type.equals("id")){                   // 사용자 아이디
            regex = "^[a-zA-z]+[0-9a-zA-Z]{5,11}$";
        }else if (type.equals("board")){                // 게시판코드
            regex = "^[a-zA-z]{3,9}$";
        }else if (type.equals("pw")) {
            //regex = "^.*(?=^.{13,}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$";
            // regex = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&()_]).*$";
            regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{10,}$";
        }else if (type.equals("ip")) {
            regex = "((([0-9])|([1-9]\\d{1})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}(([0-9])|([1-9]\\d{1})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))";
        }

        //return Pattern.matches(regex, str);
        return str.matches(regex);
    }
    
    /**
     *                      IP 정규식
     * @param s
     * @return
     */
    /*
    public static boolean checkIPv4(String str) {
        String regex = "((([0-9])|([1-9]\\d{1})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}(([0-9])|([1-9]\\d{1})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))";
        
        return str.matches(regex);
    }
    */
    
    /**
     * 널이면 true 리턴
     * @param s
     * @return
     */
    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>)s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>)s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[])s).length == 0);
        }
        return false;
    }
    
    /**
     * 32글자의 랜덤 문자열 생성
     * @return
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 
     * XSS
     * @param str
     * @param use_html
     * @return
     */
    public static String removeXSS(String str, boolean use_html) {
//        String str_low = "";

        // 스크립트 문자열 필터링 (선별함 - 필요한 경우 보안가이드에 첨부된 구문 추가)
//        str_low= str.toLowerCase();
//        str = str_low;

        String[] xssList = { "javascript" , "script"     , "iframe"   , "document"   , "vbscript"   , "applet"   , "embed"      , "object"     , "frame"    ,
                "grameset"   , "layer"      , "bgsound"  , "alert"      , "onblur"     , "onchange" , "onclick"    , "ondblclick" , "enerror"  ,
                "onfocus"    , "onload"     , "onmouse"  , "onscroll"   , "onsubmit"   , "onunload" , "img"
        };
        String xssTag = "";

        for (int i=0, size=xssList.length; i<size; i++ ){
            xssTag = xssList[i];

            str = str.replaceAll("x-"+ xssTag, xssTag);

            if (str.contains(xssTag)) {
                str = str.replaceAll(xssTag, "x-"+ xssTag);
            }
        }

        if(use_html){ // HTML tag를 사용하게 할 경우 부분 허용
            str = str.replace("&lt;", "<");
            str = str.replace("&gt;", ">");
            str = str.replace("&amp;nbsp;", "&nbsp;");

            // 2021-10-26 전형상 혹시나 추가
            str = str.replace("&#x2F;", "/");
            str = str.replace("&#39", "'");
            str = str.replace("&quot;", "\"");

//            str = str.replaceAll("./", "");
            // 허용할 HTML tag만 변경
            //str = str.replaceAll("&lt;p&gt;", "<p>");
            //str = str.replaceAll("&lt;P&gt;", "<P>");
            //str = str.replaceAll("&lt;br&gt;", "<br>");
            //str = str.replaceAll("&lt;BR&gt;", "<BR>");
        }else{ // HTML tag를 사용하지 못하게 할 경우
            str = str.replaceAll("\"","&gt;");
            str = str.replaceAll("&", "&amp;");
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
            str = str.replaceAll("%00", null);
            str = str.replaceAll("\"", "&#34;");
            str = str.replaceAll("'", "&#39;");
            str = str.replaceAll("%", "&#37;");    
            str = str.replaceAll("../", "");
            str = str.replaceAll("..\\\\", "");
            str = str.replaceAll("./", "");
            str = str.replaceAll("%2F", "");

            str = str.replaceAll("&lt;p&gt;", "<p>");
            str = str.replaceAll("&lt;P&gt;", "<P>");
            str = str.replaceAll("&lt;br&gt;", "<br>");
            str = str.replaceAll("&lt;BR&gt;", "<BR>");
        }
        return str;
    }

    /**
     * 랜덤값 생성
     * @return
     */
    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }
    /**
     * 
     * 문자열 암호화
     * @param str
     * @return
     */
    public static String strEncode(String str, byte[] salt) {
        try{
 
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(str.getBytes());
            digest.update(salt);

            byte[] hash = digest.digest();
            StringBuffer hexString = new StringBuffer();
 
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
 
            //출력
            //System.out.println(hexString.toString());
            return hexString.toString();
 
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
 
    }
}
