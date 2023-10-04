package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.customer.dto.CustomerDTO;
import com.my.exception.AddException;

public class SignupController extends CustomerController {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// CORS 문제 해결
//		res.setHeader("Access-Control-Allow-Origin", "http://10.155.253.8:5500");
		res.setHeader("Access-Control-Allow-Origin", "http://192.168.1.105:5500");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		
		res.setContentType("application/json;charset=utf-8");
		
		// 응답 출력스트림 얻기
		PrintWriter out = res.getWriter();
		
		// jackson 라이브러리에서 제공하는 ObjectMapper 클래스 사용하기
		ObjectMapper mapper = new ObjectMapper(); // JSON 문자열 만드는 API
		
		// map에 넣을 
		Map<String, Object> map = new HashMap<>(); 
		
		// 요청 전달데이터 얻기
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		String nickname = req.getParameter("nickname");
		String name = req.getParameter("name");
		String birthday = req.getParameter("birthday");
		String phone = req.getParameter("phone");
		String email = req.getParameter("email");
		
		// 요청전달 데이터 담을 dto 객체 생성
		CustomerDTO dto = new CustomerDTO();
		
		try {
			
			service.singup(dto);
			
			map.put("status", 1);
			map.put("msg", "회원가입 성공");
			
		} catch (AddException e) {
			
			map.put("status", 0);
			map.put("msg", e.getMessage());
		} // try-catch
		
		// JSON문자열 응답
		out.print(mapper.writeValueAsString(dto));
		
		return null;
		
	} // execute

} // end class
