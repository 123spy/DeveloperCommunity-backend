package com.spy.devApplication.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spy.devApplication.common.BaseResponse;
import com.spy.devApplication.common.ErrorCode;
import com.spy.devApplication.common.ResultUtils;
import com.spy.devApplication.exception.BusinessException;
import com.spy.devApplication.modal.dto.article.ArticleAddRequest;
import com.spy.devApplication.modal.dto.article.ArticleUpdateRequest;
import com.spy.devApplication.modal.dto.comment.CommentAddRequest;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.spy.devApplication.constant.UserConstant.ADMIN_ROLE;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Resource
    private ArticleService articleService;

    //增删改查


    @PostMapping("/add")
    public BaseResponse addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if(commentAddRequest == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        User loginUser = userService.getLoginUser(request);

        String content = commentAddRequest.getContent();
        Long authorId = commentAddRequest.getAuthorId();
        Long articleId = commentAddRequest.getArticleId();

        if(articleId == null || articleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Article article = articleService.getById(articleId);

        if(article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
        }

        if(StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论内容不能为空");
        }


        if(authorId == null) {
            authorId = loginUser.getId();
        }

        User author = userService.getById(authorId);
        if(author == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Comment comment = new Comment();
//        BeanUtils.copyProperties(commentAddRequest, comment);

        comment.setAuthorId(author.getId());
        comment.setContent(content);
        comment.setArticleId(articleId);

        boolean addResult = commentService.save(comment);

        if(!addResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "评论添加失败");
        }
        return ResultUtils.success(comment.getId());
    }


    /**
     * 删除
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    public BaseResponse deleteComment(Long id, HttpServletRequest request) {
        if(id <= 0 || id == null || request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验文章
        User loginUser = userService.getLoginUser(request);

        Comment comment = commentService.getById(id);

        if(comment == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
        }

        if(!((loginUser.getUserRole().equals(ADMIN_ROLE)) || (loginUser.getId() == comment.getAuthorId()))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean removeResult = commentService.removeById(id);

        if(!removeResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

//    /**
//     * 更新文章
//     * @param articleUpdateRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/update")
//    public BaseResponse updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest, HttpServletRequest request) {
//        if(articleUpdateRequest == null || request == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        Long id = articleUpdateRequest.getId();
//        Article article = articleService.getById(id);
//        if(article == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章不存在");
//        }
//
//        if(!((loginUser.getUserRole().equals(ADMIN_ROLE)) || (loginUser.getId() == article.getAuthorId()))) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//
//        Article newArticle = new Article();
//
//        Long authorId = articleUpdateRequest.getAuthorId();
//        if(authorId != null) {
//            if(authorId <= 0) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "作者id格式错误");
//            }
//
//            if(loginUser.getUserRole().equals(ADMIN_ROLE)) {
//                newArticle.setId(authorId);
//            } else {
//                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//            }
//        } else {
//            newArticle.setAuthorId(article.getAuthorId());
//        }
//
//        String title = articleUpdateRequest.getTitle();
//        if(StringUtils.isNotBlank(title)) {
//            if(title.length() > 30) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题格式错误");
//            }
//            newArticle.setTitle(title);
//        } else {
//            newArticle.setTitle(article.getTitle());
//        }
//
//        String content = articleUpdateRequest.getContent();
//        if(StringUtils.isNotBlank(content)) {
//            if(content.length() > 600) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章内容错误");
//            }
//            newArticle.setContent(content);
//        } else {
//            newArticle.setContent(article.getContent());
//        }
//
//        boolean updateResult = articleService.updateById(newArticle);
//        if(!updateResult) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
//        }
//        return ResultUtils.success(true);
//    }
//
//    /**
//     * 根据id获取文章
//     * @param id
//     * @return
//     */
//    @GetMapping("/get")
//    public BaseResponse getArticleById(Long id) {
//
//        if(id <= 0 || id == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//
//        Article article = articleService.getById(id);
//
//        if(article == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在");
//        }
//
//        User author = userService.getById(article.getAuthorId());
//        UserVO authorVO = new UserVO();
//        BeanUtils.copyProperties(author, authorVO);
//
//        ArticleVO articleVO = new ArticleVO();
//        BeanUtils.copyProperties(article, articleVO);
//        articleVO.setAuthor(authorVO);
//
//        // 获取文章的全部评论
//        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("articleId", articleVO.getId());
//
//        List<Comment> commentList = commentService.list(queryWrapper);
//        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
//            CommentVO commentVO = new CommentVO();
//            BeanUtils.copyProperties(comment, commentVO);
//
//            Long authorId = comment.getAuthorId();
//            User commentAuthor = userService.getById(authorId);
//            UserVO commentAuthorVO = new UserVO();
//            BeanUtils.copyProperties(commentAuthor, commentAuthorVO);
//            commentVO.setAuthor(commentAuthorVO);
//            return commentVO;
//        }).collect(Collectors.toList());
//        articleVO.setComments(commentVOList);
//
//        return ResultUtils.success(articleVO);
//    }
}
