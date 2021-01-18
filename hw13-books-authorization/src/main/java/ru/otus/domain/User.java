package ru.otus.domain;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_s")
	@SequenceGenerator(name = "users_s", sequenceName = "USERS_S", allocationSize = 1)
	@Column(name = "USER_ID")
	private Long id;

	@Column(name = "USERNAME", nullable = false, unique = true, length = 255)
	private String username;

	@Column(name = "PASSWORD", nullable = false, length = 255)
	private String password;

	@Column(name = "ACCOUNT_NON_EXPIRED", nullable = false)
	private boolean accountNonExpired;

	@Column(name = "ACCOUNT_NON_LOCKED", nullable = false)
	private boolean accountNonLocked;

	@Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false)
	private boolean credentialsNonExpired;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;

	@ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
	@JoinTable(name = "USER_AUTHORITIES",
			joinColumns = {@JoinColumn(name = "USER_ID")},
			inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID")})
	private Set<Authority> authorities;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(final boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(final boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(final boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(final Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", accountNonExpired=" + accountNonExpired +
				", accountNonLocked=" + accountNonLocked +
				", credentialsNonExpired=" + credentialsNonExpired +
				", enabled=" + enabled +
				", authorities=" + authorities +
				'}';
	}
}
