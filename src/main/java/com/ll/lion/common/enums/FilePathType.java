package com.ll.lion.common.enums;

import lombok.Getter;

@Getter
public enum FilePathType {
    PRODUCT("product"),
    PROFILE("profile");

    private final String value;

    FilePathType(String value) {
        this.value = value;
    }
}
