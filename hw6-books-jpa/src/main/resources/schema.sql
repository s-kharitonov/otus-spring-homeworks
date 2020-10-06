CREATE SEQUENCE AUTHORS_S INCREMENT BY 1 MINVALUE 1 NO CACHE;

CREATE SEQUENCE GENRES_S INCREMENT BY 1 MINVALUE 1 NO CACHE;

CREATE SEQUENCE BOOKS_S INCREMENT BY 1 MINVALUE 1 NO CACHE;

CREATE TABLE IF NOT EXISTS AUTHORS
(
    AUTHOR_ID BIGINT        NOT NULL DEFAULT AUTHORS_S.NEXTVAL PRIMARY KEY,
    NAME      VARCHAR2(255) NOT NULL,
    SURNAME   VARCHAR2(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID BIGINT               NOT NULL DEFAULT GENRES_S.NEXTVAL PRIMARY KEY,
    NAME     VARCHAR2(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS BOOKS
(
    BOOK_ID          BIGINT               NOT NULL DEFAULT BOOKS_S.NEXTVAL PRIMARY KEY,
    AUTHOR_ID        BIGINT               NOT NULL,
    GENRE_ID         BIGINT               NOT NULL,
    NAME             VARCHAR2(255) UNIQUE NOT NULL,
    PUBLICATION_DATE DATE                 NOT NULL,
    PRINT_LENGTH     INT                  NOT NULL,
    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS (AUTHOR_ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRES (GENRE_ID) ON DELETE CASCADE
);