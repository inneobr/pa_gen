package br.coop.integrada.api.pa.aplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Index {

	@GetMapping("/swagger-pa-portal")
	public String index() {
		return "/swagger-pa-portal/openapi/swagger-ui.html";
	}

	@GetMapping("/api/genesis")
	public String documentation() {
		return "/swagger-pa-portal/openapi/swagger-ui.html";
	}
}
