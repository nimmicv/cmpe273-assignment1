package edu.sjsu.cmpe.library.domain;

public class Author {
	
	private String name;
	private long id;

	public  long getId() {
		return id;
	}

	public  void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}