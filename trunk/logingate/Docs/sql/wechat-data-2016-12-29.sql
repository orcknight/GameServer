#修复钱包充值bug的sql，保持收支平衡
UPDATE wallet SET money = 100, displayMoney = 100 WHERE id = 109;
UPDATE wallet_order SET orderId = '', status = 1 WHERE id = 1013;

UPDATE wallet SET money = 0, displayMoney = 0 WHERE id = 264;
UPDATE wallet_order SET orderId = '-1' WHERE id = 987;

UPDATE wallet SET money = 107, displayMoney = 107 WHERE id = 610;
UPDATE wallet_order SET orderId = '' WHERE id = 988;

UPDATE wallet SET money = 7, displayMoney = 7 WHERE id = 610;
UPDATE wallet_order SET orderId = '' WHERE id = 989;