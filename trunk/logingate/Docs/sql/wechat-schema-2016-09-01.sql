# 为企业用户增加角色，company_role待建。
ALTER TABLE `luobojianzhi`.`company`   
  ADD COLUMN `roleId` INT(11) DEFAULT 0 NULL COMMENT '0:普通公司 1:代理 2:城市经理';

#威海-17863129509-崔杰-萝卜兼职（威海）运营中心
#烟台-18363873203-王玉玺-萝卜兼职（烟台）运营中心
#青岛-13969626874-李宾-萝卜兼职（青岛）运营中心
#济南-13573109082-王策-萝卜兼职（济南）运营中心
UPDATE company SET roleId=2 WHERE phone IN ("17863129509","18363873203","13969626874","13573109082");  