package com.example.library.repository;

import com.example.library.domain.Loan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class LoanRepository {
    private static final RowMapper<Loan> ROW_MAPPER = (rs, rowNum) -> {
        Loan l = new Loan();
        l.setId(rs.getLong("id"));
        l.setBookId(rs.getLong("book_id"));
        l.setBorrowerName(rs.getString("borrower_name"));
        Date issued = rs.getDate("issued_on");
        Date due = rs.getDate("due_on");
        Date returned = rs.getDate("returned_on");
        l.setIssuedOn(issued != null ? issued.toLocalDate() : null);
        l.setDueOn(due != null ? due.toLocalDate() : null);
        l.setReturnedOn(returned != null ? returned.toLocalDate() : null);
        return l;
    };

    private final JdbcTemplate jdbcTemplate;

    public LoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Loan save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, borrower_name, issued_on, due_on) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, loan.getBookId());
            ps.setString(2, loan.getBorrowerName());
            ps.setDate(3, Date.valueOf(loan.getIssuedOn()));
            ps.setDate(4, Date.valueOf(loan.getDueOn()));
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            loan.setId(key.longValue());
        }
        return loan;
    }

    public Optional<Loan> findActiveByBookId(long bookId) {
        List<Loan> list = jdbcTemplate.query(
                "SELECT * FROM loans WHERE book_id = ? AND returned_on IS NULL ORDER BY id DESC LIMIT 1",
                ROW_MAPPER, bookId);
        return list.stream().findFirst();
    }

    public void markReturned(long loanId, LocalDate returnedOn) {
        jdbcTemplate.update("UPDATE loans SET returned_on = ? WHERE id = ?", Date.valueOf(returnedOn), loanId);
    }
}






