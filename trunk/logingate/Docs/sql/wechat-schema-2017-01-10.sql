#把职位的intro字段长度扩展为4096字节
ALTER TABLE `job` MODIFY COLUMN intro VARCHAR(4096);