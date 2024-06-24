package com.movieflix.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

	private Integer movieId;

	@NotBlank(message = "please provide movie's title!")
	private String title;

	@NotBlank(message = "please provide movie's director!")
	private String director;

	@NotBlank(message = "please provide movie's studio!")
	private String studio;

	private Set<String> movieCast;

	private Integer releaseYear;

	@NotBlank(message = "please provide movie's poster!")
	private String poster;
	
	@NotBlank(message = "please provide poster's url")
	private String posterUrl;
	
}
