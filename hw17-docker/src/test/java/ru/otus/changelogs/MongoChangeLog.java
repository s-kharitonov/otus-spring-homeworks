package ru.otus.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.BookComment;
import ru.otus.domain.Genre;
import ru.otus.repositories.AuthorsRepository;
import ru.otus.repositories.BookCommentsRepository;
import ru.otus.repositories.BooksRepository;
import ru.otus.repositories.GenresRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "001")
public class MongoChangeLog {

	private static final String KAREN = "Karen";
	private static final String ROBERT = "Robert";
	private static final String MAXIM = "Maxim";
	private static final String NICHOLAS = "Nicholas";
	private static final String FANTASY = "Fantasy";
	private static final String PROGRAMMING_LANGUAGES = "Programming Languages";
	private static final String LITERATURE = "Literature";
	private static final String ROMANCE = "Romance";
	private static final String S_KHARITONOV = "s-kharitonov";
	private static final String ARCHITECTS_OF_MEMORY = "Architects of Memory";
	private static final String ENGINES_OF_OBLIVION = "Engines of Oblivion";
	private static final String CLEAN_CODE_A_HANDBOOK_OF_AGILE_SOFTWARE_CRAFTSMANSHIP = "Clean Code: A Handbook of Agile Software Craftsmanship";
	private static final String CLEAN_ARCHITECTURE_A_CRAFTSMAN_S_GUIDE_TO_SOFTWARE_STRUCTURE_AND_DESIGN = "Clean Architecture: A Craftsman's Guide to Software Structure and Design";
	private static final String RUTHIE_FEAR_A_NOVEL = "Ruthie Fear: A Novel";
	private static final String COME_WEST_AND_SEE_STORIES = "Come West and See: Stories";
	private static final String THE_RETURN = "The Return";
	private static final String THE_RESCUE = "The Rescue";

	private final Map<String, Author> authorsByName = new HashMap<>();
	private final Map<String, Genre> genresByName = new HashMap<>();
	private final Map<String, Book> booksByName= new HashMap<>();

	@ChangeSet(order = "000", id= "dropDb", author = S_KHARITONOV, runAlways = true)
	public void dropDb(final MongoDatabase mongoDatabase) {
		mongoDatabase.drop();
	}

	@ChangeSet(order = "001", id = "initAuthors", author = S_KHARITONOV, runAlways = true)
	public void initAuthors(final AuthorsRepository authorsRepository) {
		var karen = new Author.Builder()
				.name(KAREN)
				.surname("Osborne")
				.build();
		var robert = new Author.Builder()
				.name(ROBERT)
				.surname("Martin")
				.build();
		var maxim = new Author.Builder()
				.name(MAXIM)
				.surname("Loskutoff")
				.build();
		var nikolas = new Author.Builder()
				.name(NICHOLAS)
				.surname("Sparks")
				.build();

		authorsByName.put(KAREN, authorsRepository.save(karen).block());
		authorsByName.put(ROBERT, authorsRepository.save(robert).block());
		authorsByName.put(MAXIM, authorsRepository.save(maxim).block());
		authorsByName.put(NICHOLAS, authorsRepository.save(nikolas).block());
	}

	@ChangeSet(order = "002", id = "initGenres", author = S_KHARITONOV, runAlways = true)
	public void initGenres(final GenresRepository genresRepository) {
		var fantasy = new Genre.Builder().name(FANTASY).build();
		var programs = new Genre.Builder().name(PROGRAMMING_LANGUAGES).build();
		var literature = new Genre.Builder().name(LITERATURE).build();
		var romance = new Genre.Builder().name(ROMANCE).build();

		genresByName.put(FANTASY, genresRepository.save(fantasy).block());
		genresByName.put(PROGRAMMING_LANGUAGES, genresRepository.save(programs).block());
		genresByName.put(LITERATURE, genresRepository.save(literature).block());
		genresByName.put(ROMANCE, genresRepository.save(romance).block());
	}

