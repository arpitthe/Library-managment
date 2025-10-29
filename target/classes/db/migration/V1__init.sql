CREATE TABLE IF NOT EXISTS books (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  total_copies INT NOT NULL,
  available_copies INT NOT NULL
);

CREATE TABLE IF NOT EXISTS loans (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  book_id BIGINT NOT NULL,
  borrower_name VARCHAR(255) NOT NULL,
  issued_on DATE NOT NULL,
  due_on DATE NOT NULL,
  returned_on DATE NULL,
  CONSTRAINT fk_loans_books FOREIGN KEY (book_id) REFERENCES books(id)
);





