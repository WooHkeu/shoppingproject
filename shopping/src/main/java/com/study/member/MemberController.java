package com.study.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.payload.MemberPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.study.utility.Utility;

@Controller
public class MemberController {
  @Autowired
  @Qualifier("com.study.member.MemberServiceImpl")
  private MemberService service;
  
  
  @GetMapping("/")
  public String home() {
    return "/home";
  }
  @GetMapping("/member/read")
  public String read(String id, Model model) {
    MemberDTO dto = service.read(id);
   
    model.addAttribute("dto",dto);
     
    
     return "/member/read";

  }
  @GetMapping("/member/mypage")
  public String mypage(HttpSession session, Model model) {
     String id = (String)session.getAttribute("id");
   
    if(id==null) {
         return "redirect:/member/login/";
    }else {
    
         MemberDTO dto = service.mypage(id);
        
         model.addAttribute("dto", dto);
        
     return "/member/mypage";
    }
  }

  @PostMapping("/update/fname")
  public String updateFname(MemberPayload payload, HttpServletResponse response,
                                  HttpServletRequest request, Model model) {
    if (!payload.getPasswd().equals(payload.getNewPasswd())) {
      model.addAttribute("msg", "새 패스워드와 새 패스워드 확인이 다릅니다. 다시 입력해 주세요.");
      return "/member/errorMsg";
    }

    if (service.updatePasswordByPayload(payload) > 0) {
      return "redirect:/member/login";
    } else {
      model.addAttribute("msg", "아이디 또는 이름을 잘못 입력하셨습니다. 일치하는 계정이 없습니다.");
      return "/member/errorMsg";
    }
  }


  @RequestMapping("/admin/member/list")
  public String list(HttpServletRequest request) {
    // 검색관련------------------------
    String col = Utility.checkNull(request.getParameter("col"));
    String word = Utility.checkNull(request.getParameter("word"));

    if (col.equals("total")) {
      word = "";
    }

    // 페이지관련-----------------------
    int nowPage = 1;// 현재 보고있는 페이지
    if (request.getParameter("nowPage") != null) {
      nowPage = Integer.parseInt(request.getParameter("nowPage"));
    }
    int recordPerPage = 3;// 한페이지당 보여줄 레코드갯수

    // DB에서 가져올 순번(oracle)-----------------
    // int sno = ((nowPage - 1) * recordPerPage) + 1;
    // int eno = nowPage * recordPerPage;
    // DB에서 가져올 순번(mysql)-----------------
    int sno = (nowPage - 1) * recordPerPage;
    int eno = recordPerPage;

    Map map = new HashMap();
    map.put("col", col);
    map.put("word", word);
    map.put("sno", sno);
    map.put("eno", eno);

    int total = service.total(map);

    List<MemberDTO> list = service.list(map);

    String paging = Utility.paging(total, nowPage, recordPerPage, col, word);

    // request에 Model사용 결과 담는다
    request.setAttribute("list", list);
    request.setAttribute("nowPage", nowPage);
    request.setAttribute("col", col);
    request.setAttribute("word", word);
    request.setAttribute("paging", paging);

    return "/member/list";

  }

  @PostMapping("/member/update")
  public String update(MemberDTO dto, Model model) {
    int cnt = service.update(dto);

    if (cnt == 1) {
      model.addAttribute("id", dto.getId());
      return "redirect:/";
    } else {
      return "error";
    }
  }

  @GetMapping("/member/update")
  public String update(String id, HttpSession session, Model model) {

    if (id == null) {
      id = (String) session.getAttribute("id");
    }

    MemberDTO dto = service.read(id);

    model.addAttribute("dto", dto);

    return "/member/update";
  }

  @GetMapping("/member/agree")
  public String agree() {

    return "/member/agree";
  }

  @PostMapping("/member/createForm")
  public String create() {
    return "/member/create";
  }

