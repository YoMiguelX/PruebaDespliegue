package com.example.demo.Dto.Response;

import java.util.List;

public class ApiResponse<T> {

    private int httpStatusCode;
    private String message;
    private T data;
    private List<String> errors;
    private Object extraData;
    private String token;
    // Paginación
    private Integer totalRecords;
    private Integer pageNumber;
    private Integer pageSize;


    private boolean success;


    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.message = "OK";
    }
    public Integer getTotalPages() {
        if (pageSize != null && pageSize > 0 && totalRecords != null)
            return (int) Math.ceil((double) totalRecords / pageSize);
        return null;
    }

    // --- Constructores ---
    public ApiResponse() {}

    public ApiResponse(int httpStatusCode, String message, T data) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.data = data;
    }

    // --- Getters & Setters ---

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getHttpStatusCode() { return httpStatusCode; }
    public void setHttpStatusCode(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }

    public Object getExtraData() { return extraData; }
    public void setExtraData(Object extraData) { this.extraData = extraData; }

    public Integer getTotalRecords() { return totalRecords; }
    public void setTotalRecords(Integer totalRecords) { this.totalRecords = totalRecords; }

    public Integer getPageNumber() { return pageNumber; }
    public void setPageNumber(Integer pageNumber) { this.pageNumber = pageNumber; }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    public String getToken(){ return token;}
    public void setToken(String token){this.token = token;}
}
