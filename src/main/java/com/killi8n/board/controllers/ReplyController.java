package com.killi8n.board.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.killi8n.board.domain.Reply;
import com.killi8n.board.persistance.BoardDAO;
import com.killi8n.board.service.BoardService;

@Controller
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> replyCreate(@RequestBody Map<String, String> body) {
		String replyText = body.get("replyText");
		String username = body.get("username");
		String boardId = body.get("boardId");
	
		if(replyText.equals("") || username.equals("") || boardId.equals("")) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		try {
			Reply reply = new Reply();
			reply.setReplytext(replyText);
			reply.setUsername(username);
			reply.setBoardId(Integer.parseInt(boardId));
			boardService.CreateReplyByBoardId(reply);
			
			int lastId = boardService.GetLastReply();
			reply = boardService.GetReplyById(lastId);
			
			
			return new ResponseEntity<>(reply, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="", method=RequestMethod.PATCH)
	@ResponseBody
	public ResponseEntity<?> updateReplyById(@RequestBody Map<String, String> body) {
		
		int id = Integer.parseInt(body.get("id"));
		String replyText = body.get("replyText");
		try {
			Reply reply = new Reply();
			reply.setReplytext(replyText);
			reply.setId(id);
		
			boardService.UpdateReplyById(reply);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<?> deleteReplyById(@PathVariable int id) {
		
		try {
			boardService.DeleteReplyById(id);
			
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
