package com.rexsoft.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rexsoft.models.Post;
import com.rexsoft.models.User;
import com.rexsoft.repositories.PostRepo;
import com.rexsoft.services.PostService;

import utility.Constants;
@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Override
	public Post savePost(User user, HashMap<String, String> request, String PostImageName) {
		String caption = request.get("caption");
		String location = request.get("location");
		Post post = new Post();
		post.setCaption(caption);
		post.setLocation(location);
		post.setUsername(user.getUsername());
		post.setPostedDate(new Date());
		post.setUserImageId(user.getId());
		user.setPost(post);
		postRepo.save(post);
		return post;
	}

	@Override
	public List<Post> postList() {
		return postRepo.findAll();
	}

	@Override
	public Post getPostById(Long id) {
		return postRepo.findPostById(id);
	}

	@Override
	public List<Post> findPostByUsername(String username) {
		return postRepo.findByUsername(username);
	}

	@Override
	public Post deletePost(Post post) {
		try {
			Files.deleteIfExists(Paths.get(Constants.POST_FOLDER + "/"+ post.getName() + ".png"));
			Post postB = post;
			postRepo.delete(post);
			return postB;
		}catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public String savePostImage(HttpServletRequest request, String fileName) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> it = multipartRequest.getFileNames();
		MultipartFile multipartFile = multipartRequest.getFile(it.next());
		try {
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.POST_FOLDER + fileName + ".png");
			Files.write(path, bytes, StandardOpenOption.CREATE);
		}catch (Exception e) {
			return "Error ocurre, Photo Not saved";
		}
		return "Photo saved successfully";
	}

}
