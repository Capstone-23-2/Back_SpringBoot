create table if not exists RtoU.users
(
    userid varchar(20) primary key,
    sso    varchar(10) not null
);

create table if not exists RtoU.attention
(
    id     bigint auto_increment primary key,
    userid varchar(20) not null,
    date   char(8)     not null
);

create table if not exists RtoU.characterinfo
(
    name      varchar(15) primary key,
    voicename varchar(10) not null,
    pitch     double      not null
);

create table if not exists RtoU.conversations
(
    id            bigint auto_increment primary key,
    userid        varchar(20)  not null,
    audiofilelink varchar(100) not null,
    usersentence  varchar(100) not null,
    index idx_conversations (userid)
);

create table if not exists RtoU.conversationcharacter
(
    userid        varchar(20) primary key,
    charactername varchar(15) not null
);

create table if not exists RtoU.estimations
(
    id                 bigint auto_increment primary key,
    userid             varchar(20)  not null,
    sentence           varchar(100) not null,
    accuracyscore      double       not null,
    pronunciationscore double       not null,
    fluencyscore       double       not null,
    completnessscore   double       not null,
    index idx_estimation (userid)
);

create table if not exists RtoU.errorwords
(
    id        bigint auto_increment primary key,
    userid    varchar(20)  not null,
    sentence  varchar(100) not null,
    errorword varchar(100) not null,
    errortype varchar(15)  not null,
    index idx_errors (userid, sentence)
);


