/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : netdisk

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2021-02-23 08:39:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_file`
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(50) DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `open` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_file
-- ----------------------------
INSERT INTO `t_file` VALUES ('4', 'Postman-win64-7.2.2-Setup.exe', 'D:\\upload\\guest\\Postman-win64-7.2.2-Setup.exe', '76077688', '2', '0', '2021-01-24 10:49:35');
INSERT INTO `t_file` VALUES ('2', '1e98620df3c930938340379db39fd603.jpeg', 'D:\\upload\\admin\\1e98620df3c930938340379db39fd603.jpeg', '308166', '1', '0', '2021-01-23 08:37:04');
INSERT INTO `t_file` VALUES ('3', '3da64f5096fd44956df02bf19cf6eed9.jpg', 'D:\\upload\\admin\\3da64f5096fd44956df02bf19cf6eed9.jpg', '395214', '1', '0', '2021-01-23 08:46:55');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', '123456', '15667778888', 'admin123@126.com', '1');
INSERT INTO `t_user` VALUES ('2', 'guest', '123456', '15678432256', 'axcca@126.com', '0');
