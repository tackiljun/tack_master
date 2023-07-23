package org.zerock.bj2.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.zerock.bj2.dto.PageRequestDTO;
import org.zerock.bj2.dto.PageResponseDTO;
import org.zerock.bj2.dto.ReplyDTO;
import org.zerock.bj2.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies/")
public class ReplyController {

  // 예외처리.
  public static class DataNotFoundException extends RuntimeException {
    
    public DataNotFoundException(String msg) {
      super(msg);
    }
  }

  // 서비스에 의존성주입.
  private final ReplyService service;

  // List.
  @GetMapping(value = "{tno}/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResponseDTO<ReplyDTO> getReplyList(
    @PathVariable("tno") Long tno, PageRequestDTO pageRequestDTO) {
    
    log.info("TNO: " + tno);

    // List<ReplyDTO> result = IntStream.rangeClosed(1, 10).mapToObj(i -> {

    //   return ReplyDTO.builder()
    //   .rno((long)i)
    //   .tno(tno)
    //   .reply("댓글....." + i)
    //   .replyer("user00")
    //   .replyDate(LocalDate.now())
    //   .build();
    // }).collect(Collectors.toList());

    return service.getList(tno, pageRequestDTO);
  }

  @GetMapping(value = "{rno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ReplyDTO getOne(@PathVariable("rno") Long rno) {

    if(service.getOne(rno) == null) {
      throw new DataNotFoundException("에러입니다!!!!!");
    }
       

    return service.getOne(rno);
  }


  @PostMapping("{tno}/new")
  public Map<String, Long> register(@PathVariable("tno") Long tno, 
  @Valid @RequestBody ReplyDTO replyDTO, 
  BindingResult bindingResult) throws Exception {
    
    replyDTO.setTno(tno);

    if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
      }

    Long rno = service.register(replyDTO);

    log.info(replyDTO);

    return Map.of("result", rno);
  }



  
}