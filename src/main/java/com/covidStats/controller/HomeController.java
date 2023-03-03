package com.covidStats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.covidStats.services.CoronaVirusDataService;

@Controller
@RequestMapping
public class HomeController {

	@Autowired
	CoronaVirusDataService coronaVirusDataService;

	@GetMapping("/home")
	public String Home(Model model) {

		model.addAttribute("Data", coronaVirusDataService.getAllStats());
		return "home";
	}

}
