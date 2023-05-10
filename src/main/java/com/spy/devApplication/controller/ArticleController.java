package com.spy.devApplication.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.spy.devApplication.common.BaseResponse;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.common.ResultUtils;
import com.spy.devApplication.constant.CommonConstant;
import com.spy.devApplication.exception.BusinessException;
import com.spy.devApplication.modal.dto.article.ArticleAddRequest;
import com.spy.devApplication.modal.dto.article.ArticleQueryRequest;
import com.spy.devApplication.modal.dto.article.ArticleSearchRequest;
import com.spy.devApplication.modal.dto.article.ArticleUpdateRequest;
import com.spy.devApplication.modal.entity.Article;
import com.spy.devApplication.modal.entity.Comment;
import com.spy.devApplication.modal.entity.User;
import com.spy.devApplication.modal.vo.article.ArticleVO;
import com.spy.devApplication.modal.vo.comment.CommentVO;
import com.spy.devApplication.modal.vo.user.UserVO;
import com.spy.devApplication.service.ArticleService;
import com.spy.devApplication.service.CommentService;
import com.spy.devApplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.spy.devApplication.constant.UserConstant.ADMIN_ROLE;

/**
 * 文章接口
 */

@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Resource
    private UserService userService;

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    // region 增删改查

    /**
     * 添加文章
     * @param articleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse addArticle(@RequestBody ArticleAddRequest articleAddRequest, HttpServletRequest request) {
        if(articleAddRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Article article = new Article();
        User authorUser = userService.getLoginUser(request);

        String title = articleAddRequest.getTitle();
        String content = articleAddRequest.getContent();
        if(StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 标题校验
        if(title.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题长度超过30");
        }

        // 文章校验
        if(content.length() > 600) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章长度超过600");
        }

        article.setAuthorId(authorUser.getId());
        article.setTitle(title);
        article.setContent(content);

        boolean addResult = articleService.save(article);
        if(!addResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文章添加失败");
        }
        return ResultUtils.success(article.getId());
    }


    /**
     * 删除
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    public BaseResponse deleteArticle(Long id, HttpServletRequest request) {
        if(id <= 0 || id == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验文章
        User loginUser = userService.getLoginUser(request);
        Article article = articleService.getById(id);
        if(article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
        }

        if(!((loginUser.getUserRole().equals(ADMIN_ROLE)) || (loginUser.getId() == article.getAuthorId()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean removeResult = articleService.removeById(id);

        if(!removeResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新文章
     * @param articleUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest, HttpServletRequest request) {
        if(articleUpdateRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long id = articleUpdateRequest.getId();
        Article article = articleService.getById(id);
        if(article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
        }

        if(!((loginUser.getUserRole().equals(ADMIN_ROLE)) || (loginUser.getId() == article.getAuthorId()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Article newArticle = new Article();

        Long authorId = articleUpdateRequest.getAuthorId();
        if(authorId != null) {
            if(authorId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "作者id格式错误");
            }

            if(loginUser.getUserRole().equals(ADMIN_ROLE)) {
                newArticle.setId(authorId);
            } else {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        } else {
            newArticle.setAuthorId(article.getAuthorId());
        }

        String title = articleUpdateRequest.getTitle();
        if(StringUtils.isNotBlank(title)) {
            if(title.length() > 30) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题格式错误");
            }
            newArticle.setTitle(title);
        } else {
            newArticle.setTitle(article.getTitle());
        }

        String content = articleUpdateRequest.getContent();
        if(StringUtils.isNotBlank(content)) {
            if(content.length() > 600) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章内容错误");
            }
            newArticle.setContent(content);
        } else {
            newArticle.setContent(article.getContent());
        }

        newArticle.setId(id);
        boolean updateResult = articleService.updateById(newArticle);
        if(!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据id获取文章
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse getArticleById(Long id) {

        if(id <= 0 || id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Article article = articleService.getById(id);

        if(article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在");
        }

        User author = userService.getById(article.getAuthorId());
        UserVO authorVO = new UserVO();
        BeanUtils.copyProperties(author, authorVO);

        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setAuthor(authorVO);

        // 获取文章的全部评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("articleId", articleVO.getId());
        queryWrapper.orderByDesc("createTime");

        List<Comment> commentList = commentService.list(queryWrapper);
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);

            Long authorId = comment.getAuthorId();
            User commentAuthor = userService.getById(authorId);
            UserVO commentAuthorVO = new UserVO();
            BeanUtils.copyProperties(commentAuthor, commentAuthorVO);
            commentVO.setAuthor(commentAuthorVO);
            return commentVO;
        }).collect(Collectors.toList());
        articleVO.setComments(commentVOList);

        return ResultUtils.success(articleVO);
    }

    @PostMapping("/search/page")
    public BaseResponse searchArticle(@RequestBody ArticleSearchRequest articleSearchRequest, HttpServletRequest request) {
        if(articleSearchRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Integer current = 1;
        Integer pageSize = 20;

        if(articleSearchRequest.getCurrent() != null && articleSearchRequest.getCurrent() > 0) {
            current = articleSearchRequest.getCurrent();
        }
        if(articleSearchRequest.getPageSize() != null && articleSearchRequest.getPageSize() > 0) {
            pageSize = articleSearchRequest.getPageSize();
        }

        String queryParam = articleSearchRequest.getParam();

        // 文章标题
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();

        if(queryParam != null) {
            articleQueryWrapper.like("title", queryParam);
            // List<Article> articleList = articleService.list(articleQueryWrapper);

            // 用户
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("userName", queryParam);
            List<User> userList = userService.list(userQueryWrapper);

            // idList
            List<Long> authorIdList = new ArrayList<>();
            for (User user : userList) {
                authorIdList.add(user.getId());
            }

            if(authorIdList.size() != 0) {
                articleQueryWrapper.or().in("authorId", authorIdList);
            }
        }


        Page<Article> articlePage = articleService.page(new Page<>(current, pageSize), articleQueryWrapper);
        PageDTO<ArticleVO> articleVOPage = new PageDTO<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());

        List<ArticleVO> articleVOList = articlePage.getRecords().stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);

            // 查询作者
            User author = userService.getById(article.getAuthorId());
            UserVO authorVO = new UserVO();
            BeanUtils.copyProperties(author, authorVO);
            articleVO.setAuthor(authorVO);

            return articleVO;
        }).collect(Collectors.toList());
        articleVOPage.setRecords(articleVOList);
        return ResultUtils.success(articleVOPage);
    }

    /**
     * 按照页数查询文章
     * @param articleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse getArticleByPage(@RequestBody ArticleQueryRequest articleQueryRequest, HttpServletRequest request) {
        if(articleQueryRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Integer current = 1;
        Integer pageSize = 20;

        if(articleQueryRequest.getCurrent() != null && articleQueryRequest.getCurrent() > 0) {
            current = articleQueryRequest.getCurrent();
        }
        if(articleQueryRequest.getPageSize() != null && articleQueryRequest.getPageSize() > 0) {
            pageSize = articleQueryRequest.getPageSize();
        }

        Long id = articleQueryRequest.getId();
        Long authorId = articleQueryRequest.getAuthorId();

        String title = articleQueryRequest.getTitle();
        String content = articleQueryRequest.getContent();

        Date createTimeStart = articleQueryRequest.getCreateTimeStart();
        Date createTimeEnd = articleQueryRequest.getCreateTimeEnd();

        Date updateTimeStart = articleQueryRequest.getUpdateTimeStart();
        Date updateTimeEnd = articleQueryRequest.getUpdateTimeEnd();

        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(id != null && id > 0,"id", id);
        queryWrapper.eq(authorId != null && authorId > 0,"authorId", authorId);
        queryWrapper.like(StringUtils.isNotBlank(title),"title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);

        // 更新时间
        queryWrapper.ge(updateTimeStart != null, "updateTime", updateTimeStart);
        queryWrapper.le( updateTimeEnd != null, "updateTime", updateTimeEnd);

        // 创建时间
        queryWrapper.ge(createTimeStart != null, "createTime", createTimeStart);
        queryWrapper.le( createTimeEnd != null, "createTime", createTimeEnd);

        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        Page<Article> articlePage = articleService.page(new Page<>(current, pageSize), queryWrapper);
        PageDTO<ArticleVO> articleVOPage = new PageDTO<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());

        List<ArticleVO> articleVOList = articlePage.getRecords().stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);

            // 查询作者
            User author = userService.getById(article.getAuthorId());
            UserVO authorVO = new UserVO();
            BeanUtils.copyProperties(author, authorVO);
            articleVO.setAuthor(authorVO);

            return articleVO;
        }).collect(Collectors.toList());
        articleVOPage.setRecords(articleVOList);
        return ResultUtils.success(articleVOPage);
    }

    // endregion
}
