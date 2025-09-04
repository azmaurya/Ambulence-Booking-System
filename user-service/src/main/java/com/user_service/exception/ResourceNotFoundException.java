package com.user_service.exception;

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
