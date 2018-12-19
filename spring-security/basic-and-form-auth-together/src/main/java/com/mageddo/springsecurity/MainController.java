package com.mageddo.springsecurity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@RequestMapping("/api/users")
	@ResponseBody
	public String body(){
		return "[Joao, Maria]";
	}
}
