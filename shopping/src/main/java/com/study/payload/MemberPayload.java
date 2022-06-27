package com.study.payload;

import lombok.Data;

@Data
public class MemberPayload {
    private String id;
    private String passwd;
    private String newPasswd;
}
