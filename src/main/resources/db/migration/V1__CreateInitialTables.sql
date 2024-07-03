create table cart
(
    id          bigint not null auto_increment,
    total_price float(53),
    primary key (id)
);
create table cart_product
(
    cart_id    bigint not null,
    product_id bigint not null
);
create table order_item
(
    id          bigint not null auto_increment,
    created_at  date,
    total_price float(53),
    order_list  bigint,
    primary key (id)
);
create table order_product
(
    order_id   bigint not null,
    product_id bigint not null
);
create table product
(
    id                bigint    not null auto_increment,
    available         bit,
    brand             varchar(50),
    category          varchar(50),
    description       varchar(200),
    name              varchar(50),
    price             float(53) not null check (price >= 1),
    product_condition varchar(50),
    size              varchar(10),
    user_id           bigint,
    primary key (id)
);
create table user
(
    id       bigint not null auto_increment,
    email    varchar(255),
    name     varchar(50),
    password varchar(255),
    cart_id  bigint,
    primary key (id)
);
create table user_type
(
    id        bigint                  not null auto_increment,
    user_type enum ('BUYER','SELLER') not null,
    user_id   bigint,
    primary key (id)
);
alter table user
    drop index UK_47dq8urpj337d3o65l3fsjph3;
alter table user
    add constraint UK_47dq8urpj337d3o65l3fsjph3 unique (cart_id);
alter table cart_product
    add constraint FK2kdlr8hs2bwl14u8oop49vrxi foreign key (product_id) references product (id);
alter table cart_product
    add constraint FKlv5x4iresnv4xspvomrwd8ej9 foreign key (cart_id) references cart (id);
alter table order_item
    add constraint FKb2599o8ih8ea9gdjl4nji48wk foreign key (order_list) references user (id);
alter table order_product
    add constraint FKhnfgqyjx3i80qoymrssls3kno foreign key (product_id) references product (id);
alter table order_product
    add constraint FKj48e2olfc5a8x5gek2iv5eaqi foreign key (order_id) references order_item (id);
alter table product
    add constraint FK979liw4xk18ncpl87u4tygx2u foreign key (user_id) references user (id);
alter table user
    add constraint FKtqa69bib34k2c0jhe7afqsao6 foreign key (cart_id) references cart (id);
alter table user_type
    add constraint FKj0whdmtccunmsfxctomsgp1vn foreign key (user_id) references user (id);