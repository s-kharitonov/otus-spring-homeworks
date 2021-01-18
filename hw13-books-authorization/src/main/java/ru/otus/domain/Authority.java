package ru.otus.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "AUTHORITIES")
public class Authority implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_s")
	@SequenceGenerator(name = "authorities_s", sequenceName = "AUTHORITIES_S", allocationSize = 1)
	@Column(name = "AUTHORITY_ID")
	private Long id;

	@Column(name = "AUTHORITY", nullable = false, unique = true, length = 255)
	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(final String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "Authority{" +
				"id=" + id +
				", authority='" + authority + '\'' +
				'}';
	}
}
