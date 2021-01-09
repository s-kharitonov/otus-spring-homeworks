package ru.otus.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.services.UsersService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsersService usersService;

	public UserDetailsServiceImpl(final UsersService usersService) {
		this.usersService = usersService;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return usersService.getByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("user not found!"));
	}
}
