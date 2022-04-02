package com.blog.service;

import com.blog.entity.ArticleBody;
import com.blog.vo.ArticleBodyVo;

public interface ArticleBodyService {

    ArticleBodyVo findArticleBodyById(Long bodyId);

    void insert(ArticleBody articleBody);

    void updateArticleBody(ArticleBody articleBody);
}
