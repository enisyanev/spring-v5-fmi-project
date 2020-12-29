package com.project.spring.digitalwallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    @NonNull
    private String message;
    private List<String> violations = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp = new Date();

    public ErrorResponse(int code, @NonNull String message, List<String> violations) {
        this.code = code;
        this.message = message;
        this.violations = violations;
    }

	public ErrorResponse(int code, String message) {
		this.code = code;
        this.message = message;
	}
}
