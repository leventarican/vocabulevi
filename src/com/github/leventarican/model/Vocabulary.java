package com.github.leventarican.model;

public class Vocabulary {
	
	private String source;
	private String solution;
	private String input;
	private String check;
	private Boolean learned;
	
	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return source;
	}
	
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getSolution() {
		return solution;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	public String getInput() {
		return input;
	}
	
	public void setCheck(String check) {
		this.check = check;
	}
	public String getCheck() {
		return check;
	}
	
	public void setLearned(Boolean learned) {
		this.learned = learned;
	}
	public Boolean getLearned() {
		return learned;
	}
}
