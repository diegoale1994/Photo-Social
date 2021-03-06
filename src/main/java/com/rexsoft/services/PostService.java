package com.rexsoft.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rexsoft.models.Post;
import com.rexsoft.models.User;

public interface PostService {
	
	public Post savePost(User user, HashMap<String, String> request, String PostImageName);
	public List<Post> postList();
	public Post getPostById(Long id);
	public List<Post> findPostByUsername(String username);
	public Post deletePost(Post post);
	public String savePostImage(MultipartFile multipartFile, String fileName);
	
}
