package seeya.movie.service;

import org.springframework.stereotype.Repository;
import seeya.movie.service.dao.MovieDAO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class MovieService {
    @Resource(name="movieDAO")
    private MovieDAO movieDAO;

    public List<Map<String, Object>> login(Map<String, Object> params) {
        return movieDAO.login(params);
    }

    public int signIn(Map<String, Object> params) {
        return movieDAO.signIn(params);
    }

    public List<Map<String, Object>> genreList(Map<String, Object> params) {
        return movieDAO.genreList(params);
    }

    public List<Map<String, Object>> movieList(Map<String, Object> params) {
        return movieDAO.movieList(params);
    }

    public int addRating(Map<String,Object> params) {
        return movieDAO.addRating(params);
    }

    public List<Map<String, Object>> getRating() {
        return movieDAO.getRating();
    }
}
