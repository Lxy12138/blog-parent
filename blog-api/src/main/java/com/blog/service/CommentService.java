package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.CommentParam;
import org.springframework.stereotype.Component;

public interface CommentService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
