use rtou;

create table if not exists users
(
    userid varchar(20) primary key,
    sso    varchar(10) not null
);

create table if not exists attentions
(
    id     bigint auto_increment primary key,
    userid varchar(20) not null,
    date   date        default (curdate()),
    index idx_attention (userid),
    foreign key (userid) references users (userid) on update cascade on delete cascade
);

create table if not exists characterinfo
(
    name      varchar(15) primary key,
    voicename varchar(20) not null,
    pitch     double      not null,
    langcode  varchar(10) not null
);

create table if not exists conversationcharacter
(
    userid        varchar(20) not null,
    charactername varchar(15) not null,
    primary key (userid, charactername),
    foreign key (userid) references users (userid) on update cascade on delete cascade,
    foreign key (charactername) references characterinfo (name) on update cascade on delete cascade
);

create table if not exists conversations
(
    userid         varchar(20)  not null,
    conversationid varchar(100) not null,
    sentence       varchar(225) not null,
    date           datetime default current_timestamp(),
    primary key (userid, conversationid, sentence),
    index idx_conversations (userid, conversationid),
    index idx_conversation_sentence (conversationid, sentence),
    foreign key (userid) references users(userid) on update cascade on delete cascade
);

create table if not exists estimations
(
    estimationid  varchar(100),
    userid        varchar(20),
    charactername varchar(15),
    date          datetime default current_timestamp(),
    primary key (estimationid, userid),
    foreign key (estimationid) references conversations (conversationid) on update cascade on delete cascade,
    foreign key (charactername) references characterinfo (name) on update cascade on delete cascade,
    foreign key (userid) references users (userid) on update cascade on delete cascade
);



create table if not exists estimationscores
(
    id            bigint auto_increment primary key,
    userid        varchar(20)  not null,
    estimationid  varchar(20)  not null,
    sentence      varchar(225) not null,
    accuracy      double       not null,
    prosody       double       not null,
    pronunciation double       not null,
    fluency       double       not null,
    completeness  double       not null,
    index idx_scores (userid, estimationid, sentence),
    foreign key (userid) references users(userid) on update cascade on delete cascade,
    foreign key (estimationid, sentence) references conversations(conversationid, sentence) on update cascade on delete cascade
);

create table if not exists errorwords
(
    id           bigint auto_increment primary key,
    userid       varchar(20)  not null,
    estimationid varchar(100) not null,
    sentence     varchar(100) not null,
    errorword    varchar(100) not null,
    errortype    varchar(15)  not null,
    index idx_errors (estimationid, sentence),
    foreign key (userid, estimationid, sentence) references conversations(userid, conversationid, sentence) on update cascade on delete cascade
);

