package com.blog.controller;

import com.blog.service.ArticleService;
import com.blog.vo.Result;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//JSON数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticles(limit);
    }

    @PostMapping("new")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping({"view/{id}", "{id}"})
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    //请求方式：POST
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }

    //请求方式：POST
    @PostMapping("update/{id}")
    public Result update(@RequestBody ArticleParam articleParam){

        return articleService.update(articleParam);
    }
}
