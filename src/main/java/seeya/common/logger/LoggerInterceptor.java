package seeya.common.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerInterceptor extends HandlerInterceptorAdapter {
    protected Log log = LogFactory.getLog(LoggerInterceptor.class);

    // @Resource(name="loginService")
    // private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("======================================          START         ======================================");
            log.debug(" Request URI \t:  " + request.getRequestURI());
        }

        // 로그인 체크 : 로그인 시도하는 페이지가 아니면 로그인 여부 확인 후 리다이렉트.
        /*
        if (!(request.getRequestURI().equals("/LoginProcess.do") || request.getRequestURI().equals("/ajax/test.do"))) {
            if(request.getSession().getAttribute("strMyID") == null || request.getSession().getAttribute("strMyID") == "" ){
                response.sendRedirect("/");
            }
        }
        */

        // SSO 적용 - 운영서버 배포
//        if (!request.getRequestURI().equals("/index.do") ) {
//            if(request.getSession().getAttribute("strMyID") == null || request.getSession().getAttribute("strMyID") == "" ){
//                String result = loginService.loginProcessSSO(request , request.getSession() , null);
//
//                if (result.equals("SUCCESS") || result.equals("WORK_PAGE")) {
//                }else{
//                    response.sendRedirect("/");
//                    return false;
//                }
//            }
//        }

        
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("======================================           END          ======================================\n");
        }
    }
}
