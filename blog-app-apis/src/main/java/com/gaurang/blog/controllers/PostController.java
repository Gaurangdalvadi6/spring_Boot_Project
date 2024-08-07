package com.gaurang.blog.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gaurang.blog.config.AppConstants;
import com.gaurang.blog.payloads.ApiResponse;
import com.gaurang.blog.payloads.PostDto;
import com.gaurang.blog.payloads.PostResponse;
import com.gaurang.blog.services.FileService;
import com.gaurang.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project-image}")
	private String path;

	// create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable int userId,
			@PathVariable int categoryId) {
		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);

	}

	// get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable int userId) {
		List<PostDto> postsByUser = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(postsByUser, HttpStatus.OK);
	}

	// get by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable int categoryId) {
		List<PostDto> postsByCategory = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(postsByCategory, HttpStatus.OK);
	}

//	 get All posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

//	@GetMapping("/posts")
//	public ResponseEntity<List<PostDto>> getAllPost(){
//		List<PostDto> allPost = this.postService.getAllPost();
//		return new ResponseEntity<List<PostDto>>(allPost,HttpStatus.OK);
//	}

	// get post by id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable int postId) {
		PostDto postById = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
	}

	// update
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable int postId) {
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePost(@PathVariable int postId) {
		this.postService.deletePost(postId);
		return new ApiResponse("Post is delete successfully !!!", true);
	}

	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
		List<PostDto> result = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}

	// post image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable("postId") int postId) throws IOException {
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, image);
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// method to serve files
	@GetMapping(value="post/image/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
	public void downLoadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response) throws IOException{
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
