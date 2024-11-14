package seeya.sample.service.dao;

import seeya.core.dao.AbstractDAO;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

@Repository("sampleDAO")
public class SampleDAO extends AbstractDAO {
    protected Log log = LogFactory.getLog(this.getClass());
    
    /*
    @SuppressWarnings({ "unchecked" })
    public List<Map<String, Object>> getMimeType(Map<String, Object> map) throws Exception{
        return (List<Map<String,Object>>) list("mimeType.getMimeType" , map);
    }
     */

    public List<Map<String, Object>> testSelect(Map<String, Object> map) throws Exception{
        return (List<Map<String,Object>>) list("sample.testSelect", map);
    }
    
}