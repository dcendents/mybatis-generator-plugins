package com.github.dcendents.mybatis.generator.plugin.wrap;

import lombok.Getter;
import lombok.Setter;

public class ClassDTO extends BaseClassDTO {

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
