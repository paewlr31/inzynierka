package com.example.library.repository;

import com.example.library.models.BorrowedBook;
import com.example.library.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowedRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByUser(User user);
}


