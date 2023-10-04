package com.example.todospringapp.controller;

import com.example.todospringapp.dto.ResponseDto;
import com.example.todospringapp.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todospringapp.service.MailService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("mail/send")
    public String mailConfirm(@RequestParam String email) throws Exception {
        String code = mailService.sendSimpleMessage(email);
        log.info("인증코드 : " + code);
        return code;
    }

    @GetMapping("mail/check")
    public ResponseEntity<?> compareCode(@RequestParam String code) throws ChangeSetPersister.NotFoundException {
        try{
            mailService.verifyCode(code);

            return ResponseEntity.ok().body("인증 성공");
        } catch (ChangeSetPersister.NotFoundException e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();

            return ResponseEntity.badRequest().body("코드 인증 에러");
        } catch (RuntimeException e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();

            return ResponseEntity.badRequest().body("코드 인증 에러");
        }

    }
}
