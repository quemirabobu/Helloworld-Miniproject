package com.example.miniprogect1.DTO;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponseDTO<T> {
    private List<T> items;
    private Page<T> pItems;
    private T item;
    private String errorMessage;
    private int statusCode;
}
