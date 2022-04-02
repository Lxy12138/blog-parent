package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ArticleTag {


    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private Long tagId;
}
