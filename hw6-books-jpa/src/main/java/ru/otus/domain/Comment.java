package ru.otus.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "COMMENTS")
public class Comment {
	@Id
	@Column(name = "COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_s")
	@SequenceGenerator(name = "comment_s", sequenceName = "COMMENTS_S", allocationSize = 1)
	private Long id;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Column(name = "TEXT", nullable = false, length = 255)
	private String text;

	public Comment() {
	}

	private Comment(final Builder builder) {
		this.id = builder.id;
		this.text = builder.text;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id=" + id +
				", text='" + text + '\'' +
				'}';
	}

	public static class Builder {
		private Long id;
		private String text;

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder text(String text) {
			this.text = text;
			return this;
		}

		public Comment build() {
			return new Comment(this);
		}
	}
}
