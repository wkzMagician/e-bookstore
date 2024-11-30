DROP DATABASE IF EXISTS bookstore;
CREATE DATABASE bookstore;
USE bookstore;

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cover` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `price` INTEGER DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `ISBN` char(13) DEFAULT NULL,
  `inventory` int DEFAULT 0,
  PRIMARY KEY (`id`)
    CREATE INDEX `idx_book_title_author_category` ON `book` (`title` ASC, `author` ASC, `category` ASC);
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(45) UNIQUE NOT NULL,
    `email` varchar(45) UNIQUE NOT NULL,
    `role` varchar(10) NOT NULL,
    `first_name` varchar(45) DEFAULT NULL,
    `last_name` varchar(45) DEFAULT NULL,
    `phone` char(11) DEFAULT NULL,
    `signature` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth` (
    `id` int NOT NULL AUTO_INCREMENT,
    `password` varchar(45) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_auth_userinfo_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date` datetime DEFAULT NULL,
  `total_price` int DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `book_amount` int unsigned NOT NULL,
  `book_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_item_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_order_item_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `book_amount` int unsigned NOT NULL,
    PRIMARY KEY (`id`),
  CONSTRAINT `fk_cart_book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE KEY `unique_user_book` (`user_id`, `book_id`)
);
