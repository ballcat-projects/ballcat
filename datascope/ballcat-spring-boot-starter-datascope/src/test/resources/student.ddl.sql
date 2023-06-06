DROP TABLE IF EXISTS `h2student`;
CREATE TABLE `h2student`
(
    `id`   int(10) unsigned NOT NULL AUTO_INCREMENT,
    `class_name` varchar(10) NOT NULL,
    `school_name` varchar(10) NOT NULL,
    `name` varchar(1024) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;