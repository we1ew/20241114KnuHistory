package seeya.sample.service;

import java.util.Map;
import java.util.List;

import seeya.sample.service.dao.SampleDAO;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service("sampleService")
public class SampleService {
    Logger log = Logger.getLogger(this.getClass());
    
    @Resource(name="sampleDAO")
    private SampleDAO sampleDAO;

    // @Resource(name="commonService")
    // private CommonService commonService;
    
    //     public List<Map<String, Object>> testSelect(Map<String, Object> map) throws Exception{

    public List<Map<String, Object>> testSelect(Map<String, Object> map) throws Exception{
        return sampleDAO.testSelect(map);
    }

    
}
