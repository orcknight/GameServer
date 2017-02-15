
#元旦礼包发放sql
SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '15155930835';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 1298, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '1298';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17863620085';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 10021, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '10021';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17853137032';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 39341, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '39341';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '15234829064';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 52926, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '52926';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '18263120347';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 947, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '947';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17864163616';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 512, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '512';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17865578637';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 1592, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '1592';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17853532583';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 21600, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '21600';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '13280996371';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 42048, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '42048';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '13642562144';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 1027, 600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 600 WHERE id = '1027';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17853522278';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 39510, 2600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 2600 WHERE id = '39510';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '15155196213';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 1231, 2600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 2600 WHERE id = '1231';

SELECT w.id, w.money FROM user u
LEFT JOIN wallet w ON w.userId = u.id 
WHERE u.phone = '17862706325';
INSERT INTO wallet_order (fromWalletId, toWalletId, money, type, orderType, businessId, orderId, status, remark) VALUE(0, 638, 8600, 1, 0, 0, -1, 2, '元旦礼包');
UPDATE wallet SET money = money + 8600 WHERE id = '638';

