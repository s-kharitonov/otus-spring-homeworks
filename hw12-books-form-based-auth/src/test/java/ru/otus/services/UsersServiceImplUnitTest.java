package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Authority;
import ru.otus.domain.User;
import ru.otus.exceptions.UserServiceException;
import ru.otus.repositories.UsersRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("service for users")
class UsersServiceImplUnitTest {

	private static final String USERNAME = "s-kharitonov";
	private static final boolean CREDENTIALS_NON_EXPIRED = true;
	private static final boolean ACCOUNT_NON_LOCKED = true;
	private static final boolean ACCOUNT_NON_EXPIRED = true;
	private static final boolean ENABLED = true;
	private static final Set<Authority> AUTHORITIES = new HashSet<>();
	private static final String PASSWORD = "password";

	@Configuration
	@Import(UsersServiceImpl.class)
	public static class UserServiceConfig {

	}

	@Autowired
	private UsersService usersService;

	@MockBean
	private UsersRepository usersRepository;

	@Test
	@DisplayName("should return user by username")
	public void shouldReturnUserByUsername() {
		var expectedUser = buildUser();
		given(usersRepository.findByUsername(USERNAME)).willReturn(Optional.of(expectedUser));

		assertThat(usersService.getByUsername(USERNAME))
				.get()
				.isEqualToComparingFieldByField(expectedUser);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("should throw UserServiceException for incorrect username")
	public void shouldThrowExceptionForIncorrectUsername(final String username) {
		assertThrows(UserServiceException.class, () -> usersService.getByUsername(username));
	}

	private User buildUser() {
		var user = new User();

		user.setId(1L);
		user.setUsername(USERNAME);
		user.setPassword(PASSWORD);
		user.setAuthorities(AUTHORITIES);
		user.setEnabled(ENABLED);
		user.setAccountNonExpired(ACCOUNT_NON_EXPIRED);
		user.setAccountNonLocked(ACCOUNT_NON_LOCKED);
		user.setCredentialsNonExpired(CREDENTIALS_NON_EXPIRED);

		return user;
	}
}