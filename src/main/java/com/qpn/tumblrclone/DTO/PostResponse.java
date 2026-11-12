package com.qpn.tumblrclone.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponse {
    private String id;
    private Integer userId;
    private String content;
    private LocalDateTime postedTime;
    private List<String> mediaUrl;
}
