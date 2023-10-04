package com.example.todospringapp.repository;

import com.example.todospringapp.model.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    @Query("select t from TodoEntity t where t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);

    List<TodoEntity> findAllByDone(boolean done);

    Page<TodoEntity> findAllByUserIdOrderByDoneAscIdDesc(String userId, Pageable pageable);
}
