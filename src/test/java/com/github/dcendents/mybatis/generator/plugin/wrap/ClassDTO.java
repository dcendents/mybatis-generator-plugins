package com.github.dcendents.mybatis.generator.plugin.wrap;

import lombok.Getter;
import lombok.Setter;

public class ClassDTO {

	@Getter
	@Setter
	private String name;
	
	private String address;
	
	@Getter
	private String street;
	
	@Getter
	@Setter
	private String city;
	
	@Getter
	@Setter
	private String postCode;
	
	private String country;
	
	@Getter
	@Setter
	private boolean homeAddress;
	
	@Getter
	@Setter
	private Boolean workAddress;
}
