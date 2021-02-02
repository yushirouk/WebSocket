package com.polling.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.polling.vo.PollingVo;

@Controller
public class MainController {
	
	//클라이언트의 cpCd와 txSeqNo 설정.
	@GetMapping("/")
	String main(Model model) throws SQLException {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String txSeqNo = df.format(cal.getTime());
		
		PollingVo vo = new PollingVo();
		vo.setCpCd("V06880000000");
		vo.setTxSeqNo(txSeqNo);
		
		model.addAttribute("initForm", vo);
		
		return "index";
	}
}
