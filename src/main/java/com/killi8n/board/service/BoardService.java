package com.killi8n.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.killi8n.board.domain.Account;
import com.killi8n.board.domain.Board;
import com.killi8n.board.domain.Search;
import com.killi8n.board.domain.View;
import com.killi8n.board.persistance.BoardDAO;

@Service
public class BoardService {
	@Autowired
	BoardDAO boardDAO;
	
	public void Register(Account account) {
		boardDAO.Register(account);
	}
	
	public int CheckExisting(String username) {
		return boardDAO.CheckExisting(username);
	}
	
	public Account GetUserDetail(String username) {
		return boardDAO.GetUserDetail(username);
	}
	
	public void WriteBoard(Board board) {
		boardDAO.WriteBoard(board);
	}
	
	public List<Board> GetAllBoards(Map<String, String> map) {
		return boardDAO.GetAllBoards(map);
	}
	
	public int GetCount() {
		return boardDAO.GetCount();
	}
	
	public Board GetBoardDetail(int id) {
		return boardDAO.GetBoardDetail(id);
	}
	
	public void UpdateBoard(Board board) {
		boardDAO.UpdateBoard(board);
	}
	
	public void RemoveBoard(int id) {
		boardDAO.RemoveBoard(id);
	}
	
	public List<Board> GetBoardByTitle(Search search)  {
		return boardDAO.GetBoardByTitle(search);
	}
	
	public List<Board> GetBoardByUsername(Search search)  {
		return boardDAO.GetBoardByUsername(search);
	}
	
	public List<Board> GetBoardByContent(Search search)  {
		return boardDAO.GetBoardByContent(search);
	}
	
	public Board GetNextBoardItem(int id) {
		return boardDAO.GetNextBoardItem(id);
	}
	
	public Board GetPrevBoardItem(int id) {
		return boardDAO.GetPrevBoardItem(id);
	}
	
	public int GetFirst() {
		return boardDAO.GetFirst();
	}
	
	public int GetLast() {
		return boardDAO.GetLast();
	}
	
	public void UpdateCount(int id) {
		boardDAO.UpdateCount(id);		
	}
	
	public void ViewThisItem(View view) {
		boardDAO.ViewThisItem(view);
	}
	
	public int CheckViewed(View view) {
		return boardDAO.CheckViewed(view);
	}
}	
