package com.ll.lion.common.dto;

import java.util.List;

public class ResponseDto<T> {
    private String error;
    private List<T> listData;
    private T objData;

    public ResponseDto(T obj) {
        this.objData = obj;
    }
}