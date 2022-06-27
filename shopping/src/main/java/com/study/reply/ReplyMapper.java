package com.study.reply;

import java.util.List;
import java.util.Map;

public interface ReplyMapper {

  List<ReplyDTO> list(Map map);

  int create(ReplyDTO replyDTO);

  ReplyDTO read(int rnum);

  int update(ReplyDTO replyDTO);

  int delete(int rnum);

  int total(int contentsno);
  
  ReplyDTO detail(int rnum);

}
