package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.domain.Loan;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public LibraryService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    public Book addBook(Book book) {
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Loan issueBook(long bookId, String borrowerName, int loanDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies");
        }
        bookRepository.updateAvailableCopies(bookId, -1);

        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setBorrowerName(borrowerName);
        LocalDate now = LocalDate.now();
        loan.setIssuedOn(now);
        loan.setDueOn(now.plusDays(loanDays));
        return loanRepository.save(loan);
    }

    @Transactional
    public void returnBook(long bookId) {
        Loan loan = loanRepository.findActiveByBookId(bookId)
                .orElseThrow(() -> new IllegalStateException("No active loan for book: " + bookId));
        loanRepository.markReturned(loan.getId(), LocalDate.now());
        bookRepository.updateAvailableCopies(bookId, 1);
    }
}






