package com.ambulence.exception;

public class ResourceNotFoundException  extends RuntimeException
{
	
	  public ResourceNotFoundException()
	    {
	    	super("Resource Not Found Exception");
	    }
	    
	    
	    public ResourceNotFoundException(String message)
	    {
	    	super(message);
	    }

}
