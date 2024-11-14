package seeya.schedule.service;

// import seeya.ai.service.PatrolWorkManageService;
// import seeya.management.service.BlacklistService;
// import seeya.management.service.EmployeeService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @className : comnonScheduleService
 * @description : comnonScheduleService 클래스
 * 
 * @author 정보보호 이병덕
 * @since 2017.10.27
 * @version 1.0
 * @see
 * 
 * -Copyright (C) by seeya All right reserved.
 */
//@Service("scheduleService")
public class comnonScheduleService {
//    Logger log = Logger.getLogger(this.getClass());

    // @Resource
    // private EmployeeService employeeService;
    
    // @Resource
    // private BlacklistService blacklistService;
    
    // @Resource
    // private PatrolWorkManageService patrolWorkManageService;


    /**
     * 인사디비 정보 업데이트
     * @throws Exception
     */
    @Scheduled(cron="0 10 05 * * *") // 매일 새벽 5시 10분에 실행
    public void procEmployee() throws Exception{
        // employeeService.procEmployee();
    }


    
    /**
     * 구급일자 소견 내 주취 키워드 발견 시, 블랙리스트 등록
     * 2021.12.06, 김주승
     * @throws Exception
     */
    @Scheduled(cron="0 00 04 * * *") // 매일 새벽 4시에 설정
    //@Scheduled(cron="0 * * * * *") // 테스트
    public void regBlacklistAuto() throws Exception{
        // blacklistService.regBlacklistAuto();
    }
    
    
    
    /**
     * 예측출동 건에 대한 실제출동 건 재난번호 탐색 매핑
     * 2021.12.09, 김주승
     * @throws Exception
     */
    /* //일단 비활성화
    @Scheduled(cron="0 30 04 * * *") // 매일 새벽 4시30분에 설정
    //@Scheduled(cron="0 * * * * *") // 테스트
    public void updateRealDsrSeqAuto() throws Exception{
        patrolWorkManageService.updateRealDsrSeqAuto();
    }
    */
}
