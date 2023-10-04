package com.example.todospringapp.controller;

import com.example.todospringapp.dto.ResponseDto;
import com.example.todospringapp.dto.TodoDto;
import com.example.todospringapp.model.TodoEntity;
import com.example.todospringapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    final private TodoService todoService;


    /** @AuthenticationPrincipal이란?
     * 세션 정보 UserDetails에 접근할 수 있는 어노테이션
     *
     * */
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
            @RequestBody TodoDto dto){
        try {
            /** POST localhost:8080/todo
             * {
             *     "title":"My first todo",
             *     "done":false
             * }
             * */
            log.info("Log:createTodo entrance");

//            dto를 이용해 테이블에 저장하기 위한 entity를 생성한다.
            TodoEntity entity = TodoDto.toEntity(dto);
            log.info("Log:dto => entity ok!");

//            entity userId를 임시로 지정한다.
            entity.setId(null);
            entity.setUserId(userId);

//            service.create를 통해 repository에 entity를 저장한다.
//            이때 넘어오는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
            Optional<TodoEntity> entities = todoService.create(entity);
            log.info("Log:service.create ok!");

//            entities를 dtos로 스트림 변환한다.
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            log.info("Log:entities => dtos ok!");

//            Response Dto를 생성한다.
            /**
             * {
             *     "error": null,
             *     "data": [
             *         {
             *             "id": "402865c887460a880187460b34240000",
             *             "title": "My first todo",
             *             "done": false
             *         }
             *     ]
             * }
             * */
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            log.info("Log:responsedto ok!");

//            HTTP Status 200 상태로 response를 전송한다.
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body("this is error");
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId,
                                              Pageable pageable) {
        String temporaryUserId = userId;
        Page<TodoEntity> entities = todoService.getTodoPage(temporaryUserId, pageable);
        List<TodoDto> dtos = entities.getContent().stream().map(TodoDto::new).collect(Collectors.toList());

        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
        response.setError(String.valueOf(entities.getTotalElements()));

//        HTTP Status 200 상태로 response를 전송한다.
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal String userId,
                                    @RequestBody TodoDto dto) {
        try {
//        dto를 이용해 테이블에 저장하기 위한 entity를 생성한다.
            TodoEntity entity = TodoDto.toEntity(dto);

//        entity userId를 임시로 지정한다.
            entity.setUserId(userId);

//        service.create를 통해 repository에 entity를 저장한다.
//        이때 넘어오는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
            Optional<TodoEntity> entities = todoService.update(entity);

//        entities를 dtos로 스트림 변환한다.
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

//        Response Dto를 생성한다.
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().data(dtos).build();

//        HTTP Status 200 상태로 response를 전송한다.
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().error(error).build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PutMapping
    public ResponseEntity<?>updateTodo(@AuthenticationPrincipal String userId,
                                       @RequestBody TodoDto dto) {
        try{
            //        dto를 이용해 테이블에 저장하기 위한 entity를 생성한다.
            TodoEntity entity = TodoDto.toEntity(dto);

            //        entity userId를 임시로 지정한다.
            entity.setUserId(userId);

            //        service.create를 통해 repository에 entity를 저장한다.
//        이때 넘어오는 값이 없을 수도 있으므로 List가 아닌 Optional로 한다.
            Optional<TodoEntity> entities = todoService.updateTodo(entity);

            //        entities를 dtos로 스트림 변환한다.
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

            //        Response Dto를 생성한다.
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().data(dtos).build();
//        HTTP Status 200 상태로 response를 전송한다.
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().error(error).build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal String userId,
                                    @RequestBody(required = false) TodoDto dto,
                                    @RequestParam(value = "done", defaultValue = "false") String done){
        try{
            List<TodoEntity> entities;
            if (done.equals("ok") && dto==null){
                entities = todoService.deleteTodoIsDone();
            } else{
                TodoEntity entity = TodoDto.toEntity(dto);

                // entity userId를 임시로 지정한다.
                entity.setUserId(userId);

                entities = todoService.delete(entity);
            }


            // entities를 dtos로 스트림 변환한다.
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

//            ResponseDto를 생성한다.
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> responseDto = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
