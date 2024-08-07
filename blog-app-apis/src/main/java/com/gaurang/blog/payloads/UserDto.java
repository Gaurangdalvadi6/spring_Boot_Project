package com.gaurang.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gaurang.blog.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor@Getter@Setter
public class UserDto {

	private int id;
	@NotEmpty
	@Size(min = 3,message = "Username must be min of 3 characters")
	private String name;
	@Email(message = "Please Enter Valid Email !!")
	private String email;
	@NotEmpty
	//@JsonIgnore
	@Size(min=3,max = 10,message = "Password must be min 3 chars and max 10 chars !!")
	private String password;
	private String about;
	
	private Set<RoleDto> roles = new HashSet();
}
