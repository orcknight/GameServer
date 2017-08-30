package com.tian.server.common;

/**
 * Created by PPX on 2017/8/11.
 */
public class Ansi {

    // Copyright (C) 2003-2004, by Lonely. All rights reserved.
// This software can not be used, copied, or modified
// in any form without the written permission from authors.
//
//        File        :  /include/ansi.h
//
//        The standard set of ANSI codes for mudlib use.
/*
155 6D m * SGR - Set Graphics Rendition (affects character attributes)
         *        [0m = Clear all special attributes
         *        [1m = Bold or increased intensity
         *        [2m = Dim or secondary color on GIGI  (superscript on XXXXXX)
                [3m = Italic                          (subscript on XXXXXX)
         *        [4m = Underscore, [0;4m = Clear, then set underline only
         *        [5m = Slow blink
                [6m = Fast blink                      (overscore on XXXXXX)
         *        [7m = Negative image, [0;1;7m = Bold + Inverse
                [8m = Concealed (do not display character echoed locally)
                [9m = Reserved for future standardization
         *        [10m = Select primary font (LA100)
         *        [11m - [19m = Selete alternate font (LA100 has 11 thru 14)
                [20m = FRAKTUR (whatever that means)
         *        [22m = Cancel bold or dim attribute only (VT220)
         *        [24m = Cancel underline attribute only (VT220)
         *        [25m = Cancel fast or slow blink attribute only (VT220)
         *        [27m = Cancel negative image attribute only (VT220)
         *        [30m = Write with black,   [40m = Set background to black (GIGI)
         *        [31m = Write with red,     [41m = Set background to red
         *        [32m = Write with green,   [42m = Set background to green
         *        [33m = Write with yellow,  [43m = Set background to yellow
         *        [34m = Write with blue,    [44m = Set background to blue
         *        [35m = Write with magenta, [45m = Set background to magenta
         *        [36m = Write with cyan,    [46m = Set background to cyan
         *        [37m = Write with white,   [47m = Set background to white

Minimum requirements for VT100 emulation:

  [A       Sent by the up-cursor key (alternately ESC O A)
  [B       Sent by the down-cursor key (alternately ESC O B)
  [C       Sent by the right-cursor key (alternately ESC O C)
  [D       Sent by the left-cursor key (alternately ESC O D)
  OP       PF1 key sends ESC O P
  OQ       PF2 key sends ESC O Q
  OR       PF3 key sends ESC O R
  OS       PF3 key sends ESC O S
  [c       Request for the terminal to identify itself
  [?1;0c   VT100 with memory for 24 by 80, inverse video character attribute
  [?1;2c   VT100 capable of 132 column mode, with bold+blink+underline+inverse

  [0J     Erase from current position to bottom of screen inclusive
  [1J     Erase from top of screen to current position inclusive
  [2J     Erase entire screen (without moving the cursor)
  [0K     Erase from current position to end of line inclusive
  [1K     Erase from beginning of line to current position inclusive
  [2K     Erase entire line (without moving cursor)
  [12;24r   Set scrolling region to lines 12 thru 24.  If a linefeed or an
            INDex is received while on line 24, the former line 12 is deleted
            and rows 13-24 move up.  If a RI (reverse Index) is received while
            on line 12, a blank line is inserted there as rows 12-13 move down.
            All VT100 compatible terminals (except GIGI) have this feature.
*/

    public static final String ESC = "\u001B";
    public static final String CSI = ESC + "[";
    public static final String BEL = ESC + "[s";

     /*  Foreground Colors  */

    public static final String BLK = ESC + "[30m"; /* Black    */
    public static final String RED = ESC + "[31m"; /* Red      */
    public static final String GRN = ESC + "[32m"; /* Green    */
    public static final String YEL = ESC + "[33m"; /* Yellow   */
    public static final String BLU = ESC + "[34m"; /* Blue     */
    public static final String MAG = ESC + "[35m"; /* Magenta  */
    public static final String CYN = ESC + "[36m"; /* Cyan     */
    public static final String WHT = ESC + "[37m"; /* White    */

