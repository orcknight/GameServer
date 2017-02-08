/*
*此文件为前端用到的状态字段作统一的定义
*/
var statusEnum = {

	//交易类型
	TYPE_DECREASE : 0, //减少
	TYPE_INCREASE : 1, //增加
	//排序类型
	SORT_NORMAL: 0, //原始创建时间倒序
	SORT_PRICE : 1, //单价排序  目前单价最高的在前
	SORT_AUDIT : 2, //审核时间  目前审核最快的在前
	
	//线上兼职次数限制
	TYPE_UNLIMITED : 0, //不限次数
	TYPE_ONCE      : 1, //每人一次
	TYPE_DAILY     : 2, //每天一次
	
	//申请的用户端状态
	APPLY_USTATUS_APPLIED    : 1, //进行中
	APPLY_USTATUS_FINISHED   : 2, //已完成
	APPLY_USTATUS_UNFINISHED : 3, //未完成
	APPLY_USTATUS_PASSED     : 4, //审核通过
	APPLY_UNSTATUS_UNPASSED  : 5, //审核未通过
	
	//申请的企业端状态
	APPLY_CSTATUS_APPLIED    : 1, //进行中
	APPLY_CSTATUS_FINISHED   : 2, //待审核
	APPLY_CSTATUS_UNFINISHED : 3, //未完成
	APPLY_CSTATUS_PASSED     : 4, //审核通过
	APPLY_CNSTATUS_UNPASSED  : 5, //审核未通过
};

var handler = function (enumer) {
	for (key in enumer) {
		if (enumer.hasOwnProperty(key)) {
			template.helper(key, (function (key) {
				return function () {
					return String(enumer[key]);
				};
			})(key));
		}
	}
};

handler(statusEnum);