package seeya.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

public class AbstractDAO {
	protected Log log = LogFactory.getLog(AbstractDAO.class);

	/**
	 * HIRA
	 */
    @Autowired
    @Qualifier("sqlSessionTemplateHira")
    private SqlSessionTemplate sqlSessionHira;

    protected void printQueryId(String queryId) {
        if(log.isDebugEnabled()){
            log.debug("\t QueryID  \t:  " + queryId);
        }
    }
    protected void printQueryId_TEST(String queryId) {
        if(log.isDebugEnabled()){
            log.debug("\t QueryID : HIRA  \t:  " + queryId);
        }
    }

    public Object insert(String queryId, Object params){
        printQueryId_TEST(queryId);
        return sqlSessionHira.insert(queryId, params);
    }

    public Object update(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHira.update(queryId, params);
    }

    public Object delete(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHira.delete(queryId, params);
    }

    public Object selectOne(String queryId){
        //printQueryId(queryId);
        return sqlSessionHira.selectOne(queryId);
    }

    public Object selectOne(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHira.selectOne(queryId, params);
    }

    @SuppressWarnings("rawtypes")
    public List list(String queryId){
        //printQueryId(queryId);
        return sqlSessionHira.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List list(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHira.selectList(queryId,params);
    }

    @SuppressWarnings("unchecked")
    public Object selectPagingList(String queryId, Object params){
        //printQueryId(queryId);
        Map<String, Object> map = (Map<String, Object>) params;

        String strPageIndex = (String) map.get("page");   // 현재 페이지 번호
        String strPageRow   = (String) map.get("listCount");     // 한페이지에 보여줄 행의 수
        int nPageIndex = 0;
        int nPageRow   = 2;

        if (StringUtils.isEmpty(strPageIndex) == false) {
            nPageIndex = Integer.parseInt(strPageIndex) - 1;
        }

        if (StringUtils.isEmpty(strPageRow) == false) {
            nPageRow = Integer.parseInt(strPageRow);
        }

//        map.put("START", (nPageIndex * nPageRow) + 1);
//        map.put("END", (nPageIndex * nPageRow) + nPageRow);
        map.put("START", (nPageIndex * nPageRow));
        map.put("END", nPageRow);

        return sqlSessionHira.selectList(queryId, map);
    }

    /**
     * 2017.10.03 시야 이병덕
     * 페이징 처리를 위한 메소드 추가 : selectPagingListPopup  : 팝업용
     * @param queryId
     * @param params
     * @return
     */
    public Object selectPagingListPopup(String queryId, Object params){
        Map<String, Object> map = (Map<String, Object>) params;

        String strPageIndex = (String) map.get("ppage");          // 현재 페이지 번호
        String strPageRow   = (String) map.get("plistCount");     // 한페이지에 보여줄 행의 수
        int nPageIndex = 0;
        int nPageRow   = 15;

        if (StringUtils.isEmpty(strPageIndex) == false) {
            nPageIndex = Integer.parseInt(strPageIndex) - 1;
        }

        if (StringUtils.isEmpty(strPageRow) == false) {
            nPageRow = Integer.parseInt(strPageRow);
        }

//        map.put("START", (nPageIndex * nPageRow) + 1);
//        map.put("END", (nPageIndex * nPageRow) + nPageRow);
        map.put("START", (nPageIndex * nPageRow));
        map.put("END", nPageRow);

        return sqlSessionHira.selectList(queryId, map);
    }
    
    /*
     * 인사디비
     */
    /*
    @Autowired
    @Qualifier("sqlSessionTemplateEmployee")
    private SqlSessionTemplate sqlSessionEmployee;

    public Object selectOneEMP(String queryId){
        //printQueryId(queryId);
        return sqlSessionEmployee.selectOne(queryId);
    }

    public Object selectOneEMP(String queryId, Object params){
        printQueryId(queryId);
        return sqlSessionEmployee.selectOne(queryId, params);
    }

    @SuppressWarnings("rawtypes")
    public List selectListEMP(String queryId){
        //printQueryId(queryId);
        return sqlSessionEmployee.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List selectListEMP(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionEmployee.selectList(queryId,params);
    }

    @SuppressWarnings("unchecked")
    public Object selectPagingListEMP(String queryId, Object params){
        printQueryId(queryId);
        Map<String, Object> map = (Map<String, Object>) params;

        String strPageIndex = (String) map.get("page");   // 현재 페이지 번호
        String strPageRow   = (String) map.get("listCount");     // 한페이지에 보여줄 행의 수
        int nPageIndex = 0;
        int nPageRow   = 2;

        if (StringUtils.isEmpty(strPageIndex) == false) {
            nPageIndex = Integer.parseInt(strPageIndex) - 1;
        }

        if (StringUtils.isEmpty(strPageRow) == false) {
            nPageRow = Integer.parseInt(strPageRow);
        }

        map.put("START", (nPageIndex * nPageRow) + 1);
        map.put("END", (nPageIndex * nPageRow) + nPageRow);

        return sqlSessionEmployee.selectList(queryId, map);
    }
    */

    /*
     * 하이웨어
     */
    /*
    @Autowired
    @Qualifier("sqlSessionTemplateHiware")
    private SqlSessionTemplate sqlSessionHiware;

    public Object selectOneHiware(String queryId){
        //printQueryId(queryId);
        return sqlSessionHiware.selectOne(queryId);
    }

    public Object selectOneHiware(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHiware.selectOne(queryId, params);
    }

    @SuppressWarnings("rawtypes")
    public List selectListHiware(String queryId){
        //printQueryId(queryId);
        return sqlSessionHiware.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List selectListHiware(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHiware.selectList(queryId,params);
    }

    public Object updateHiware(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionHiware.update(queryId, params);
    }
    */
    /*
     * 메신저 DB
     */
    
    /*
    @Autowired
    @Qualifier("sqlSessionTemplateMessenger")
    private SqlSessionTemplate sqlSessionMessenger;

    public Object insertMSG(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionMessenger.insert(queryId, params);
    }
    
    public Object selectOneMSG(String queryId){
        //printQueryId(queryId);
        return sqlSessionMessenger.selectOne(queryId);
    }

    public Object selectOneMSG(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionMessenger.selectOne(queryId, params);
    }

    @SuppressWarnings("rawtypes")
    public List selectListMSG(String queryId){
        //printQueryId(queryId);
        return sqlSessionMessenger.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List selectListMSG(String queryId, Object params){
        //printQueryId(queryId);
        return sqlSessionMessenger.selectList(queryId,params);
    }
    */
}

