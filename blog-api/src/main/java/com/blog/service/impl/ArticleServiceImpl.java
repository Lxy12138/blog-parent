package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Article;
import com.blog.entity.ArticleBody;
import com.blog.entity.ArticleTag;
import com.blog.entity.SysUser;
import com.blog.mapper.ArticleBodyMapper;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.service.*;
import com.blog.utils.UserThreadLocal;
import com.blog.vo.*;
import com.blog.vo.params.ArticleParam;
import com.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TagService tagService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    ArticleBodyService articleBodyService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ThreadService threadService;
    @Autowired
    ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 1. 分页查询 article数据库表
         */
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size() > 0){
                queryWrapper.in(Article::getId,articleIdList);
            }
        }

        //是否置顶进行排序
        //order by create_date desc
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        //能直接返回吗？ 很明显不能
        List<ArticleVo> articleVoList = copyList(records,true,true, false, false);
        return Result.success(articleVoList);
    }


    @Override
    public Result hotArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.last("limit " + limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false, false, false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.last("limit " + limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false,  false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        Article article = this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true,true,true);

        threadService.updateArticleViewCount(articleMapper,article);

        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(articleParam.getCategory().getId());
        //插入之后 会生成一个文章id
        articleMapper.insert(article);
        //ArticleTag添加
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody  = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyService.insert(articleBody);
        //给article上bodyId
        article.setBodyId(articleBody.getId());
        //更新article
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result update(ArticleParam articleParam) {

        Long articleId = articleParam.getId();
        Article article = articleMapper.selectById(articleId);


        ArticleBody articleBody = new ArticleBody();

        articleBody.setId(article.getBodyId());
        articleBody.setArticleId(articleId);
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyService.updateArticleBody(articleBody);


        Map<String,String> map = new HashMap<>();
        map.put("id",articleBody.getArticleId().toString());
        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean hasTag, boolean hasAuthor,boolean hasBody,boolean hasCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();

        for (Article record : records) {
            articleVoList.add(copy(record, hasTag, hasAuthor, hasBody, hasCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean hasTag, boolean hasAuthor,boolean hasBody,boolean hasCategory) {
        ArticleVo articleVo = new ArticleVo();

        BeanUtils.copyProperties(article,articleVo);

//        articleVo.setId(String.valueOf(article.getId()));

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        if (hasTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }

        if (hasAuthor) {
            Long authorId = article.getAuthorId();
            UserVo authorVo = new UserVo();
            authorVo.setId(sysUserService.findUserById(authorId).getId());
            authorVo.setAvatar(sysUserService.findUserById(authorId).getAvatar());            authorVo.setId(sysUserService.findUserById(authorId).getId());
            authorVo.setNickname(sysUserService.findUserById(authorId).getNickname());
            articleVo.setAuthor(authorVo);
        }

        if (hasBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(articleBodyService.findArticleBodyById(bodyId));
        }

        if (hasCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }


        return articleVo;
    }


}
