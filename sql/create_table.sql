-- auto-generated definition
create table user
(
    avatarUrl    varchar(1024) default 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png' null comment '用户头像',
    gender       tinyint                                                                                             null comment '性别
',
    userPassword varchar(512)                                                                                        not null comment '密码',
    phone        varchar(128)                                                                                        null comment '电话',
    email        varchar(512)                                                                                        null comment '邮箱',
    userStatus   int           default 0                                                                             not null comment '状态
0 - 正常',
    createTime   datetime      default CURRENT_TIMESTAMP                                                             null comment '创建时间',
    updateTime   datetime      default CURRENT_TIMESTAMP                                                             null on update CURRENT_TIMESTAMP,
    isDelete     tinyint       default 0                                                                             not null comment '是否删除',
    planetCode   varchar(512)                                                                                        null comment '编号/学号冗余字段 暂未用',
    id           bigint auto_increment
        primary key,
    username     varchar(256)                                                                                        null comment '用户昵称',
    userAccount  varchar(256)                                                                                        null comment '账号',
    userRole     int           default 0                                                                             not null comment '用户角色 0 - 普通用户 1 - 管理员

'
)
    comment '用户';



-- auto-generated definition
create table minzu
(
    minzuName   varchar(128)                       null comment '资源名',
    id          bigint auto_increment comment 'id'
        primary key,
    minzuUrl    varchar(1024)                      not null comment 'url',
    minzuType   varchar(64)                        not null comment '民族类型',
    minzuSource int                                not null comment '0 - 图片 1 - 音频 2 - 视频',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null,
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment 'nation';

