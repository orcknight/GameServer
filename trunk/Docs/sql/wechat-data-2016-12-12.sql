#É¾³ýÖ£Ñ«µÄÕËºÅ
DELETE FROM user WHERE phone = '15954041467';
DELETE FROM wallet WHERE userId = 581;
DELETE FROM wallet_order WHERE toWalletId = 925 or fromWalletId = 925;

DELETE FROM wt_user WHERE nickname = 'Ö£Ñ«';
DELETE FROM ac_chief_fans WHERE nickname = 'Ö£Ñ«';
DELETE FROM ac_chief WHERE tel = '15954041467';
