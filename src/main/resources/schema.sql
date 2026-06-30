CREATE TABLE if not exists products(
    id BigInt Auto_Increment primary key,
    sku varchar(20) unique not null,
    name varchar(255) not null,
    category varchar(100),
    price decimal(10,2) not null,
    stock_quantity int not null
)
create table if not exists customers(
    id bigint auto_increment primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    phone varchar(20) not null
)
create table if not exists orders(
    id bigint auto_increment primary key,
    customer_id bigint not null,
    order_date timestamp default current_timestamp,
    total_amount decimal(10,2) not null,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
)
create table if not exists order_items(
    id bigint auto_increment primary key,
    order_id bigint not null,
    product_id bigint not null,
    quantity int not null,
    unit_price decimal(10,2) not null,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
)