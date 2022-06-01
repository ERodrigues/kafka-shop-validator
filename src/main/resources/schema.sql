create schema if not exists shop;

create table shop.Product(
    id bigint PRIMARY KEY auto_increment,
    product_identifier varchar not null,
    amount int not null
);