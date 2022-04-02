package com.blog.controller;

import com.blog.service.CommentService;
import com.blog.vo.Result;
import com.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    CommentService commentService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id) {
        Result result = commentService.commentsByArticleId(id);
        return result;
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
