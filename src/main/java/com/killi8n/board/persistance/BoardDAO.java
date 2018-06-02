package com.killi8n.board.persistance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.killi8n.board.domain.Account;
import com.killi8n.board.domain.Board;

@Repository
public class BoardDAO {
	@Autowired
	SqlSession session;
	
	private static final String NAME_SPACE = "com.killi8n.board.mappers.LastTermBoardMapper";
	
	public void Register(Account account) {
		session.insert(NAME_SPACE + ".Register", account);
	}	
	
	public int CheckExisting(String username) {
		return session.selectOne(NAME_SPACE + ".CheckExisting", username); 
	}
	
	public Account GetUserDetail(String username) {
		return session.selectOne(NAME_SPACE + ".GetUserDetail", username);
	}
	
	public void WriteBoard(Board board) {
		session.insert(NAME_SPACE + ".WriteBoard", board);
	}
	
	public List<Board> GetAllBoards(Map<String, String> map) {
		return session.selectList(NAME_SPACE + ".GetAllBoards", map);
	}
	
	public int GetCount() {
		return session.selectOne(NAME_SPACE + ".GetCount");
	}
	
	public Board GetBoardDetail(int id) {
		return session.selectOne(NAME_SPACE + ".GetBoardDetail", id);
	}
	
	public void UpdateBoard(Board board) {
		session.update(NAME_SPACE + ".UpdateBoard", board);
	}
	
	public void RemoveBoard(int id) {
		session.delete(NAME_SPACE + ".RemoveBoard", id);
	}
}
