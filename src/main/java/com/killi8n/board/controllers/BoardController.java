package com.killi8n.board.controllers;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.killi8n.board.domain.Account;
import com.killi8n.board.domain.Board;
import com.killi8n.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(HttpSession session, 
			Model model, 
			Integer count,
			@RequestParam(value="page", defaultValue="1", required=false) int page) {
		if(session.getAttribute("logged") != null) {
			if((Boolean)session.getAttribute("logged") == true) {
				model.addAttribute("logged", session.getAttribute("logged"));
				model.addAttribute("username", session.getAttribute("username"));
			}
		}
		
		
		try {
			count = boardService.GetCount();
			double LastPage = Math.ceil((double)count / 10);
			
			if(count == 0) {
				return "board/index";
			}
			
			System.out.println("LastPage: " + Math.ceil(count / 10));
			System.out.println("Count: " + count);
			
			if(page > LastPage) {
				return "redirect:/board/index?page=" + (int) LastPage;
			}
			
			if(page < 1) {
				return "redirect:/board/index?page=1";
			}
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("pagingValue", (page - 1) * 10 + "");
	
			
			List<Board> boardList = boardService.GetAllBoards(map);
			model.addAttribute("boardList", boardList);
			model.addAttribute("LastPage", LastPage);
			model.addAttribute("Page", page);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		return "board/index";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginPage() {
		return "board/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginAction(Account account, 
			RedirectAttributes redirectAttributes, 
			Integer existing,
			HttpSession session) {
		
		String username = account.getUsername();
		String password = account.getPassword();
		
		if(username.trim().length() == 0 || username == null || username == "") {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("ErrorMessage", "부적합한 아이디 입니다.");
			return "redirect:/board/login";
		}
		
		if(password.trim().length() == 0 || password == null || password == "") {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("ErrorMessage", "부적합한 패스워드 입니다.");
			return "redirect:/board/login";
		}
		
		try {
			existing = boardService.CheckExisting(username);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(existing == 0) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("ErrorMessage", "존재하지 않는 아이디입니다.");
			return "redirect:/board/login";
		}
		
		
		
		try {
			Account userDetail = boardService.GetUserDetail(username);
			
			if(!userDetail.getPassword().equals(password)) {
				redirectAttributes.addFlashAttribute("username", username);
				redirectAttributes.addFlashAttribute("password", password);
				redirectAttributes.addFlashAttribute("ErrorMessage", "패스워드가 일치하지 않습니다.");
				return "redirect:/board/login";
			}
			
			session.setAttribute("username", userDetail.getUsername());
			session.setAttribute("logged", true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	
		return "redirect:/board/index";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerPage() {
		System.out.println("Register Page");
		return "board/register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerAction(Account account, RedirectAttributes redirectAttributes, Integer existing) {
//		
//		System.out.println("account: " + account);
//		System.out.println("username: " + account.getUsername());
//		System.out.println("password: " + account.getPassword());
//		System.out.println("email: " + account.getEmail());
//		
		String username = account.getUsername();
		String password = account.getPassword();
		String passwordCheck = account.getPasswordCheck();
		String email = account.getEmail();
		
		if(username.equals("") || username == null || username.trim().length() == 0) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("passwordCheck", passwordCheck);
			redirectAttributes.addFlashAttribute("email", email);
			redirectAttributes.addFlashAttribute("ErrorMessage", "부적절한 아이디입니다.");
			return "redirect:/board/register";
		}
		
		if(!password.equals(passwordCheck)) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("passwordCheck", passwordCheck);
			redirectAttributes.addFlashAttribute("email", email);
			redirectAttributes.addFlashAttribute("ErrorMessage", "두 비밀번호가 일치하지 않습니다.");
			return "redirect:/board/register";
		}
		
		if(password.length() < 6) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("passwordCheck", passwordCheck);
			redirectAttributes.addFlashAttribute("email", email);
			redirectAttributes.addFlashAttribute("ErrorMessage", "패스워드는 6자 이상으로 해주세요.");
			return "redirect:/board/register";
		}
		
		if(email.equals("") || email.trim().length() == 0 || email == null) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("passwordCheck", passwordCheck);
			redirectAttributes.addFlashAttribute("email", email);
			redirectAttributes.addFlashAttribute("ErrorMessage", "부적절한 이메일입니다.");
			return "redirect:/board/register";
		}
		
		try {
			existing = boardService.CheckExisting(username);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(existing != 0) {
			redirectAttributes.addFlashAttribute("username", username);
			redirectAttributes.addFlashAttribute("password", password);
			redirectAttributes.addFlashAttribute("passwordCheck", passwordCheck);
			redirectAttributes.addFlashAttribute("email", email);
			redirectAttributes.addFlashAttribute("ErrorMessage", "이미 존재하는 아이디 입니다.");
			return "redirect:/board/register";
		}
		
		
		try {
			 boardService.Register(account);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/board/login";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	public String logoutAction(HttpSession session) {
		session.removeAttribute("logged");
		session.removeAttribute("username");
		
		
		return "redirect:/board/index";
	}
	
	@RequestMapping(value="/editor", method=RequestMethod.GET)
	public String editorPage(HttpSession session, Model model, @RequestParam(value="id", defaultValue="0", required=false) int id, Board board) {
		
		if(session.getAttribute("logged") == null) {
			return "redirect:/board/login";
		}
		
		try {
			if(id != 0) {
				board = boardService.GetBoardDetail(id);
				if(!session.getAttribute("username").equals(board.getUsername())) {
					return "redirect:/board/index";
				}
				model.addAttribute("board", board);
				model.addAttribute("isUpdate", true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("username", session.getAttribute("username"));
		
		return "board/editor";
	}
	
	@RequestMapping(value="/editor", method=RequestMethod.POST)
	public String editorAction(Board board, RedirectAttributes redirectAttributes)
	{
		String title = board.getTitle();
		String content = board.getContent();
		if(title == "" || title.trim().equals("")) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "제목을 입력해주세요.");
			return "redirect:/board/editor";
		}
		
		if(content == "" || content.trim().equals("")) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "내용을 입력해주세요.");
			return "redirect:/board/editor";
		}
		
		try {
			boardService.WriteBoard(board);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/board/index";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String boardDetailPage(@PathVariable int id, Board board, HttpSession session, Model model) {
		
		if(session.getAttribute("logged") == null) {
			return "redirect:/board/login";
		}
		
		try {
			board = boardService.GetBoardDetail(id);
			model.addAttribute("board", board);
			if(session.getAttribute("username").equals(board.getUsername())) {
				model.addAttribute("owner", true);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "board/detail";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateBoardAction(HttpSession session, Board board, RedirectAttributes redirectAttributes) {
		
		if(!session.getAttribute("username").equals(board.getUsername())) {
			return "redirect:/board/index";
		}
		
		if(board.getTitle().equals("") || board.getTitle().trim().length() == 0) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "제목을 입력해주세요.");
			return "redirect:/board/editor?id=" + board.getId();
		}
		
		if(board.getContent().equals("") || board.getContent().trim().length() == 0) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "내용을 입력해주세요.");
			return "redirect:/board/editor?id=" + board.getId();
		}
		
		try {
			boardService.UpdateBoard(board);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/board/index";
	}
	
	@RequestMapping(value="/remove/{id}", method=RequestMethod.POST)
	public String removeAction(HttpSession session, @PathVariable int id, Board board) {
		
		
		
		try {
			board = boardService.GetBoardDetail(id);
			if(!session.getAttribute("username").equals(board.getUsername())) {
				return "redirect:/board/index";
			}
			
			boardService.RemoveBoard(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/board/index";
	}
}
