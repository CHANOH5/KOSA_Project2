package com.my.rank.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.exception.FindException;
import com.my.qna.dto.QnaBoardCommentDTO;
import com.my.rank.dao.RankDAO;
import com.my.rank.dao.RankDAOImpl;
import com.my.rank.dto.RankDTO;
import com.my.task.dto.MemberTaskDTO;
import com.my.task.dto.TaskDTO;
import com.my.team.dto.AttendanceDTO;
import com.my.team.dto.TeamMemberDTO;

public class RankServiceImpl implements RankService {
	
	private RankDAO rankDao;
	private static RankServiceImpl service = new RankServiceImpl();
	public RankServiceImpl() {
		rankDao = new RankDAOImpl();
	}
	public static RankServiceImpl getInstance() {
		return service;
	}
	
	@Override
	public List<RankDTO> findByMonth(Integer teamNo, Integer month) throws FindException {
		List<RankDTO> ranklist = rankDao.selectByMonth(teamNo, month);
		return ranklist;
	}

	@Override
	public Map<String, Object> calculateTotalScore(Integer teamNo, String rankDate, Integer month) throws FindException {
		List<TeamMemberDTO> tmlist = rankDao.selectMemberId(teamNo);
		List<AttendanceDTO> attlist = rankDao.selectAttendanceDay(teamNo, rankDate, month);
		List<TaskDTO> tasknumlist = rankDao.countMonthlyTask(teamNo, month);
		List<MemberTaskDTO> mtlist = rankDao.selectTaskScore(teamNo, month);
		List<TaskDTO> rslist = rankDao.selectReviewScore(teamNo, month);
		List<QnaBoardCommentDTO> qnalist = rankDao.selectQnAScore(teamNo, month);
		
		//id 출력하기
		for (TeamMemberDTO tmdto : tmlist) {
			String id = tmdto.getId();
		}
		
		// id별 total score를 담을 맵 
		Map<String, Object> totalScoreMap = new HashMap();
		
		//id별 출석률 계산 
		Map<String, Double> attmap = new HashMap<>();
		for (AttendanceDTO attdto : attlist) {
			String id = attdto.getAttendanceId();
			
			// 출석률 = 출석인증일수 / 월별 총 일수 * 100
			Integer attendanceday = attdto.getAttendanceday();
			Integer monthday = attdto.getMonthday();
			Double attendancerate = ((double)attendanceday/monthday)*100;
			attmap.put(id, attendancerate);
		}
		
		//id별 평균 task score 계산 
		Map<String, Double> tsmap = new HashMap<>();
		Integer monthlytasknum = tasknumlist.get(0).getMonthlyTaskNum();
		for (MemberTaskDTO mtdto : mtlist) {
			String id = mtdto.getId();
			
			// 과제점수평균 = 과제 점수 총합/월에 출제된 과제 총 개수
			Double totaltaskscore = mtdto.getTotalScore();
			Double avgtaskscore = (double)(totaltaskscore/monthlytasknum);
			tsmap.put(id, avgtaskscore);
		}
		
		//id별 평균 리뷰점수 누적합 계산 
		Map<String, Double> rsmap = new HashMap<>();
		for (TaskDTO tsdto : rslist) {	
			String id = tsdto.getId();
			
			// 출제한 과제 평균 리뷰점수 누적합
			Double totalreviewscore = tsdto.getTotalReviewscore();
			rsmap.put(id, totalreviewscore);
		}

		//id별 큐엔에이 채택 점수 누적합 계산 
		Map<String, Double> qnamap = new HashMap<>();
		for (QnaBoardCommentDTO qnadto : qnalist) {
			String id = qnadto.getTeammemberId();
			
			// 큐엔에이 채택 점수 누적합
			Double qnapickedscore = qnadto.getPickedNum();	
			qnamap.put(id, qnapickedscore);
		}
		
		//id별 Total Score 계산 
		//총 점수 = 출석률*0.1 + 과제점수평균 + 리뷰점수 + 큐엔에이 채택점수
		for (String id : attmap.keySet()) {
			Double attendancerate = attmap.getOrDefault(id, 0.0);
			Double avgtaskscore = tsmap.getOrDefault(id, 0.0);
			Double totalreviewscore = rsmap.getOrDefault(id, 0.0);
			Double qnapickedscore = qnamap.getOrDefault(id, 0.0);
			
			//총점 계산 
			Double totalscore = Math.round(((attendancerate*0.1) + avgtaskscore + totalreviewscore + qnapickedscore)*100)/100.0;
			totalScoreMap.put(id, totalscore);
		}
		return totalScoreMap;
	}
	
	public static void main(String[] args) {
		RankServiceImpl service = new RankServiceImpl();
		try {
			System.out.println(service.findByMonth(9999, 10));
			System.out.println(service.calculateTotalScore(9999, "2023-10-01", 10));
		} catch (FindException e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public List<AttendanceDTO> calculateAttendanceRate(Integer teamNo, String attendanceDate, Integer month)
//			throws FindException {
//		// 출석률 = 출석인증일수 / 월별 총 일수 * 100
//		List<AttendanceDTO> list = rankDao.selectAttendanceDay(teamNo, attendanceDate, month);
//		for (int i = 0; i < list.size(); i++) {
//			Integer attendanceday = list.get(i).getAttendanceday();
//			Integer monthday = list.get(i).getMonthday();
//			Double attendancerate = ((double)attendanceday/monthday)*100;
//		}
//		return list;
//	}
//	
//	@Override
//	public List<TaskDTO> calculateTaskScore(Integer teamNo, Integer month) throws FindException {
//		// 과제점수평균 = 과제 점수 총합/월에 출제된 과제 총 개수
//		return null;
//	}
//	
//	@Override
//	public List<TaskDTO> calculateReviewScore(Integer teamNo, Integer month) throws FindException {
//		// 출제한 과제 평균 리뷰점수 누적합
//		return null;
//	}
//	
//	@Override
//	public List<QnACommentDTO> calculateQnAScore(Integer teamNo, Integer month) throws FindException {
//		// 큐엔에이 채택 점수 누적합
//		return null;
//	}

}
