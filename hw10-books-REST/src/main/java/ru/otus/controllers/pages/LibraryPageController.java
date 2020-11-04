package ru.otus.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.domain.dto.AuthorDto;
import ru.otus.domain.dto.GenreDto;
import ru.otus.services.AuthorsService;
import ru.otus.services.GenresService;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class LibraryPageController {

	private final GenresService genresService;
	private final AuthorsService authorsService;

	public LibraryPageController(final GenresService genresService,
								 final AuthorsService authorsService) {
		this.genresService = genresService;
		this.authorsService = authorsService;
	}

	@GetMapping(value = "/library")
	public String getAdminPage(Model model) {
		var genres = genresService.getAll()
				.stream()
				.map(GenreDto::new)
				.sorted(Comparator.comparing(GenreDto::getName))
				.collect(Collectors.toUnmodifiableList());
		var authors = authorsService.getAll()
				.stream()
				.map(AuthorDto::new)
				.sorted(Comparator.comparing(AuthorDto::getFullName))
				.collect(Collectors.toUnmodifiableList());

		model.addAttribute("genres", genres);
		model.addAttribute("authors", authors);

		return "library";
	}
}