  @GetMapping(value = "/member/emailcheck", produces = "application/json;charset=utf-8")
  @ResponseBody
  public Map<String, String> emailcheck(String email) {
    int cnt = service.duplicatedEmail(email);

    Map<String, String> map = new HashMap<String, String>();
    if (cnt > 0) {
      map.put("str", email + "는 중복되어서 사용할 수 없습니다.");
    } else {
      map.put("str", email + "는 중복아님, 사용가능합니다.");
    }
    return map;
  }

  @GetMapping(value = "/member/idcheck", produces = "application/json;charset=utf-8")
  @ResponseBody
  public Map<String, String> idcheck(String id) {
    int cnt = service.duplicatedId(id);

    Map<String, String> map = new HashMap<String, String>();
    if (cnt > 0) {
      map.put("str", id + "는 중복되어서 사용할 수 없습니다.");
    } else {
      map.put("str", id + "는 중복아님, 사용가능합니다.");
    }
    return map;
  }

  @PostMapping("/member/create")
  public String create(MemberDTO dto, HttpServletRequest request) throws IOException {
    String upDir = UploadMem.getUploadDir();
    String fname = Utility.saveFileSpring(dto.getFnameMF(), upDir);
    int size = (int) dto.getFnameMF().getSize();
    if (size > 0) {
      dto.setFname(fname);
    } else {
      dto.setFname("member.jpg");
    }

    if (service.create(dto) > 0) {
      return "redirect:/";
    } else {
      return "error";
    }
  }

  @GetMapping("/member/logout")
  public String logout(HttpSession session) {
    // session.removeAttribute("id");
    // session.removeAttribute("grade");
    // session.removeAttribute("mname");
    session.invalidate();

    return "redirect:/";
  }

  @PostMapping("/member/login")
  public String login(@RequestParam Map<String, String> map, HttpSession session, HttpServletResponse response,
      HttpServletRequest request, Model model) {
    int cnt = service.loginCheck(map);

    if (cnt > 0) {// 회원이다.
      Map gmap = service.getGrade(map.get("id"));
      session.setAttribute("id", map.get("id"));
      session.setAttribute("grade", gmap.get("grade"));
      session.setAttribute("mname", gmap.get("mname"));

      // Cookie 저장,id저장 여부 및 id
      Cookie cookie = null;
      String c_id = request.getParameter("c_id");
      if (c_id != null) {
        cookie = new Cookie("c_id", c_id); // c_id=> Y
        cookie.setMaxAge(60 * 60 * 24 * 365);// 1년
        response.addCookie(cookie);// 요청지(client:브라우저 컴) 쿠키 저장

        cookie = new Cookie("c_id_val", map.get("id"));
        cookie.setMaxAge(60 * 60 * 24 * 365);// 1년
        response.addCookie(cookie);// 요청지(client:브라우저 컴) 쿠키 저장
      } else {
        cookie = new Cookie("c_id", ""); // 쿠키 삭제
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("c_id_val", "");// 쿠키 삭제
        cookie.setMaxAge(0);
        response.addCookie(cookie);

      }

    } // ifcnt>0 end

    if (cnt > 0) {

      if (map.get("rurl") != null && !map.get("rurl").equals("")) {
        model.addAttribute("contentsno", map.get("contentsno"));
        model.addAttribute("nPage", map.get("nPage"));
        model.addAttribute("nowPage", map.get("nowPage"));
        model.addAttribute("col", map.get("col"));
        model.addAttribute("word", map.get("word"));

        return "redirect:" + map.get("rurl");
      } else {

        return "redirect:/";
      }

    } else {
      model.addAttribute("msg", "아이디 또는 비밀번호를 잘못 입력 했거나 <br>회원이 아닙니다. 회원가입 하세요");
      return "/member/errorMsg";
    }
  }

  @GetMapping("/member/login")
  public String login(HttpServletRequest request) {
    /*----쿠키설정 내용시작----------------------------*/
    String c_id = ""; // ID 저장 여부를 저장하는 변수, Y
    String c_id_val = ""; // ID 값

    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    if (cookies != null) {
      for (int i = 0; i < cookies.length; i++) {
        cookie = cookies[i];

        if (cookie.getName().equals("c_id")) {
          c_id = cookie.getValue(); // Y
        } else if (cookie.getName().equals("c_id_val")) {
          c_id_val = cookie.getValue(); // user1...
        }
      }
    }
    /*----쿠키설정 내용 끝----------------------------*/

    request.setAttribute("c_id", c_id);
    request.setAttribute("c_id_val", c_id_val);

    return "/member/login";
  }

