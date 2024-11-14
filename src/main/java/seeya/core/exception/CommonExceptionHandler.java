package seeya.core.exception;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonExceptionHandler implements ExceptionHandler {
    protected Log log = LogFactory.getLog(this.getClass());

    public CommonExceptionHandler() {
    }

    public void occur(Exception ex, String packageName) {
        String cs = ex.getClass().toString().trim();
        int pt = cs.lastIndexOf(".");
        cs.substring(pt + 1);
        this.log.error("Exeption Handler(CommonExceptionHandler)..." + packageName);
        this.log.error("ex.getMessage()=" + ex.getMessage());
        ex.printStackTrace();
    }
}
