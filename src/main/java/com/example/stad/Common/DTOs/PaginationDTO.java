package com.example.stad.Common.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO<T> {
    private long totalElements; // total number of elements
    private int totalPages; // total number of pages
    private int size; // number of elements in a page
    private int number; // current page number
    private int numberOfElements; // number of elements in the current page
    private List<T> content; // list of elements

}
