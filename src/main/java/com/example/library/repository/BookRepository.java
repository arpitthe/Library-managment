package com.example.library.repository;

import com.example.library.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private static final RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setTotalCopies(rs.getInt("total_copies"));
        b.setAvailableCopies(rs.getInt("available_copies"));
        return b;
    };

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book save(Book book) {
        String sql = "INSERT INTO books (title, author, total_copies, available_copies) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getTotalCopies());
            ps.setInt(4, book.getAvailableCopies());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            book.setId(key.longValue());
        }
        return book;
    }

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM books ORDER BY id DESC", ROW_MAPPER);
    }

    public Optional<Book> findById(long id) {
        List<Book> list = jdbcTemplate.query("SELECT * FROM books WHERE id = ?", ROW_MAPPER, id);
        return list.stream().findFirst();
    }

    public void updateAvailableCopies(long bookId, int delta) {
        jdbcTemplate.update("UPDATE books SET available_copies = available_copies + ? WHERE id = ?", delta, bookId);
    }
}





