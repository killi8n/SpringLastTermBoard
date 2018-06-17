package com.killi8n.board.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.killi8n.board.domain.Account;
import com.killi8n.board.domain.Board;
import com.killi8n.board.domain.Good;
import com.killi8n.board.domain.Reply;
import com.killi8n.board.domain.Search;
import com.killi8n.board.domain.View;
import com.killi8n.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	
	private final String basePath = "/tmp/upload/";
	
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
		
		int displayingPageCount = 10;
		int displayingPerPage = 10;
		boolean lessThanDpp = false;
		boolean lastPageZone = false;
		boolean emptyList = false;
		boolean isLastPage = false;
		boolean isFirstPage = false;
		
		model.addAttribute("isSearched", false);
		
		try {
			count = boardService.GetCount();
			double LastPage = Math.ceil((double)count / 10);
			
			if(count == 0) {
				emptyList = true;
				model.addAttribute("emptyList", emptyList);
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
			
			
			
			System.out.println("LastPage: " + LastPage);

			model.addAttribute("emptyList", emptyList);
			model.addAttribute("boardList", boardList);
			model.addAttribute("LastPage", LastPage);
			model.addAttribute("Page", page);
			model.addAttribute("totalCount", count);
			int totalPage = (int) Math.ceil((double) count / displayingPerPage);
			model.addAttribute("totalPage", totalPage);
			
			if(page == LastPage) {
				isLastPage = true;
				model.addAttribute("isLastPage", isLastPage);
			} else {
				model.addAttribute("isLastPage", isLastPage);
			}
			
			if(page == 1) {
				isFirstPage = true;
				model.addAttribute("isFirstPage", isFirstPage);
			} else {
				model.addAttribute("isFirstPage", isFirstPage);
			}
			
			
			if(displayingPageCount >= totalPage) {
				lessThanDpp = true;
				model.addAttribute("lessThanDpp", lessThanDpp);
				return "board/index";
			}
			
			if((int) ((Math.floor((double) LastPage / 10) * 10) + 1) <= page) {
				lastPageZone = true;
				model.addAttribute("lastPageZone", true);
				model.addAttribute("lessThanDpp", false);
				int startPage = (int)Math.floor((double) LastPage / displayingPageCount) * 10 + 1;
				System.out.println("startPage: " + startPage);
				model.addAttribute("startPage", startPage);
				System.out.println("flagPage: " + (int) ((Math.floor((double) LastPage / 10) * 10) + 1));
				return "board/index";
			}
			
			if(page % displayingPageCount == 0) {
				int startPage = (int) Math.floor((double) page / displayingPageCount) * 10 - 9;
				model.addAttribute("lessThanDpp", false);
				model.addAttribute("lastPageZone", false);
				model.addAttribute("startPage", startPage);
				return "board/index";
			}
			
			
			int startPage = (int) Math.floor((double) page / displayingPageCount) * 10 + 1;
			model.addAttribute("lessThanDpp", false);
			model.addAttribute("lastPageZone", false);
			model.addAttribute("startPage", startPage);
			System.out.println("Here");
			
			
			
			
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
				board.setContent(board.getContent().replaceAll("<br/>", "\r\n"));
				model.addAttribute("board", board);
				model.addAttribute("isUpdate", true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("username", session.getAttribute("username"));
		
		return "board/editor";
	}
	
	@RequestMapping(value="/image", method=RequestMethod.GET)
	public void image(HttpServletRequest request, HttpServletResponse response) {
		String saveName = request.getQueryString();
		if (saveName == null) {
            return;
        }

		File f = new File(basePath + saveName);
		InputStream is = null;
        try {
            is = new FileInputStream(f);
            OutputStream oos = response.getOutputStream();

            byte[] buf = new byte[8192];
            int c = 0;
            while ((c = is.read(buf, 0, buf.length)) > 0) {
                oos.write(buf, 0, c);
                oos.flush();
            }
            oos.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@RequestMapping(value="/editor", method=RequestMethod.POST)
	public String editorAction(
			RedirectAttributes redirectAttributes, 
			@RequestParam(required=false, value="file") MultipartFile file,
			@RequestParam("username") String username,
			@RequestParam("title") String title,
			@RequestParam("content") String content)
	{
//		String title = board.getTitle();
//		String content = board.getContent();
		System.out.println("filename: " + file.getOriginalFilename());
		if(title == "" || title.trim().equals("")) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "제목을 입력해주세요.");
			return "redirect:/board/editor";
		}
		
		if(content == "" || content.trim().equals("")) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "내용을 입력해주세요.");
			return "redirect:/board/editor";
		}
		
		
		
		try {
			Board board = new Board();
			board.setTitle(title);
			board.setUsername(username);
			board.setContent(content);
//			System.out.println("username: " +username);
			board.setContent(board.getContent().replaceAll("\r\n", "<br/>"));
			
			if(!file.isEmpty()) {
				String saveName = String.valueOf(new Date().getTime());
				OutputStream fos = null;
				
				try {
					fos = new FileOutputStream(basePath + File.separator + saveName);
					
					fos.write(file.getBytes());
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					try {
		                if (fos != null) {
		                    fos.close();
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
				}
				board.setFilename(file.getOriginalFilename());
				board.setSavename(saveName);
				board.setFilesize(file.getSize());
				
				
				
			}
			
			boardService.WriteBoard(board);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/board/index";
//		return "board/editor";
	}
	
//	private void saveFile()
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String boardDetailPage(@PathVariable int id, Board board, HttpSession session, Model model) {
		
		if(session.getAttribute("logged") == null) {
			return "redirect:/board/login";
		}
		
		
		
		
		View view = new View();
		view.setBoardId(id);
		view.setUsername((String) session.getAttribute("username"));
		
		int checkViewed = boardService.CheckViewed(view);
		if(checkViewed == 0) {
			boardService.ViewThisItem(view);
			boardService.UpdateCount(id);
		} 
		
		Good good = new Good();
		good.setBoardId(id);
		good.setUsername((String) session.getAttribute("username"));
		
		int alreadyChecked = boardService.CheckGoodExist(good);
		if(alreadyChecked == 1) {
			model.addAttribute("alreadyChecked", true);
		} else {
			model.addAttribute("alreadyChecked", false);
		}
		
		
		int firstId = boardService.GetFirst();
		int lastId = boardService.GetLast();
		
		boolean isFirst = false;
		boolean isLast = false;
		
		Board nextBoardItem;
		Board prevBoardItem;
		
		String username = (String) session.getAttribute("username");
		model.addAttribute("username", username);
		
		List<Reply> replyList = boardService.GetReplyListByBoardId(id);
		if(replyList.size() > 0) {
			model.addAttribute("replyList", replyList);
		}
		
		if(id == firstId) {
			isFirst = true;
			nextBoardItem = boardService.GetNextBoardItem(id);
			if(boardService.GetCount() > 1) {
				model.addAttribute("nextBoardItem", nextBoardItem);
			}
			
			model.addAttribute("prevBoardItem", null);
			board = boardService.GetBoardDetail(id);
			model.addAttribute("board", board);
			model.addAttribute("isFirst", isFirst);
			model.addAttribute("isLast", isLast);
			if(session.getAttribute("username").equals(board.getUsername())) {
				model.addAttribute("owner", true);
			}
			
			return "board/detail";
		}
		
		if(id == lastId) {
			isLast = true;
			prevBoardItem = boardService.GetPrevBoardItem(id);
			model.addAttribute("nextBoardItem", null);
			model.addAttribute("prevBoardItem", prevBoardItem);
			board = boardService.GetBoardDetail(id);
			model.addAttribute("board", board);
			model.addAttribute("isFirst", isFirst);
			model.addAttribute("isLast", isLast);
			if(session.getAttribute("username").equals(board.getUsername())) {
				model.addAttribute("owner", true);
			}
			
			return "board/detail";
		}
		
		try {
			board = boardService.GetBoardDetail(id);
			nextBoardItem = boardService.GetNextBoardItem(id);
			prevBoardItem = boardService.GetPrevBoardItem(id);
			model.addAttribute("board", board);
			model.addAttribute("isFirst", isFirst);
			model.addAttribute("isLast", isLast);
			model.addAttribute("nextBoardItem", nextBoardItem);
			model.addAttribute("prevBoardItem", prevBoardItem);
			if(session.getAttribute("username").equals(board.getUsername())) {
				model.addAttribute("owner", true);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "board/detail";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateBoardAction(
			HttpSession session, 
			RedirectAttributes redirectAttributes,
			@RequestParam("username") String username,
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam("id") String id,
			@RequestParam(value="file", required=false) MultipartFile file) {
		
		System.out.println("username: " + username);
		System.out.println("title: " + title);
		System.out.println("content: " + content);
		System.out.println("id: " + id);
		
		if(!session.getAttribute("username").equals(username)) {
			return "redirect:/board/index";
		}
		
		if(title.equals("") || title.trim().length() == 0) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "제목을 입력해주세요.");
			return "redirect:/board/editor?id=" + id;
		}
		
		if(content.equals("") || content.trim().length() == 0) {
			redirectAttributes.addFlashAttribute("ErrorMessage", "내용을 입력해주세요.");
			return "redirect:/board/editor?id=" + id;
		}
		
		try {
			
			Board board = new Board();
			board.setTitle(title);
			board.setContent(content.replaceAll("\r\n", "<br/>"));
			board.setId(Integer.parseInt(id));
			board.setUsername(username);
			
			
			
			if(!file.isEmpty()) {
				
				String saveName = String.valueOf(new Date().getTime());

		        OutputStream fos = null;

		        try {
		            fos = new FileOutputStream(basePath + File.separator + saveName);

		            fos.write(file.getBytes());
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                if (fos != null) {
		                    fos.close();
		                }
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		        
		        board.setFilename(file.getOriginalFilename());
		        board.setSavename(saveName);
		        board.setFilesize(file.getSize());
		        
		        
		        
			}
			
			Board oldBoard = boardService.GetBoardDetail(Integer.parseInt(id));
	        if(oldBoard.getSavename() != null) {
	        	File f = new File(basePath + oldBoard.getSavename());
	        	if(f.exists()) {
	        		f.delete();
	        	}
	        }
			
//			board.setContent(board.getContent().replaceAll("\r\n", "<br/>"));
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
			if(board.getSavename() != null) {
				File f = new File(basePath + board.getSavename());
				if(f.exists()) {
					f.delete();
				}
			}
			if(!session.getAttribute("username").equals(board.getUsername())) {
				return "redirect:/board/index";
			}
			
			boardService.RemoveBoard(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/board/index";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String searchByWord(@RequestParam("searchWord") String searchWord, 
			@RequestParam("searchCategory") String searchCategory, Model model,
			@RequestParam("page") String pageParam) {
		
		Search search = new Search();
		search.setCategory(searchCategory);
		search.setWord(searchWord);
		
		model.addAttribute("isSearched", true);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("category", searchCategory);
		
//		int displayingPageCount = 10;
//		int displayingPerPage = 10;
//		boolean lessThanDpp = false;
//		boolean lastPageZone = false;
//		boolean emptyList = false;
//		boolean isLastPage = false;
//		boolean isFirstPage = false;
		
		if(searchCategory.equals("title")) {
			int totalCount;
			int totalPage;
			int page = Integer.parseInt(pageParam);
			System.out.println("searchedCurrentPage: " + page);
			int LastPage;
			model.addAttribute("Page", page);
			try {
				List<Board> boardList = boardService.GetBoardByTitle(search);
				totalCount = boardList.size();
				System.out.println("totalCount: " + totalCount);
				if(totalCount <= 10) {
					model.addAttribute("isFirstPage", true);
					model.addAttribute("LastPage", 1);
					model.addAttribute("SearchedPage", page);
					model.addAttribute("lessThanDpp", true);
					model.addAttribute("lastPageZone", true);
					model.addAttribute("startPage", 1);
					model.addAttribute("isLastPage", true);
					model.addAttribute("searchedList", boardList);
				} else {
					totalPage = (int) Math.ceil((double) totalCount / 10);
					if(page == 1) {
						model.addAttribute("isFirstPage", true);
					} else {
						model.addAttribute("isFirstPage", false);
					}
					model.addAttribute("LastPage", totalPage);
					model.addAttribute("SearchedPage", page);
					
					if(totalCount > 90) {
						model.addAttribute("lessThanDpp", false);
					} else {
						model.addAttribute("lessThanDpp", true);
					}
					
					if((int) ((Math.floor((double) totalPage / 10) * 10) + 1) <= page) {
						int startPage = (int)Math.floor((double) totalPage / 10) * 10 + 1;
						model.addAttribute("lastPageZone", true);
						model.addAttribute("startPage", startPage);
						model.addAttribute("lessThanDpp", false);
					} else {
						int startPage = (int)Math.floor((double) page / 10) * 10 + 1;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						System.out.println("totalPage: " + totalPage);
					}
					
					
					if(page % 10 == 0) {
						int startPage = (int) Math.floor((double) page / 10) * 10 - 9;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						
					}
					
					ArrayList<Board> searchedBoardList = new ArrayList<Board>();
					int startIndex = (page - 1) * 10;
					int index = 0;
					if(page == totalPage) {
						for(int i = startIndex;i < totalCount;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", true);
					} else {
						for(int i = startIndex;i < startIndex + 10;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", false);
						
					}
					
					
					
					System.out.println("searchedBoardListCount: " + searchedBoardList.size());
					model.addAttribute("searchedList", searchedBoardList);
				}
				
				if(boardList.size() == 0) {
					model.addAttribute("emptyList", true);
				} else {
					model.addAttribute("emptyList", false);
				}
				
				
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(searchCategory.equals("username")) {
			int totalCount;
			int totalPage;
			int page = Integer.parseInt(pageParam);
			System.out.println("searchedCurrentPage: " + page);
			int LastPage;
			model.addAttribute("Page", page);
			try {
				List<Board> boardList = boardService.GetBoardByUsername(search);
				totalCount = boardList.size();
				System.out.println("totalCount: " + totalCount);
				if(totalCount <= 10) {
					model.addAttribute("isFirstPage", true);
					model.addAttribute("LastPage", 1);
					model.addAttribute("SearchedPage", page);
					model.addAttribute("lessThanDpp", true);
					model.addAttribute("lastPageZone", true);
					model.addAttribute("startPage", 1);
					model.addAttribute("isLastPage", true);
					model.addAttribute("searchedList", boardList);
				} else {
					totalPage = (int) Math.ceil((double) totalCount / 10);
					if(page == 1) {
						model.addAttribute("isFirstPage", true);
					} else {
						model.addAttribute("isFirstPage", false);
					}
					model.addAttribute("LastPage", totalPage);
					model.addAttribute("SearchedPage", page);
					
					if(totalCount > 90) {
						model.addAttribute("lessThanDpp", false);
					} else {
						model.addAttribute("lessThanDpp", true);
					}
					
					if((int) ((Math.floor((double) totalPage / 10) * 10) + 1) <= page) {
						int startPage = (int)Math.floor((double) totalPage / 10) * 10 + 1;
						model.addAttribute("lastPageZone", true);
						model.addAttribute("startPage", startPage);
						model.addAttribute("lessThanDpp", false);
					} else {
						int startPage = (int)Math.floor((double) page / 10) * 10 + 1;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						System.out.println("totalPage: " + totalPage);
					}
					
					
					if(page % 10 == 0) {
						int startPage = (int) Math.floor((double) page / 10) * 10 - 9;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						
					}
					
					ArrayList<Board> searchedBoardList = new ArrayList<Board>();
					int startIndex = (page - 1) * 10;
					int index = 0;
					if(page == totalPage) {
						for(int i = startIndex;i < totalCount;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", true);
					} else {
						for(int i = startIndex;i < startIndex + 10;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", false);
						
					}
					
					
					
					System.out.println("searchedBoardListCount: " + searchedBoardList.size());
					model.addAttribute("searchedList", searchedBoardList);
				}
				
				if(boardList.size() == 0) {
					model.addAttribute("emptyList", true);
				} else {
					model.addAttribute("emptyList", false);
				}
				
				
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(searchCategory.equals("content")) {
			int totalCount;
			int totalPage;
			int page = Integer.parseInt(pageParam);
			System.out.println("searchedCurrentPage: " + page);
			int LastPage;
			model.addAttribute("Page", page);
			try {
				List<Board> boardList = boardService.GetBoardByContent(search);
				totalCount = boardList.size();
				System.out.println("totalCount: " + totalCount);
				if(totalCount <= 10) {
					model.addAttribute("isFirstPage", true);
					model.addAttribute("LastPage", 1);
					model.addAttribute("SearchedPage", page);
					model.addAttribute("lessThanDpp", true);
					model.addAttribute("lastPageZone", true);
					model.addAttribute("startPage", 1);
					model.addAttribute("isLastPage", true);
					model.addAttribute("searchedList", boardList);
				} else {
					totalPage = (int) Math.ceil((double) totalCount / 10);
					if(page == 1) {
						model.addAttribute("isFirstPage", true);
					} else {
						model.addAttribute("isFirstPage", false);
					}
					model.addAttribute("LastPage", totalPage);
					model.addAttribute("SearchedPage", page);
					
					if(totalCount > 90) {
						model.addAttribute("lessThanDpp", false);
					} else {
						model.addAttribute("lessThanDpp", true);
					}
					
					if((int) ((Math.floor((double) totalPage / 10) * 10) + 1) <= page) {
						int startPage = (int)Math.floor((double) totalPage / 10) * 10 + 1;
						model.addAttribute("lastPageZone", true);
						model.addAttribute("startPage", startPage);
						model.addAttribute("lessThanDpp", false);
					} else {
						int startPage = (int)Math.floor((double) page / 10) * 10 + 1;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						System.out.println("totalPage: " + totalPage);
					}
					
					
					if(page % 10 == 0) {
						int startPage = (int) Math.floor((double) page / 10) * 10 - 9;
						model.addAttribute("lessThanDpp", false);
						model.addAttribute("lastPageZone", false);
						model.addAttribute("startPage", startPage);
						
					}
					
					ArrayList<Board> searchedBoardList = new ArrayList<Board>();
					int startIndex = (page - 1) * 10;
					int index = 0;
					if(page == totalPage) {
						for(int i = startIndex;i < totalCount;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", true);
					} else {
						for(int i = startIndex;i < startIndex + 10;i++) {
							searchedBoardList.add(index, boardList.get(i));
							index++;
						}
						model.addAttribute("isLastPage", false);
						
					}
					
					
					
					System.out.println("searchedBoardListCount: " + searchedBoardList.size());
					model.addAttribute("searchedList", searchedBoardList);
				}
				
				if(boardList.size() == 0) {
					model.addAttribute("emptyList", true);
				} else {
					model.addAttribute("emptyList", false);
				}
				
				
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		return "/board/index";
	}
	
	@RequestMapping(value="/good", method=RequestMethod.POST)
	public String goodAction(@RequestParam("boardId") int boardId, HttpSession session, Model model) {
		
		boolean alreadyChecked = false;
		
		try {
			Good good = new Good();
			good.setBoardId(boardId);
			good.setUsername((String) session.getAttribute("username"));
			int checked = boardService.CheckGoodExist(good);
			System.out.println("checked: " + checked);
			if(checked == 1) {
				alreadyChecked = true;
				boardService.DeleteGood(good);
				boardService.MinusGood(good);
//				model.addAttribute("alreadyChecked", alreadyChecked);
				
			} else {
				boardService.CheckGood(good);
				boardService.PlusGood(good);
				
//				model.addAttribute("alreadyChecked", alreadyChecked);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/board/" + boardId;
	}
}
