package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ThreadService;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl implements ThreadService
{
    @Override
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();

        BeanUtils.copyProperties(article,articleUpdate);

        articleUpdate.setViewCounts(viewCounts + 1);
//        articleUpdate.setWeight(null);
//        articleUpdate.setViewCounts(null);

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        updateWrapper.eq(Article::getViewCounts,article.getViewCounts());

        articleMapper.update(articleUpdate,updateWrapper);

    }
}
