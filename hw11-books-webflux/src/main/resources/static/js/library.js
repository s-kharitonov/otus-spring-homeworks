window.onload = function () {
  const SUCCESS_ALERT_CLASS = 'alert-success',
    ERROR_ALERT_CLASS = 'alert-danger',
    SUCCESS_TITLE = 'SUCCESS!',
    ERROR_TITLE = 'ERROR!';

  const $tabs = $('.tab-pane'),
    $createBookTab = $tabs.filter('#create-book'),
    $readBookTab = $tabs.filter('#read-book'),
    $updateBookTab = $tabs.filter('#update-book'),
    $deleteBookTab = $tabs.filter('#delete-book');

  let authorById = {},
      genreById = {};

  getAuthors().then(response => {
    const content = response.json();

    if (response.ok) {
      content.then(authors => {
        let html = authors.reduce((partHtml, author, index) => {
          return partHtml + renderOption(author.id, author.fullName, index === 0);
        }, '');
        authorById = authors.reduce((obj, author) => {
          obj[author.id] = author
          return obj;
        }, {});

        $('#author, #author-for-update').html(html);
      })
    }
  });

  getGenres().then(response => {
    const content = response.json();

    if (response.ok) {
      content.then(genres => {
        let html = genres.reduce((partHtml, genre, index) => {
          return partHtml + renderOption(genre.id, genre.name, index === 0);
        }, '');
        genreById = genres.reduce((obj, genre) => {
          obj[genre.id] = genre
          return obj;
        }, {});

        $('#genre, #genre-for-update').html(html);
      })
    }
  });

  $createBookTab.find('.btn').on('click', function () {
    let $alertContainer = $createBookTab.find('.alert-container'),
      bookCandidate = {
        name: $createBookTab.find('#book-name').val().trim(),
        publicationDate: $createBookTab.find('#book-publication-date').val(),
        printLength: Number($createBookTab.find('#book-print-length').val().trim()),
        authorId: $createBookTab.find('#author option:selected').attr('value'),
        genreId: $createBookTab.find('#genre option:selected').attr('value')
      };

    createBook(bookCandidate).then(response => {
      const content = response.json();

      if (response.ok) {
        content.then(book => {
          $alertContainer.html(renderAlert(`Book with id: ${book.id} created successfully`, SUCCESS_ALERT_CLASS, SUCCESS_TITLE));
        });
      } else {
        content.then(error => renderErrorAlert($alertContainer, error.message));
      }
    });
  });

  $updateBookTab.find('.btn').on('click', function () {
    let $alertContainer = $updateBookTab.find('.alert-container'),
      bookId = $updateBookTab.find('#book-id-for-update').val().trim(),
      genreId = $updateBookTab.find('#genre-for-update option:selected').attr('value'),
      authorId = $updateBookTab.find('#author-for-update option:selected').attr('value'),
      book = {
        id: bookId,
        name: $updateBookTab.find('#book-name-for-update').val().trim(),
        publicationDate: $updateBookTab.find('#book-publication-date-for-update').val(),
        printLength: Number($updateBookTab.find('#book-print-length-for-update').val().trim()),
        author: authorById[authorId],
        genre: genreById[genreId]
      };

    updateBook(book).then(response => {
      if (response.ok) {
        $alertContainer.html(renderAlert(`Book with id: ${bookId} updated successfully`, SUCCESS_ALERT_CLASS, SUCCESS_TITLE));
      } else {
        response.json().then(error => renderErrorAlert($alertContainer, error.message));
      }
    });
  });

  $readBookTab.find('.btn[data-type="getById"]').on('click', function () {
    let bookId = $readBookTab.find('#book-id-for-read').val().trim(),
      $alertContainer = $readBookTab.find('.alert-container');

    getBookById(bookId).then(response => {
      const content = response.json();

      if (response.ok) {
        content.then(book => {
          $alertContainer.html(renderAlert(stringifyJsonPretty(book), SUCCESS_ALERT_CLASS, SUCCESS_TITLE));
        });
      } else {
        content.then(error => renderErrorAlert($alertContainer, error.message));
      }
    });
  });

  $readBookTab.find('.btn[data-type="getAll"]').on('click', function () {
    let $alertContainer = $readBookTab.find('.alert-container');

    getAllBooks().then(response => {
      const content = response.json();

      if (response.ok) {
        content.then(books => {
          $alertContainer.html(renderAlert(stringifyJsonPretty(books), SUCCESS_ALERT_CLASS, SUCCESS_TITLE));
        });
      } else {
        content.then(error => renderErrorAlert($alertContainer, error.message));
      }
    });
  });

  $deleteBookTab.find('.btn').on('click', function () {
    let bookId = $deleteBookTab.find('#book-id-for-delete').val().trim(),
      $alertContainer = $deleteBookTab.find('.alert-container');

    deleteBookById(bookId).then(response => {
      if (response.ok) {
        $alertContainer.html(
          renderAlert(
            `Book with id: ${bookId} deleted successfully`,
            SUCCESS_ALERT_CLASS,
            SUCCESS_TITLE
          )
        );
      } else {
        response.json().then(error => renderErrorAlert($alertContainer, error.message));
      }
    });
  });

  function renderErrorAlert(alertContainer, message) {
    $(alertContainer).html(renderAlert(message, ERROR_ALERT_CLASS, ERROR_TITLE));
  }

  function stringifyJsonPretty(json) {
    return JSON.stringify(json, null, '\t');
  }

  async function createBook(bookCandidate) {
    return await fetch(path + '/api/book/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(bookCandidate)
    });
  }

  async function updateBook(book) {
    return await fetch(path + '/api/book/', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(book)
    });
  }

  async function getAllBooks() {
    return await fetch(path + '/api/book/', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      }
    });
  }

  async function deleteBookById(id) {
    return await fetch(path + '/api/book/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      }
    });
  }

  async function getBookById(id) {
    return await fetch(path + '/api/book/' + id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      }
    });
  }

  async function getGenres() {
    return await fetch(path + '/api/genre/', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      }
    });
  }

  async function getAuthors() {
    return await fetch(path + '/api/author/', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      }
    });
  }

  function renderOption(id, text, selected) {
    if (selected) {
      return `<option selected value="${id}">${text}</option>`;
    } else {
      return `<option value="${id}">${text}</option>`;
    }
  }

  function renderAlert(message, classType, title) {
    return '<div class="alert ' + classType + ' alert-dismissible fade show" role="alert">' +
      '<h4 class="alert-heading">' + title + '</h4>' +
      '<pre>' + message + '</pre>' +
      '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
      '<span aria-hidden="true">&times;</span>' +
      '</button>' +
      '</div>';
  }
}