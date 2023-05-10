-- 创建库
create database if not exists DeveloperCommunity;

-- 切换库
use DeveloperCommunity;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户名称',
    userPhone  varchar(256)                           not null comment '用户账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userGender   tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/ admin',
    userPassword varchar(512)                           not null comment '用户密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount unique (userPhone)
) comment '用户表';

#
# -- 文章表
create table if not exists article
(
    id            bigint auto_increment comment 'id' primary key,
    authorId        bigint                             not null comment '创建用户 id',

    title          varchar(512)                       null comment '文章标题',
    content       text                               null comment '文章内容',

    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '文章';

#
# -- 评论表
create table if not exists comment
(
    id            bigint auto_increment comment 'id' primary key,
    articleId		bigint       not null   comment '指向文章',
    authorId	        bigint       not null   comment '评论作者',
    content       text                               null comment '评论内容',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '评论信息';

# -- 信息表
create table if not exists message
(
    id            bigint auto_increment comment 'id' primary key,
    userId	      bigint       not null   comment '用户',
    isRead        tinyint      default 0             not null comment '信息状态',
    content       text                               null comment '信息内容',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '提示信息';

# --好友关注
create table if not exists follow
(
    userId	      bigint       not null   comment '被关注用户ID',
    followId      bigint       not null   comment '关注用户ID',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
)
# -- 字段信息表
# create table if not exists field_info
# (
#     id            bigint auto_increment comment 'id' primary key,
#     name          varchar(512)                       null comment '名称',
#     fieldName     varchar(512)                       null comment '字段名称',
#     content       text                               null comment '字段信息（json）',
#     reviewStatus  int      default 0                 not null comment '状态（0-待审核, 1-通过, 2-拒绝）',
#     reviewMessage varchar(512)                       null comment '审核信息',
#     userId        bigint                             not null comment '创建用户 id',
#     createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete      tinyint  default 0                 not null comment '是否删除'
# ) comment '字段信息';
#
# -- 举报表
# create table if not exists report
# (
#     id             bigint auto_increment comment 'id' primary key,
#     content        text                               not null comment '内容',
#     type           int                                not null comment '举报实体类型（0-词库）',
#     reportedId     bigint                             not null comment '被举报对象 id',
#     reportedUserId bigint                             not null comment '被举报用户 id',
#     status         int      default 0                 not null comment '状态（0-未处理, 1-已处理）',
#     userId         bigint                             not null comment '创建用户 id',
#     createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
#     updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
#     isDelete       tinyint  default 0                 not null comment '是否删除'
# ) comment '举报';