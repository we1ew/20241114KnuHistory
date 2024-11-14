package seeya.sample.web;

import seeya.sample.service.SampleService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class SampleController {
    /*
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name="loginService")
    private LoginService loginService;

    @Resource(name="commonService")
    private CommonService commonService;

    @Resource(name="dashboardService")
    private DashboardService dashboardService;
    */
    
    @Resource(name="sampleService")
    private SampleService sampleService;
    

    @RequestMapping(value="/sample/sample1.do")
    public ModelAndView Sample1Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Sample/sample1");
        return mv;
    }
    
    @RequestMapping(value="/sample/sample2.do")
    public ModelAndView Sample2Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Sample/sample2");
        return mv;
    }
    
    @RequestMapping(value="/sample/sample3.do")
    public ModelAndView Sample3Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Sample/sample3");
        return mv;
    }
    
    @RequestMapping(value="/sample/sample4.do")
    public ModelAndView Sample4Page(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("/Sample/sample4");
        
        if (params.containsKey("foo")) {
            String[] foo = (String[]) params.get("foo");
            mv.addObject("foo", foo[0]);
        }
        return mv;
    }
    
    @RequestMapping(value="/sample/sample5.do")
    public ModelAndView Sample5Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Sample/sample5");
        
        Object foo = session.getAttribute("foo");
        if (foo != null) {
            mv.addObject("foo", foo);
        }
        return mv;
    }
    
    @RequestMapping(value="/sample/sample5_request.do", method=RequestMethod.POST)
    public ModelAndView Sample5Request(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        
        if (params.containsKey("foo")) {
            String[] foo = (String[]) params.get("foo");
            session.setAttribute("foo", foo[0]);
        }
        
        Object foo = session.getAttribute("foo");
        if (foo != null) {
            mv.addObject("foo", (String)foo);
        }
        return mv;
    }
    
    @RequestMapping(value="/sample/sample6.do")
    public ModelAndView Sample6Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Sample/sample6");
        return mv;
    }
    
    @RequestMapping(value="/sample/sample6_request.do", method=RequestMethod.POST)
    public ModelAndView Sample6Request(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String, Object>> list = sampleService.testSelect(params);
        if (list != null) {
            mv.addObject("list", list);
        }
        return mv;
    }
    


}
