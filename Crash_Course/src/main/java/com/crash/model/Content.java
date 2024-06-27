package com.crash.model;

import java.time.LocalDateTime;

public record Content(
		
		Integer id,
		String title,
		String desc,
		Status status,
		Type contextType,
		LocalDateTime dateCreated,
		LocalDateTime dateUpdated,
		String url
		
		
		) {

}