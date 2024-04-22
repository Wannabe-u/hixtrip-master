#todo 你的建表语句,包含索引
-- 分库分表：
-- 分库键: user_id（买家ID或卖家ID），采用哈希取模的方式进行分库
-- 分表键: id（订单ID），采用范围分表的方式（或者以订单创建时间进行分表）
-- 1. 采用哈希分库，确保买家和卖家的订单均匀分布在不同数据库中。
-- 2. 采用范围分表，基于 id 进行分表。
-- 3. 使用路由表快速定位订单所在的库和表。
-- 买卖双方查询需求：
-- 1. 对于买家查询，可以通过 user_id 快速定位到对应的库和表，然后直接查询。
-- 2. 对于卖家查询，由于卖家可能需要查询所有买家的订单，可以通过扫描多个库或多个表来实现，允许秒级延迟。
-- 索引：
-- 1. 主键索引 id，确保唯一性。
-- 2. 辅助索引 user_id，查询订单。
-- 3. 辅助索引 create_time，用于时间范围的查询优化。

CREATE TABLE `order_db0`.`orders_00000001` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `user_id` BIGINT UNSIGNED NOT NULL,
   `sku_id`	VARCHAR(32),
   `amount` INT NOT NULL,
   `money` DECIMAL(10,2) NOT NULL,
   `pay_time` TIMESTAMP NOT NULL ,
   `pay_status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
   `del_flag` TINYINT UNSIGNED NOT NULL,
   `create_by`	VARCHAR(32),
   `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_by`	VARCHAR(32),
   `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `idx_user_id` (`user_id`),
   KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 以此类推
CREATE TABLE `order_db1`.`orders_00000001` (
   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
   `user_id` BIGINT UNSIGNED NOT NULL,
   `sku_id`	VARCHAR(32),
   `amount` INT NOT NULL,
   `money` DECIMAL(10,2) NOT NULL,
   `pay_time` TIMESTAMP NOT NULL ,
   `pay_status` TINYINT UNSIGNED NOT NULL DEFAULT 0,
   `del_flag` TINYINT UNSIGNED NOT NULL,
   `create_by`	VARCHAR(32),
   `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `update_by`	VARCHAR(32),
   `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `idx_user_id` (`user_id`),
   KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加用来定位数据库表位置的路由中间表（db_index = user_id % 4 ）
CREATE TABLE `order_router` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `db_index` TINYINT UNSIGNED NOT NULL,
    `table_index` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`),
    KEY `idx_db_table` (`db_index`, `table_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