    /*   Hi Intensity Foreground Colors   */
    public static final String HIK = ESC + "[1;30m"; /* Black    */
    public static final String HIR = ESC + "[1;31m"; /* Red      */
    public static final String HIG = ESC + "[1;32m"; /* Green    */
    public static final String HIY = ESC + "[1;33m"; /* Yellow   */
    public static final String HIB = ESC + "[1;34m"; /* Blue     */
    public static final String HIM = ESC + "[1;35m"; /* Magenta  */
    public static final String HIC = ESC + "[1;36m"; /* Cyan     */
    public static final String HIW = ESC + "[1;37m"; /* White    */

    /* High Intensity Background Colors  */
    public static final String HBBLK = ESC + "[40;1m"; /* 淡黑     */
    public static final String HBRED = ESC + "[41;1m"; /* Red      */
    public static final String HBGRN = ESC + "[42;1m"; /* Green    */
    public static final String HBYEL = ESC + "[43;1m"; /* Yellow   */
    public static final String HBBLU = ESC + "[44;1m"; /* Blue     */
    public static final String HBMAG = ESC + "[45;1m"; /* Magenta  */
    public static final String HBCYN = ESC + "[46;1m"; /* Cyan     */
    public static final String HBWHT = ESC + "[47;1m"; /* White    */


    public static final String REDGRN = ESC + "[1;31;42m";        /* Magenta  */
    public static final String REDYEL = ESC + "[1;31;43m";
    public static final String REDBLU = ESC + "[1;31;44m";
    public static final String REDMAG = ESC + "[1;31;45m";
    public static final String REDCYN = ESC + "[1;31;46m";
    public static final String REDWHI = ESC + "[1;31;47m";
    public static final String GRNRED = ESC + "[1;32;41m";
    public static final String GRNYEL = ESC + "[1;32;43m";
    public static final String GRNBLU = ESC + "[1;32;44m";
    public static final String GRNMAG = ESC + "[1;32;45m";
    public static final String GRNCYN = ESC + "[1;32;46m";
    public static final String GRNWHI = ESC + "[1;32;47m";
    public static final String YELRED = ESC + "[1;33;41m";
    public static final String YELGRN = ESC + "[1;33;42m";
    public static final String YELBLU = ESC + "[1;33;43m";
    public static final String YELMAG = ESC + "[1;33;45m";
    public static final String YELCYN = ESC + "[1;33;46m";
    public static final String YELWHI = ESC + "[1;33;47m";
    public static final String BLURED = ESC + "[1;34;41m";
    public static final String BLUGRN = ESC + "[1;34;42m";
    public static final String BLUYEL = ESC + "[1;34;43m";
    public static final String BLUMAG = ESC + "[1;34;45m";
    public static final String BLUCYN = ESC + "[1;34;46m";
    public static final String BLUWHI = ESC + "[1;34;47m";
    public static final String MAGRED = ESC + "[1;35;41m";
    public static final String MAGGRN = ESC + "[1;35;42m";
    public static final String MAGYEL = ESC + "[1;35;43m";
    public static final String MAGBLU = ESC + "[1;35;44m";
    public static final String MAGCYN = ESC + "[1;35;46m";
    public static final String MAGWHI = ESC + "[1;35;47m";
    public static final String CYNRED = ESC + "[1;36;41m";
    public static final String CYNGRN = ESC + "[1;36;42m";
    public static final String CYNYEL = ESC + "[1;36;43m";
    public static final String CYNBLU = ESC + "[1;36;44m";
    public static final String CYNMAG = ESC + "[1;36;45m";
    public static final String CYNWHI = ESC + "[1;36;47m";
    public static final String WHIRED = ESC + "[1;37;41m";
    public static final String WHIGRN = ESC + "[1;37;42m";
    public static final String WHIYEL = ESC + "[1;37;43m";
    public static final String WHIBLU = ESC + "[1;37;44m";
    public static final String WHIMAG = ESC + "[1;37;45m";
    public static final String WHICYN = ESC + "[1;37;46m";

    /*  Background Colors  */

    public static final String BBLK = ESC + "[40m";          /* Black    */
    public static final String BRED = ESC + "[41m";         /* Red      */
    public static final String BGRN = ESC + "[42m";          /* Green    */
    public static final String BYEL = ESC + "[43m";          /* Yellow   */
    public static final String BBLU = ESC + "[44m";          /* Blue     */
    public static final String BMAG = ESC + "[45m";          /* Magenta  */
    public static final String BCYN = ESC + "[46m";          /* Cyan     */
    public static final String BWHT = ESC + "[47m";          /* White    */

