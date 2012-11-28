package com.interzonedev.requestr.web.send;

import org.hibernate.validator.constraints.NotEmpty;

public class JsonForm {

	@NotEmpty
	private String input;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

}
