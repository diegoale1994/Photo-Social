package com.rexsoft.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rexsoft.models.Comment;
import com.rexsoft.models.Post;
import com.rexsoft.models.User;
import com.rexsoft.services.AccountService;
import com.rexsoft.services.CommentService;
import com.rexsoft.services.PostService;

@RestController
@RequestMapping("/post")
public class PostResource {

	@Autowired
	private PostService postService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CommentService commentService;

	private String postImageName;

	@GetMapping("/list")
	public List<Post> getPostList() {
		return postService.postList();
	}

	@GetMapping("/getPostById/{postId}")
	public ResponseEntity<?> getPost(@PathVariable("postId") Long id) {
		Post post = postService.getPostById(id);
		if (post == null) {
			return new ResponseEntity<>("No post found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(post, HttpStatus.OK);
		
	}

	@GetMapping("/getPostByUsername/{username}")
	public ResponseEntity<?> getPostByUsername(@PathVariable("postId") String username) {
		User user = accountService.findByUsername(username);

		if (user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}

		try {
			List<Post> postList = postService.findPostByUsername(username);
			return new ResponseEntity<>(postList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/save")
	public ResponseEntity<?> savePost(@RequestBody HashMap<String, String> request) {
		String username = request.get("username");
		User user = accountService.findByUsername(username);

		if (user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}

		postImageName = RandomStringUtils.randomAlphabetic(10);
		try {
			Post post = postService.savePost(user, request, postImageName);
			return new ResponseEntity<>(post, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
		Post post = postService.getPostById(id);
		if (post == null) {
			return new ResponseEntity<>("No post found", HttpStatus.NOT_FOUND);
		}
		try {
			postService.deletePost(post);
			return new ResponseEntity<>(post, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/like")
	public ResponseEntity<String> like(@RequestBody HashMap<String, String> request) {
		String postId = request.get("postId");
		Post post = postService.getPostById(Long.parseLong(postId));
		if (post == null) {
			return new ResponseEntity<>("No post found", HttpStatus.NOT_FOUND);
		}
		String username = request.get("username");
		User user = accountService.findByUsername(username);
		if (user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}
		try {
			post.setLikes(post.getLikes() + 1);
			user.setLikedPost(post);
			accountService.simpleSave(user);
			return new ResponseEntity<>("Post was liked", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/unlike")
	public ResponseEntity<String> unlike(@RequestBody HashMap<String, String> request) {
		String postId = request.get("postId");
		Post post = postService.getPostById(Long.parseLong(postId));
		if (post == null) {
			return new ResponseEntity<>("No post found", HttpStatus.NOT_FOUND);
		}
		String username = request.get("username");
		User user = accountService.findByUsername(username);
		if (user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}
		try {
			post.setLikes(post.getLikes() - 1);
			user.getLikedPost().remove(post);
			accountService.simpleSave(user);
			return new ResponseEntity<>("Post was uliked", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/comment/add")
	public ResponseEntity<?> addComment(@RequestBody HashMap<String, String> request) {
		String postId = request.get("postId");
		Post post = postService.getPostById(Long.parseLong(postId));
		if (post == null) {
			return new ResponseEntity<>("No post found", HttpStatus.NOT_FOUND);
		}
		String username = request.get("username");
		User user = accountService.findByUsername(username);
		if (user == null) {
			return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
		}
		String content = request.get("content");
		try {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setUsername(username);
			comment.setPostedDate(new Date());
			post.setCommentsList(comment);
			commentService.saveComment(comment);
			return new ResponseEntity<>(comment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/photo/upload")
	public ResponseEntity<String> fileUpload(HttpServletRequest request) {
		try {
			postService.savePostImage(request, postImageName);
			return new ResponseEntity<>("picture saved", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error ocurred ", HttpStatus.BAD_REQUEST);
		}
	}
}
