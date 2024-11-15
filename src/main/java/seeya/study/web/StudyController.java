package seeya.study.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import seeya.sample.service.SampleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class StudyController {

    @RequestMapping(value="/login")
    public ModelAndView Sample1Page(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Study/login");
        return mv;
    }
}
