DROP DATABASE IF EXISTS answerly;
CREATE DATABASE answerly  DEFAULT CHARACTER SET utf8mb4;

USE `answerly`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                        `username`      varchar(256) NOT NULL COMMENT '用户名',
                        `password`      varchar(512) NOT NULL COMMENT '密码',
                        `mail`          varchar(20)  NOT NULL COMMENT '邮箱',
                        `image`         varchar(50)     DEFAULT NULL COMMENT '头像',
                        `phone`         varchar(20)     DEFAULT NULL COMMENT '手机号',
                        `introduction`  varchar(1024)   DEFAULT NULL COMMENT '个人简介',
                        `like_count`    int(11)         DEFAULT 0 COMMENT '点赞数',
                        `solved_count`  int(11)         DEFAULT 0 COMMENT '解决问题的数量',
                        `user_type` ENUM('student', 'volunteer') NOT NULL COMMENT '用户类型',
                        `status` tinyint(4)        DEFAULT 0    COMMENT '状态',
                        `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
                        `del_flag`    tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY idx_unique_username (username) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生和义工';


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `student_id`    varchar(20)  NOT NULL COMMENT '学号',
                            `username`      varchar(256) NOT NULL COMMENT '用户名',
                            `password`      varchar(256) NOT NULL COMMENT '密码',
                            `phone`         varchar(20)  NOT NULL COMMENT '手机号',
                            `status` tinyint(4)        DEFAULT 0 COMMENT '状态',
                            `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
                            `del_flag`    tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='管理员';


DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
                            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `name`  varchar(50)        NOT NULL COMMENT '分类名称',
                            `image` varchar(50)        DEFAULT NULL COMMENT '图片',
                            `sort` int(11)             DEFAULT 0    COMMENT '排序',
                            `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
                            `del_flag`    tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类';


DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                            `category` int(11)         NOT NULL COMMENT '分类ID',
                            `title` varchar(256)       NOT NULL COMMENT '标题',
                            `content` varchar(2048)    DEFAULT NULL COMMENT '内容',
                            `user_id` bigint(20)       DEFAULT NULL COMMENT '发布人ID',
                            `images` varchar(512)      DEFAULT NULL COMMENT '照片路径，最多10张，多张以","隔开',
                            `view_count` int(11)       DEFAULT 0 COMMENT '浏览量',
                            `like_count` int(11)       DEFAULT 0 COMMENT '点赞数',
                            `solved_flag` tinyint(1)   DEFAULT 0 COMMENT '是否解决 0：未解决 1：已解决',
                            `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
                            `del_flag`   tinyint(1)    DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                            PRIMARY KEY (`id`),
                            KEY `idx_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题';


DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                          `category_id` bigint(20)   NOT NULL COMMENT '主题ID',
                          `user_id` bigint(20)       NOT NULL COMMENT '发布人ID',
                          `question_id` bigint(20)   NOT NULL COMMENT '问题ID',
                          `content` varchar(2048)    DEFAULT NULL COMMENT '内容',
                          `images` varchar(512)      DEFAULT NULL COMMENT '照片路径，最多10张，多张以","隔开',
                          `like_count` int(11)       DEFAULT 0 COMMENT '点赞数',
                          `useful` tinyint(1)        DEFAULT 0 COMMENT '是否有用',
                          `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
                          `update_time` datetime     DEFAULT NULL COMMENT '修改时间',
                          `del_flag`    tinyint(1)   DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回复';

