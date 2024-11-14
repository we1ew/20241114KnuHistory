package seeya.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import seeya.common.common.CommandMap;

public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver{

//    Logger log = Logger.getLogger(this.getClass());
    protected Log log = LogFactory.getLog(this.getClass());
    // supportsParameter : Resolver가 적용가능하진 검사
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CommandMap.class.isAssignableFrom(parameter.getParameterType());
    }

    // resolverArgument메소드는 파라미터와 기타정보를 받아 실제 객체를 반환한다
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        // TODO Auto-generated method stub
        
        CommandMap commandMap = new CommandMap();
        
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Enumeration<?> enumeration = request.getParameterNames();
        Enumeration<?> enumeration2 = request.getParameterNames();

        String key = null;
        String[] values = null;

        // String strEncoding = null;
//        boolean  isComsubmit = false;

//        log.debug("== CustomMapArgumentResolver : Encoding Test s ====================================================================");
//        String fileEncoding = System.getProperty("file.encoding");
//        log.debug("file.encoding = " + fileEncoding);
        while(enumeration2.hasMoreElements()){
            key = (String) enumeration2.nextElement();
//            log.debug("enumeration2 -------------------- "+ key +" ---------------------");
//            if (key.equals("isComsubmit")) {
////                log.debug("-------------------- isComsubmit ---------------------");
//                isComsubmit = true;
//            }
        }
        while(enumeration.hasMoreElements()){
            key = (String) enumeration.nextElement();
            values = request.getParameterValues(key);
            
            if (values != null){
                commandMap.put(key, (values.length > 1) ? values : values[0]);

//                if (isComsubmit) {
//                    strEncoding = (String) commandMap.getMap().get(key);
////                    log.debug("-------------------- isComsubmit ---------------------");
//                    commandMap.getMap().put(key , new String(strEncoding.getBytes("iso-8859-1"), "utf-8"));
////                    log.debug("-------------------- isComsubmit --------------------- : "+ new String(strEncoding.getBytes("iso-8859-1"), "utf-8"));
//                }

/*                String browerType = request.getHeader("User-Agent");

//                Encoding = new String(Encoding.getBytes("iso-8859-1"), "utf-8");
//                commandMap.getMap().put(key , Encoding);

                log.debug("-----------------------------------------");
                log.debug("Encoding : " + strEncoding);
                String toBinaryRaw = new String(strEncoding.getBytes());
                log.debug("Binary Raw Data : " + toBinaryRaw);

                log.debug("-----------------------------------------");

                String[] charSet = {"utf-8" , "euc-kr" , "ksc5601" , "iso-8859-1" , "x-windows-949"};

                for (int i=0; i<charSet.length; i++) {
                    for (int j=0; j<charSet.length; j++) {
                        try {
                            if (browerType != null && (browerType.indexOf("MSIE") != -1 || browerType.indexOf("Trident") != -1)) {
//                                strEncoding = new String(strEncoding.getBytes("8859_1") , "euc-kr");
                                log.debug("IE [" + charSet[i] + ", " + charSet[j] + "] = " + new String(strEncoding.getBytes(charSet[i]), charSet[j]));
                            } else if (browerType != null && (browerType.indexOf("Chrome") != -1)) {
//                                strEncoding = new String(strEncoding.getBytes("8859_1") , "UTF-8");
                                log.debug("Chrome [" + charSet[i] + ", " + charSet[j] + "] = " + new String(strEncoding.getBytes(charSet[i]), charSet[j]));
                            }
                        }catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                }*/

            } // if value != null
        } // while

//        log.debug("== CustomMapArgumentResolver : Encoding Test e ====================================================================");
        
        HttpSession session = request.getSession();
        
        Enumeration<?> attEnum = session.getAttributeNames(); 
        while(attEnum.hasMoreElements()) { 
             key = (String) attEnum.nextElement(); 
             commandMap.put(key , session.getAttribute(key));
        }

        
//        log.debug("== CustomMapArgumentResolver : s ====================================================================");
//        Iterator<String> iterator = commandMap.getMap().keySet().iterator();
//
//        while (iterator.hasNext()) {
//            key = (String) iterator.next();
//            log.debug("key="+ key + ", value="+ commandMap.getMap().get(key));
//        }
//
//        log.debug("== CustomMapArgumentResolver : e ====================================================================");
        
        return commandMap;
    }
    
}
