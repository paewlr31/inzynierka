package com.example.library.services;

import com.example.library.models.BorrowedBook;
import com.example.library.repository.BorrowedRepository;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {

    private final BorrowedRepository borrowedBookRepository;

    public BorrowService(BorrowedRepository borrowedBookRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
    }

    public void borrowBook(BorrowedBook borrowedBook) {
        borrowedBookRepository.save(borrowedBook);
    }
}
