package edu.sjsu.cmpe.library.domain;

public enum Status {
	
	Available("available"),
	Checked_out("checked_out"),
	In_Queue("in_queue"),
	Lost("lost");
	
	 private String id;

	    private Status(String id) {
	        this.id = id;
	    }

	  
	    public String getId() {
	        return id;
	    }
	

}
