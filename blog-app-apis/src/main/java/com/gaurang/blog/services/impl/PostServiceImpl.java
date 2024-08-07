package com.gaurang.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gaurang.blog.entities.Category;
import com.gaurang.blog.entities.Post;
import com.gaurang.blog.entities.User;
import com.gaurang.blog.exceptions.ResourceNotFoundException;
import com.gaurang.blog.payloads.PostDto;
import com.gaurang.blog.payloads.PostResponse;
import com.gaurang.blog.repositories.CategoryRepo;
import com.gaurang.blog.repositories.PostRepo;
import com.gaurang.blog.repositories.UserRepo;
import com.gaurang.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, int userId, int categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "Category id", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setCategory(category);
		post.setUser(user);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, int postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post Id", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post save = this.postRepo.save(post);
		PostDto postDto2 = this.modelMapper.map(save, PostDto.class);
		return postDto2;
	}

	@Override
	public void deletePost(int postId) {
		// TODO Auto-generated method stub
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "Post Id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostDto getPostById(int postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post ", "post Id", postId));
		PostDto postDto2 = this.modelMapper.map(post, PostDto.class);
		return postDto2;
	}

//	@Override
//	public List<PostDto> getAllPost() {
//
//		List<Post> allPost = this.postRepo.findAll();
//
//		List<PostDto> list = allPost.stream().map(post -> this.modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//		return list;
//	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = null;
		if (sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		} else {
			sort = Sort.by(sortBy).descending();
		}
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allPost = pagePost.getContent();
		List<PostDto> list = allPost.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(list);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public List<PostDto> getPostsByCategory(int categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category ", "category Id", categoryId));

		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> list = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return list;
	}

	@Override
	public List<PostDto> getPostsByUser(int userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user Id", userId));

		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> list = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return list;
	}

//	@Override
//	public List<PostDto> searchPosts(String keyword) {
//		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
//		List<PostDto> list = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//		return list;
//	}
	
	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
		List<PostDto> list = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return list;
	}

}
