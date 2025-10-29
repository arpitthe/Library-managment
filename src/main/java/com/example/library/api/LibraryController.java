package com.example.library.api;

import com.example.library.domain.Book;
import com.example.library.domain.Loan;
import com.example.library.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(libraryService.addBook(book));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> listBooks() {
        return ResponseEntity.ok(libraryService.listBooks());
    }

    public record IssueRequest(@Min(1) Long bookId, @NotBlank String borrowerName, @Min(1) Integer days) {}

    @PostMapping("/loans/issue")
    public ResponseEntity<Loan> issue(@Valid @RequestBody IssueRequest req) {
        return ResponseEntity.ok(libraryService.issueBook(req.bookId(), req.borrowerName(), req.days()));
    }

    public record ReturnRequest(@Min(1) Long bookId) {}

    @PostMapping("/loans/return")
    public ResponseEntity<Void> returnBook(@Valid @RequestBody ReturnRequest req) {
        libraryService.returnBook(req.bookId());
        return ResponseEntity.noContent().build();
    }
}