	@ChangeSet(order = "003", id = "initBooks", author = S_KHARITONOV, runAlways = true)
	public void initBooks(final BooksRepository booksRepository) {
		var architects = new Book.Builder()
				.name(ARCHITECTS_OF_MEMORY)
				.printLength(345)
				.publicationDate(new Date())
				.genre(genresByName.get(FANTASY))
				.author(authorsByName.get(KAREN))
				.build();
		var engines = new Book.Builder()
				.name(ENGINES_OF_OBLIVION)
				.printLength(500)
				.publicationDate(new Date())
				.genre(genresByName.get(FANTASY))
				.author(authorsByName.get(KAREN))
				.build();
		var cleanCode = new Book.Builder()
				.name(CLEAN_CODE_A_HANDBOOK_OF_AGILE_SOFTWARE_CRAFTSMANSHIP)
				.printLength(447)
				.publicationDate(new Date())
				.genre(genresByName.get(PROGRAMMING_LANGUAGES))
				.author(authorsByName.get(ROBERT))
				.build();
		var cleanArchitecture = new Book.Builder()
				.name(CLEAN_ARCHITECTURE_A_CRAFTSMAN_S_GUIDE_TO_SOFTWARE_STRUCTURE_AND_DESIGN)
				.printLength(430)
				.publicationDate(new Date())
				.genre(genresByName.get(PROGRAMMING_LANGUAGES))
				.author(authorsByName.get(ROBERT))
				.build();
		var ruthieFear = new Book.Builder()
				.name(RUTHIE_FEAR_A_NOVEL)
				.printLength(304)
				.publicationDate(new Date())
				.genre(genresByName.get(LITERATURE))
				.author(authorsByName.get(MAXIM))
				.build();
		var comeWest = new Book.Builder()
				.name(COME_WEST_AND_SEE_STORIES)
				.printLength(240)
				.publicationDate(new Date())
				.genre(genresByName.get(LITERATURE))
				.author(authorsByName.get(MAXIM))
				.build();
		var theReturn = new Book.Builder()
				.name(THE_RETURN)
				.printLength(368)
				.publicationDate(new Date())
				.genre(genresByName.get(ROMANCE))
				.author(authorsByName.get(NICHOLAS))
				.build();
		var theRescue = new Book.Builder()
				.name(THE_RESCUE)
				.printLength(364)
				.publicationDate(new Date())
				.genre(genresByName.get(ROMANCE))
				.author(authorsByName.get(NICHOLAS))
				.build();

		booksByName.put(ARCHITECTS_OF_MEMORY, booksRepository.save(architects).block());
		booksByName.put(ENGINES_OF_OBLIVION, booksRepository.save(engines).block());
		booksByName.put(CLEAN_CODE_A_HANDBOOK_OF_AGILE_SOFTWARE_CRAFTSMANSHIP, booksRepository.save(cleanCode).block());
		booksByName.put(CLEAN_ARCHITECTURE_A_CRAFTSMAN_S_GUIDE_TO_SOFTWARE_STRUCTURE_AND_DESIGN, booksRepository.save(cleanArchitecture).block());
		booksByName.put(RUTHIE_FEAR_A_NOVEL, booksRepository.save(ruthieFear).block());
		booksByName.put(COME_WEST_AND_SEE_STORIES, booksRepository.save(comeWest).block());
		booksByName.put(THE_RETURN, booksRepository.save(theReturn).block());
		booksByName.put(THE_RESCUE, booksRepository.save(theRescue).block());
	}

	@ChangeSet(order = "004", id = "initBookComments", author = S_KHARITONOV, runAlways = true)
	public void initBookComments(final BookCommentsRepository bookCommentsRepository) {
		var perfectComment = new BookComment.Builder()
				.text("perfect book!")
				.book(booksByName.get(ARCHITECTS_OF_MEMORY))
				.build();
		var amazingComment = new BookComment.Builder()
				.text("amazing book!")
				.book(booksByName.get(ARCHITECTS_OF_MEMORY))
				.build();
		var interestingComment = new BookComment.Builder()
				.text("interesting book!")
				.book(booksByName.get(ARCHITECTS_OF_MEMORY))
				.build();

		bookCommentsRepository.save(perfectComment).block();
		bookCommentsRepository.save(amazingComment).block();
		bookCommentsRepository.save(interestingComment).block();
	}
}
