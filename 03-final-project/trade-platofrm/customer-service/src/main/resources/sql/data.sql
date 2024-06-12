DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS portfolio_item;

CREATE TABLE customer (
    id int AUTO_INCREMENT primary key,
    name VARCHAR(50),
    balance int
);

CREATE TABLE portfolio_item (
    id int AUTO_INCREMENT primary key,
    customer_id int,
    ticker VARCHAR(10),
    quantity int,
    foreign key (customer_id) references customer(id)
);

insert into customer(name, balance)
    values
        ('Sam', 10000),
        ('Mike', 10000),
        ('John', 10000);