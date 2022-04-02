package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import org.springframework.stereotype.Service;

//@Service
public interface ArticleService {

    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章推荐
     * @param limit
     * @return
     */
    Result hotArticles(int limit);

    /**
     * 最新文章推荐
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);

    Result update(ArticleParam articleParam);
}
