package seeya.movie.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import seeya.movie.service.MovieService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MovieController {
    @Resource(name = "movieService")
    private MovieService movieService;

    @RequestMapping(value="/Movie/main.do")
    public ModelAndView MainPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Movie/movie_main");
        return mv;
    }

    @RequestMapping(value="/Movie/list.do")
    public ModelAndView ListPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Movie/movie_list");
        return mv;
    }

    @RequestMapping(value="/Movie/login.do")
    public ModelAndView LoginPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Movie/movie_login");
        return mv;
    }

    @RequestMapping(value="/Movie/signIn.do")
    public ModelAndView SignInPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Movie/movie_signin");
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/login.do", method= RequestMethod.POST)
    public ModelAndView ajaxLogin(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        boolean result = movieService.login(params);
        mv.addObject("result", result);
        if(result) {
            session.setAttribute("user_id", ((String[])params.get("id"))[0]);
        }
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/sginIn.do", method= RequestMethod.POST)
    public ModelAndView ajaxSignIn(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        boolean result = movieService.login(params);
        mv.addObject("result", result);
        if(result) {
            session.setAttribute("user_id", ((String[])params.get("id"))[0]);
        }
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/genreList.do", method= RequestMethod.POST)
    public ModelAndView ajaxGenreList(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String, Object>> list = movieService.genreList(params);
        if (list != null) {
            mv.addObject("list", list);
        }
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/movieList.do", method= RequestMethod.POST)
    @ResponseBody
    public ModelAndView ajaxMoveList(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> reqParams = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");

        List<Map<String, Object>> list = movieService.movieList(reqParams);
        if (list != null) {
            mv.addObject("list", list);
        }
        return mv;
    }
}
