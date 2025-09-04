package com.user_service.payload;

import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
public class ApiResponse {
	
	private String message;
	private boolean success;
	private HttpStatus status;
	private Object data;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ApiResponse(String message, boolean success, HttpStatus status, Object data) {
		super();
		this.message = message;
		this.success = success;
		this.status = status;
		this.data = data;
	}
	public ApiResponse() {
		super();

	}
	@Override
	public String toString() {
		return "ApiResponse [message=" + message + ", success=" + success + ", status=" + status + ", data=" + data
				+ "]";
	}
	
	

}
