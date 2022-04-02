package com.blog.service;

import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import org.springframework.stereotype.Component;

@Component
public interface ThreadService {

    //在线程池中完成增加阅读量操作，不影响主线程
    void updateArticleViewCount(ArticleMapper articleMapper, Article article);
}