  @PostMapping("/member/updateFile")
  public String updateFile(MultipartFile fnameMF, String oldfile, HttpSession session, HttpServletRequest request, Model model)
      throws IOException {
    String basePath = UploadMem.getUploadDir();

    if (fnameMF == null || fnameMF.getOriginalFilename().equals("")) {
      model.addAttribute("msg", "사진을 선택해 주세요.");
      return "/member/errorMsg";
    } else {
      fnameMF.getName();
    }

    if (oldfile != null && !oldfile.equals("member.jpg")) { // 원본파일 삭제
      Utility.deleteFile(basePath, oldfile);
    }

    // storage에 변경 파일 저장
    Map map = new HashMap();
    map.put("id", session.getAttribute("id"));
    map.put("fname", Utility.saveFileSpring(fnameMF, basePath));

    // 디비에 파일명 변경
    int cnt = service.updateFile(map);

    if (cnt == 1) {
      return "redirect:mypage"; // 나중에 인덱스나 홈으로 바꿔도 된다.
    } else {
      return "/error";
    }
  }

  @GetMapping("/member/updateFile")
  public String updateFileForm() {

    return "/member/updateFile";
  }

  @GetMapping("/member/find/id")
  public String findId(HttpServletRequest request) {
    return "/member/find/id";
  }

  @PostMapping("/member/find/id")
  public String findId(MemberDTO memberDTO, HttpServletResponse response,
                       HttpServletRequest request, Model model) {
    MemberDTO dto = service.findIdByMemberDTO(memberDTO);
    if (dto != null) {
      return "redirect:id/complete/" + dto.getId();
    } else {
      model.addAttribute("msg", "이름 또는 이메일을 잘못 입력하셨습니다. 일치하는 아이디가 없습니다.");
      return "/member/errorMsg";
    }
  }

  @GetMapping("/member/find/id/complete/{id}")
  public String findIdComplete(@PathVariable String id, HttpServletRequest request, ModelMap modelMap) {
    modelMap.addAttribute("id", id);
    return "/member/find/id/complete";
  }

  @GetMapping("/member/find/password")
  public String findPassword(HttpServletRequest request) {
    return "/member/find/password";
  }

  @PostMapping("/member/find/password")
  public String findPassword(MemberDTO memberDTO, HttpServletResponse response,
                       HttpServletRequest request, Model model) {
    MemberDTO dto = service.findPasswordByMemberDTO(memberDTO);
    if (dto != null) {
      return "redirect:password/complete/" + dto.getId();
    } else {
      model.addAttribute("msg", "아이디 또는 이름을 잘못 입력하셨습니다. 일치하는 패스워드가 없습니다.");
      return "/member/errorMsg";
    }
  }

  @GetMapping("/member/find/password/complete/{id}")
  public String findPasswordComplete(@PathVariable String id, HttpServletRequest request, ModelMap modelMap) {
    modelMap.addAttribute("id", id);
    return "/member/find/password/complete";
  }

  @PostMapping("/member/new/password")
  public String updateNewPassword(MemberPayload payload, HttpServletResponse response,
                                  HttpServletRequest request, Model model) {
    if (!payload.getPasswd().equals(payload.getNewPasswd())) {
      model.addAttribute("msg", "새 패스워드와 새 패스워드 확인이 다릅니다. 다시 입력해 주세요.");
      return "/member/errorMsg";
    }

    if (service.updatePasswordByPayload(payload) > 0) {
      return "redirect:/member/login";
    } else {
      model.addAttribute("msg", "아이디 또는 이름을 잘못 입력하셨습니다. 일치하는 계정이 없습니다.");
      return "/member/errorMsg";
    }
  }


}
