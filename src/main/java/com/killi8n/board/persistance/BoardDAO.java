package com.killi8n.board.persistance;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.killi8n.board.domain.Account;
import com.killi8n.board.domain.Board;
import com.killi8n.board.domain.Good;
import com.killi8n.board.domain.Reply;
import com.killi8n.board.domain.Search;
import com.killi8n.board.domain.View;

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
	
	public List<Board> GetBoardByTitle(Search search) {
		return session.selectList(NAME_SPACE + ".GetBoardByTitle", search);
	}
	
	public List<Board> GetBoardByUsername(Search search) {
		return session.selectList(NAME_SPACE + ".GetBoardByUsername", search);
	}
	
	public List<Board> GetBoardByContent(Search search) {
		return session.selectList(NAME_SPACE + ".GetBoardByContent", search);
	}
	
	public Board GetNextBoardItem(int id) {
		return session.selectOne(NAME_SPACE + ".GetNextBoardItem", id);
	}
	
	public Board GetPrevBoardItem(int id) {
		return session.selectOne(NAME_SPACE + ".GetPrevBoardItem", id);
	}
	
	public int GetFirst() {
		return session.selectOne(NAME_SPACE + ".GetFirst");
	}
	
	public int GetLast() {
		return session.selectOne(NAME_SPACE + ".GetLast");
	}
	
	public void UpdateCount(int id) {
		session.update(NAME_SPACE + ".UpdateCount", id);
	}
	
	public void ViewThisItem(View view) {
		session.insert(NAME_SPACE + ".ViewThisItem", view);
	}
	
	public int CheckViewed(View view) {
		return session.selectOne(NAME_SPACE + ".CheckViewed", view);
	}
	
	public void CheckGood(Good good) {
		session.insert(NAME_SPACE + ".CheckGood", good);
	}
	
	public void PlusGood(Good good) {
		session.update(NAME_SPACE + ".PlusGood", good);
	}
	
	public void MinusGood(Good good) {
		session.update(NAME_SPACE + ".MinusGood", good);
	}
	
	public int CheckGoodExist(Good good) {
		return session.selectOne(NAME_SPACE + ".CheckGoodExist", good);
	}
	
	public void DeleteGood(Good good) {
		session.delete(NAME_SPACE + ".DeleteGood", good);
	}
	
	public int CreateReplyByBoardId(Reply reply) {
		return session.insert(NAME_SPACE + ".CreateReplyByBoardId", reply);
	}
	
	public List<Reply> GetReplyListByBoardId(int boardId) {
		return session.selectList(NAME_SPACE + ".GetReplyListByBoardId", boardId);
	}
	
	public int GetLastReply() {
		return session.selectOne(NAME_SPACE + ".GetLastReply");
	}
	
	public Reply GetReplyById(int id) {
		return session.selectOne(NAME_SPACE + ".GetReplyById", id);
	}
	
	public void UpdateReplyById(Reply reply) {
		session.update(NAME_SPACE + ".UpdateReplyById", reply);
	}
	
	public void DeleteReplyById(int id) {
		session.delete(NAME_SPACE + ".DeleteReplyById", id);
	}
}
