package com.qpn.tumblrclone.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    String token;
    String type = "Bearer";
    long expiresIn = 24 * 60 * 60 * 1000;     // token sống bao lâu (mili-giây)
}
