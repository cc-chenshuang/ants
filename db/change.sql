-- chenshuang  增加收藏夹数据表    2021-10-18
CREATE TABLE `article_favorites`
(
    `id`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
    `name`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `article_describe` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '描述',
    `create_by`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`      datetime                                                      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- chenshuang  增加收藏夹文章数据关联表    2021-10-18
CREATE TABLE `article_favorites_sub`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
    `main_id`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收藏夹ID',
    `article_id`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文章id',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- chenshuang  增加系统评分表    2021-10-20
CREATE TABLE `article_score`
(
    `id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
    `score`       int                                                          DEFAULT NULL COMMENT '分数',
    `proposal`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '建议',
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `create_time` datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
    `update_time` datetime                                                     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统评价';

