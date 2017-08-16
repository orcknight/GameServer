package com.tian.server.common;

/**
 * Created by PPX on 2017/8/15.
 */
public enum WeaponType {

    AXE(1),      // 斧
    BLADE(2),    // 刀
    CLUB(3),     // 棍
    DAGGER(4),   // 矛
    FORK(5),     // 叉
    HAMMER(6),   // 锤
    HOOK(7),     // 钩
    MACE(8),     // 鞭
    RAKE(9),     // 耙
    PIN(10),     // 针
    SWORD(11),   // 剑
    STICK(12),   // 棒
    STAFF(13),   // 杖
    SPEAR(14),   // 枪
    THROWING(15),// 暗器
    WHIP(16),    // 软鞭
    M_WEAPON(17),// 制造兵器
    XSWORD(18);  // 萧

    //私有变量，用来存储分配的值
    private int nCode;

    //构造函数传参
    private WeaponType(int nCode) {

        this.nCode = nCode;
    }

    @Override
    public String toString() {

        return String.valueOf(this.nCode);
    }

    public int toInteger() {

        return this.nCode;
    }
}