    public static final String NOR = ESC + "[2;37;0m";      /* Puts everything back to normal */

    /* Additional ansi Esc codes added to ansi.h by Gothic  april 23,1993 */
    /* Note, these are Esc codes for VT100 terminals, and emmulators */
    /*       and they may not all work within the mud               */

    public static final String BOLD = ESC + "[1m";          /* Turn on bold mode */
    public static final String CLR = ESC + "[2J";           /* Clear the screen  */
    public static final String HOME = ESC + "[H";            /* Send cursor to home position */
    public static final String REF = CLR + HOME;            /* Clear screen and home cursor */
    public static final String BIGTOP = ESC + "#3";            /* Dbl height characters, top half */
    public static final String BIGBOT = ESC + "#4";            /* Dbl height characters, bottem half */
    public static final String SAVEC = ESC + "[s";            /* Save cursor position */
    public static final String REST = ESC + "[u";            /* Restore cursor to saved position */
    public static final String REVINDEX = ESC + "M";            /* Scroll screen in opposite direction */
    public static final String SINGW = ESC + "#5";            /* Normal, single-width characters */
    public static final String DBL = ESC + "#6";            /* Creates double-width characters */
    public static final String FRTOP = ESC + "[2;25r";        /* Freeze top line */
    public static final String FRBOT = ESC + "[1;24r";        /* Freeze bottom line */
    public static final String UNFR = ESC + "[r";            /* Unfreeze top and bottom lines */
    public static final String BLINK = ESC + "[5m";           /* Initialize blink mode */
    public static final String U = ESC + "[4m";           /* Initialize underscore mode */
    public static final String ITALIC = ESC + "[3m";           /* 斜体 */
    public static final String REV = ESC + "[7m";           /* Turns reverse video mode on */
    public static final String HIREV = ESC + "[1;7m";         /* Hi intensity reverse video  */

    public static final String FLASH = ESC + "[5m";           // flash


        /*关于行控制 */
        /*1.移动 */
    // #define TOTOP(x)        (ESC+"["+x+"A")         /*向上跳转x行*/
    //  #define TOBOT(x)        (ESC+"["+x+"B")         /*向下跳转x行*/
                                                /*当 游标已经在萤幕的最下一列时, 此一命令没有作用*/
    // #define TORIGHT(x)      (ESC+"["+x+"C")         /*向右移动x行*/
                                                /*当游标已经在萤幕的最右一栏时, 此一命令没有作用。*/
    //  #define TOLEFT(x)       (ESC+"["+x+"D")         /*向左移动x行*/
                                                /*当游标已经在萤幕的最左一栏时, 此一命令没有作用。*/
    //  #define TOPOINT(x,y)    (ESC+"["+x+";"+y+"f")   /*移动到点坐标为(x,y) x:行，y:列*/
    //  #define TOPOINTA(x,y)   (ESC+"["+x+";"+y+"H")
                /*2.清除屏幕*/
    // #define CLR_LINE        ESC+"[K"                /*清除到行尾*/
    //  #define SETDISPLAY(x,y) (ESC+"["+x+";"+y+"f")
// #define DELLINE      ESC+"[K"
                /*3.冻结屏幕指定行*/
/* 冻结屏幕指定行，x 是行号，从上往下数，分辨率为 800 x 600，y = 35，
 * 分辨率为 1024 x 768，y = 40。  */
    // #define FRELINE(x,y)    (ESC+"["+x+";"+y+"r")

    // #define CUP(n)                sprintf(ESC+"[%dA",n)        /* Cursor Up n lines */
    // #define CDOWN(n)        sprintf(ESC+"[%dB",n)        /* Cursor Down n lines */
    // #define CFW(n)                sprinff(ESC+"[%dC",n)        /* Cursor Forward n characters */
    // #define CBK(n)                sprintf(ESC+"[%dD",n)        /* Cursor Backward n characters */
    //  #define DELLINE                sprintf(ESC+"[K\n"+CUP(1))
                                                /* Erase to end of line and move Cursor to home of the line */
    // #define CMOVE(y,x)        sprintf(ESC+"[%d;%dH",y,x)
                                                /* Move Cursor to (y,x) position */
    // #define BEEP ""


    //SGR(x)
}
