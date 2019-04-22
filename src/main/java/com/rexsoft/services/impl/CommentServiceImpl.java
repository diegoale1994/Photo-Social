package com.rexsoft.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rexsoft.models.Comment;
import com.rexsoft.repositories.CommentRepo;
import com.rexsoft.services.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	
	@Override
	public void saveComment(Comment comment) {
		commentRepo.save(comment);
	}

}
