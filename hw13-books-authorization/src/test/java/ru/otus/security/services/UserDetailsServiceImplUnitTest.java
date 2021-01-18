package ru.otus.security.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.domain.Authority;
import ru.otus.domain.User;
import ru.otus.services.UsersService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("user details service implementation for spring security")
class UserDetailsServiceImplUnitTest {

	private static final String USERNAME = "s-kharitonov";
	private static final boolean CREDENTIALS_NON_EXPIRED = true;
	private static final boolean ACCOUNT_NON_LOCKED = true;
	private static final boolean ACCOUNT_NON_EXPIRED = true;
	private static final boolean ENABLED = true;
	private static final Set<Authority> AUTHORITIES = new HashSet<>();
	private static final String PASSWORD = "password";

	@Configuration
	@Import(UserDetailsServiceImpl.class)
	public static class UserDetailsConfig {

	}

	@Autowired
	private UserDetailsService userDetailsService;

	@MockBean
	private UsersService usersService;

	@Test
	@DisplayName("should return user details by username")
	public void shouldReturnUserDetailsByUsername() {
		var expectedUser = buildUser();

		given(usersService.getByUsername(USERNAME)).willReturn(Optional.of(expectedUser));

		assertThat(userDetailsService.loadUserByUsername(USERNAME))
				.isNotNull()
				.isEqualToComparingFieldByField(expectedUser);
	}

	@Test
	@DisplayName("should throw UsernameNotFoundException when user details not founded by username")
	public void shouldThrowExceptionWhenUserDetailsNotFoundByUsername() {
		given(usersService.getByUsername(USERNAME)).willReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(USERNAME));
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