DROP TABLE IF EXISTS `url` CASCADE;
DROP TABLE IF EXISTS `user` CASCADE;

CREATE TABLE `user` (
    `uid` bigint(20) NOT NULL AUTO_INCREMENT,
    `email` varchar(50) NOT NULL,
    `name` varchar(50) NOT NULL,
    `picture` varchar(400) DEFAULT NULL,
    `provider_type` varchar(20) NOT NULL,
    `provider_uid` varchar(100) NOT NULL,
    `role` varchar(20) NOT NULL,
    `created_at` datetime(6),
    `updated_at` datetime(6),
    `created_by` bigint(20) DEFAULT NULL,
    `updated_by` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`uid`),
    UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `url` (
   `url_id` bigint(20) NOT NULL AUTO_INCREMENT,
   `click_cnt` int(11) NOT NULL,
   `created_at` datetime(6) NOT NULL,
   `origin_url` varchar(3000) DEFAULT NULL,
   `shorten_url` varchar(40) DEFAULT NULL,
   `state` varchar(20) DEFAULT NULL,
   `updated_at` datetime(6) NOT NULL,
   `created_by` bigint(20) DEFAULT NULL,
   `updated_by` bigint(20) DEFAULT NULL,
   PRIMARY KEY (`url_id`),
   KEY `fk_user_url_01` (`created_by`),
   KEY `fk_user_url_02` (`updated_by`),
   CONSTRAINT `fk_user_url_01` FOREIGN KEY (`created_by`) REFERENCES `user` (`uid`),
   CONSTRAINT `fk_user_url_02` FOREIGN KEY (`updated_by`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=100000000000000000 DEFAULT CHARSET=utf8mb4;