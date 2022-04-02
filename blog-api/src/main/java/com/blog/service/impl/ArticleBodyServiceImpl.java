package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.entity.ArticleBody;
import com.blog.mapper.ArticleBodyMapper;
import com.blog.service.ArticleBodyService;
import com.blog.vo.ArticleBodyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {

    @Autowired
    ArticleBodyMapper articleBodyMapper;

    @Override
    public ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public void insert(ArticleBody articleBody) {
        articleBodyMapper.insert(articleBody);
    }

    @Override
    public void updateArticleBody(ArticleBody articleBody) {
        LambdaUpdateWrapper<ArticleBody> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ArticleBody::getId, articleBody.getId());

        articleBodyMapper.update(articleBody,updateWrapper);
    }


}
