/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : db_ssm

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-10-21 13:41:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  `user_phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `user_email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `user_pwd` varchar(20) DEFAULT NULL COMMENT '用户密码',
  `pwdSalt` varchar(20) DEFAULT NULL,
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `modify_time` date DEFAULT NULL COMMENT '修改时间',
  `is_delete` smallint(6) DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '张三', '13323456785', '2372334@qq.com', '123456', '1', '2017-10-21', '2017-10-21', '0');
INSERT INTO `t_user` VALUES ('2', '李四', '13645637234', '71438914@163.com', '123456', '2', '2017-10-21', '2017-10-21', '0');
INSERT INTO `t_user` VALUES ('3', '王五', '18823451236', '3471974193@136.com', '123456', '3', '2017-10-21', '2017-10-21', '0');
