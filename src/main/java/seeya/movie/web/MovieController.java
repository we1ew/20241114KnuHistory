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
        String name = String.valueOf(session.getAttribute("user_name"));
        if(!name.equals("null")) {
            mv.addObject("name", name);
            mv.addObject("user_id", session.getAttribute("user_id"));
        }
        return mv;
    }

    @RequestMapping(value="/Movie/list.do")
    public ModelAndView ListPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/Movie/movie_list");
        return mv;
    }

    @RequestMapping(value="/Movie/login.do")
    public ModelAndView LoginPage(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv;
        if(!"null".equals(String.valueOf(session.getAttribute("user_id")))) {
            mv = new ModelAndView("redirect:/Movie/main.do");
        } else {
            mv = new ModelAndView("/Movie/movie_login");
        }
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
        List<Map<String, Object>> result = movieService.login(params);
        mv.addObject("result", result.size() == 1);
        if(result.size() == 1) {
            session.setAttribute("user_id", result.get(0).get("user_id"));
            session.setAttribute("user_name", result.get(0).get("name"));
        }
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/logout.do", method= RequestMethod.POST)
    public ModelAndView ajaxLogout(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        if(session.getAttribute("user_id") != null) {
            session.invalidate();
            mv.addObject("result", true);
        } else {
            mv.addObject("msg", "잘못된 접근입니다.");
        }
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/signIn.do", method= RequestMethod.POST)
    public ModelAndView ajaxSignIn(HttpServletRequest request, HttpSession session) throws Exception {
        Map<String, Object> params = request.getParameterMap();
        ModelAndView mv = new ModelAndView("jsonView");
        int result = movieService.signIn(params);
        switch(result) {
            case -1:
                mv.addObject("result", false);
                mv.addObject("msg", "필수 입력값이 전달되지 않았습니다.");
                break;
            case 0:
                mv.addObject("result", false);
                mv.addObject("msg", "해당 아이디는 사용하실 수 없습니다.");
                break;
            case 1:
                mv.addObject("result", true);
                break;
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

    @RequestMapping(value="/Movie/ajax/addRating.do", method= RequestMethod.POST)
    @ResponseBody
    public ModelAndView ajaxAddRating(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        Map<String, Object> reqParams = request.getParameterMap();
        Map<String, Object> map = new HashMap<>();

        map.put("movie_id", ((String[])reqParams.get("movie_id"))[0]);
        map.put("rating", ((String[])reqParams.get("rating"))[0]);
        map.put("user_id", session.getAttribute("user_id"));

        int result = movieService.addRating(map);
        mv.addObject("result", result);
        return mv;
    }

    @RequestMapping(value="/Movie/ajax/getRating.do", method= RequestMethod.POST)
    @ResponseBody
    public ModelAndView ajaxGetRating(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("jsonView");
        List<Map<String, Object>> result = movieService.getRating();
        mv.addObject("result", result);
        return mv;
    }
}
