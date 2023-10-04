package com.example.todospringapp.service;

import com.example.todospringapp.model.TodoEntity;
import com.example.todospringapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {
    final private TodoRepository todoRepository;

    public Optional<TodoEntity> create(final TodoEntity entity) {
//        Validation
        validate(entity);
        todoRepository.save(entity);

        return todoRepository.findById(entity.getId());
    }

    public Page<TodoEntity> getTodoPage(final String userId, Pageable pageable) {
        return todoRepository.findAllByUserIdOrderByDoneAscIdDesc(userId, pageable);
    }

    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    public Optional<TodoEntity> update(final TodoEntity todoEntity) {
//        Validations
        validate(todoEntity);
        if (todoRepository.existsById(todoEntity.getId())){
            todoRepository.save(todoEntity);
        } else{
            throw new RuntimeException("Unknown id");
        }

        return todoRepository.findById(todoEntity.getId());
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity todoEntity){
//        Validation
        validate(todoEntity);

//        테이블에서 id에 해당하는 데이터셋을 가져온다.
        final Optional<TodoEntity> original = todoRepository.findById(todoEntity.getId());

//        original에 담겨진 내용을 todo에 할당하고 title, done 값을 변경한다.
        original.ifPresent(todo->{
            todo.setTitle(todoEntity.getTitle());
            todo.setDone(todoEntity.isDone());
            todoRepository.save(todo);
        });
        /**
         * 위의 람다식과 동일한 표현
         * if(original.isPresent){
         *             finl TodoEntity todo = original.get();
         *             todo.setTitle(todoEntity.getTitle());
         *             todo.setDone(todoEntity.isDone());
         *             todoRepository.save(todo);
         *         }
         * */
        return todoRepository.findById(todoEntity.getId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        List<TodoEntity> list = new ArrayList<>();
        if(todoRepository.existsById(entity.getId())) {
            todoRepository.deleteById(entity.getId());
            list.add(entity);
        }
        else throw new RuntimeException("id does not exist");

        return list;
    }

    public List<TodoEntity> deleteTodoIsDone(){
        List<TodoEntity> list = new ArrayList<>();
        list = todoRepository.findAllByDone(true);
        if (!list.isEmpty())
            list.stream().forEach(todoEntity -> {todoRepository.delete(todoEntity);});

        return list;
    }

    public void validate(TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if(entity.getUserId()==null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
