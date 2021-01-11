package ru.otus.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LibraryPageController {

	@GetMapping(value = "/library")
	public String getAdminPage() {
		return "library";
	}
}
