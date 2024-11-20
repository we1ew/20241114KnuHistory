package seeya.movie.service.dao;

import org.springframework.stereotype.Repository;
import seeya.core.dao.AbstractDAO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MovieDAO extends AbstractDAO {
    public boolean login(Map<String, Object> params) {
        HashMap<String, Object> map = new HashMap<>();
        if(params.containsKey("id") && params.containsKey("pw")) {
            map.put("id", ((String[])params.get("id"))[0]);
            map.put("pw", ((String[])params.get("pw"))[0]);
        }
        int result = ((int) selectOne("movie.login", map));
        return result == 1;
    }

    public List<Map<String, Object>> genreList(Map<String, Object> params) {
        return (List<Map<String, Object>>) list("movie.genreList", params);
    }

    public List<Map<String, Object>> movieList(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if(params.containsKey("filter")) {
            String filter = ((String[])params.get("filter"))[0];
            List<String> filterList = Arrays.asList(filter.split(","));

            map.put("filter", filterList);
        }

        return (List<Map<String, Object>>) list("movie.movieList", map);
    }
}
