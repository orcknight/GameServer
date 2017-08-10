package com.tian.server.util;

import com.tian.server.entity.PlayerEntity;
import com.tian.server.entity.RoomGateEntity;
import com.tian.server.model.Living;
import com.tian.server.model.Player;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;

import java.util.List;
import java.util.Map;

/**
 * Created by PPX on 2017/8/5.
 */
public class ZjMudUtil {

    public static final String ZJ_KEY = "123456789abcd";
    public static final String ZJ_PAY_PORT = "3001";

    //下列定义如果跟现有定义冲突玩家可自行更改定义名称
    // '\n'换行符容易造成客户端信息刷新延迟，因此定义下面几个分隔符，提高客户端信息刷新效率
    public static final String ESA = "\u001B";
    public static final String ZJ_SEP = "$zj#"; //字串分隔符，比如向客户端发送场景中的多个npc信息，npc信息之间可用此分隔符连接
    public static final String ZJ_SP2 = "$z2#"; //菜单字串分隔符，一次向客户端发送多个菜单项，每项之间用此连接，菜单名字和关联命令之间用 '|' 连接
    public static final String ZJ_JBR = "$br#"; //长描述换行符

    //定义此标志后文字的热点命令，点击此标志后直到 NOR 之前的文字 会触发 w 字串内包含的命令，
    // w 字串格式："cmds:xxx"客户端会执行xxx命令，"http://xxxx","tel:xxx","mailto:xxx"会对应打开网页，电话，邮件程序
    //ZJOBLONG"这是一个巨大的石头(stone)，你可以试着"+ZJURL("cmds:move stone")+ZJSIZE(20)+"推动(move)"NOR"它。\n"
    public static String getZjUrl(String w){
        return ESA + "[u:" + w + "]";
    }

    public static String getZjSize(String n){
        return ESA + "[s:" + n + "]";
    } //设定标志之后直到NOR之前字符的文字大小为 (屏幕宽度/n)

    public static String getSySy(){
        return ESA + "000";
    }

    //弹出对话应答框，a为对话框标题，b为输入内容点击确定后执行的命令，如b="anwser",则确定后发送"answer 输入内容"
    //b字串内可使用$txt#通配串，如 b="answer $txt# for kill" 则确定后发送 "answer 输入内容 for kill"
    public static String getInputTxt(String a, String b){
        return ESA + "001" + a + ZJ_SEP + b;
    }

    //场景名称标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端场景标题标签上显示，支持彩色显示
    //ZJTITLE+ob->query("short")+"\n"....客户端收到ZJTITLE标志设置标题的同时会清空出口和左侧场景物件栏
    public static String getZjTitle(){
        return ESA + "002";
    }

    //场景出口标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端场景出口框中按方向分割处理并添加显示
    //ZJEXIT "出口名字1:dir1" ZJSEP "出口2:dir2" ZJSEP "出口3:dir3"。。。。+"\n"  出口名字支持彩色显示
    public static String getZJExit(){
        return ESA + "003";
    }

    //客户端收到此消息。会将对应出口按钮隐藏。
    public static String getZjExitRm(){
        return ESA + "903";
    }

    //客户端收到此消息。会将所有出口按钮清除。
    public static String getExitCl(){
        return ESA + "913";
    }

    //场景描述标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端场景描述框中显示。
    //ZJOBLONG+ob->query("long")+"\n"....基于对齐问题重定义了描述框控件，暂不支持文字点击定义和多彩显示，除场景描述之外的文本均支持文字点击协议
    public static String getZjLong(){
        return ESA + "004";
    }

    //场景左侧栏物件标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端场景左侧物件列表框中添加显示
    //ZJOBIN+"name1:cmds1"ZJSEP"name2:cmds2"......."\n"
    public static String getZjObIn(){
        return ESA + "005";
    }

    //客户端收到此消息。会将删除左侧栏对应物件。
    public static String getZjObOut(){
        return ESA + "905";
    }

    //推送自定义按钮定义，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端自定义按钮区处理，类似出口处理
    //ZJBTSET+"b1:查看状态:score"ZJSEP"b2:查看技能:skills"..."\n"
    //可以任意推送任何一个按钮不必每次全部推送，总共17个自定义按钮，分别b1-b17表示。(bxx:名称:命令)
    public static String getZjBtSet(){
        return ESA + "006";
    }


    //弹出描述标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端弹出框中显示，支持ZJBR换行
    //此标志打头的文字信息会弹出显示，设计中可以配合文字点击协议灵活运用
    //ZJOBLONG"这是一个巨大的石头(stone)，你可以试着"+ZJURL("cmds:move stone")+"推动(move)"NOR"它。\n"
    public static String getObjLong(){
        return ESA + "007";
    }

    //弹出窗口中的附加列表框，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端弹出框中的列表区中处理，作为可触摸命令按钮显示
    //次接口与文字点击接口功能类似，但是必须与ZJOBLONG结合，可以根据需要灵活调用此接口或者文字点击接口。
    public static String getObActs(){
        return ESA + "008";
    }

    public static String getObActs2(){
        return ESA + "009";
    }

    public static String getZjYesNo(){
        return ESA + "010";
    }

    //必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端地图框中显示，地图框支持ZJBR换行
    public static String getZjMapTxt(){
        return ESA + "011";
    }

    //顶部常用状态更新标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端状态显示框中处理显示
    //ZJHPTXT"名字:XXX:#颜色串║经验:XXX:#颜色串║气血:xxx/xxx/xxx:#颜色串║内力:xxx/xxx:颜色串。。。。"
    //各状态中间用“║”分开，每个子状态串中间为数据部分可以用"/"分开，单数据、2数据、3数据均可，客户端会自行解析
    //如果是单数据，客户端属性条会直接把数值显示出来，2、3数据，会自动解析状态条百分比
    //sp  = ZJHPTXT + my["name"];
    //sp += "║" + sprintf("EXP:%d",my["combat_exp"]);
    //sp += "║" + sprintf("气.%d:%d/%d/%d:%s",my["qi"],my["qi"],my["eff_qi"],my["max_qi"],"#BBFF0000");
    //sp += "║" + sprintf("内力.%d:%d/%d:%s",my["neili"],my["neili"],my["max_neili"],"#BB0000FF");
    //sp += "\n";
    //tell_object(me, sp);
    public static String getZjHpTxt(){
        return ESA + "012";
    }

    //配合start_more()使用，start_more()输出的内容将在客户端大文本阅读框中分页显示，支持ZJBR换行
    public static String getMoreTxt(){
        return ESA + "013";
    }

    //强制客户端发送某命令，必须在行首，之后跟随的直到 '\n' 之前的内容将被客户端作为命令发送
    //ZJFCMDS"quit\n" 客户端收到此消息自动发送quit命令
    public static String getZjForceCmd(String c){
        return ESA + "014" + c + "\n";
    }

    //既显既隐提示标志，必须在行首，之后跟随的直到 '\n' 之前的内容将在客户端临时消息框中渐显渐隐显示
    public static String getZjTmpSay(){
        return ESA + "015";
    }

    //客户端弹出菜单标志，之后跟随的直到 '\n' 之前的内容将在客户端处理并弹出菜单
    //ZJPOPMENU"菜单一|cmds1" ZJSP2 "菜单二|cmds2"...."\n"
    public static String getPopMenu(){
        return ESA + "020";
    }

    //r列数，w列宽(屏宽/w)，h行高(屏宽/h)，s字号(屏宽/s)
    public static String getZjMenuF(Integer r, Integer w, Integer h, Integer s){
        return "$"+r+","+w+","+h+","+s+"#";
    }

    //频道分屏协议
    //ZJCHANNEL打头的行会在聊天频道框显示，此协议最好在/feature/message.c中的receive_message函数中添加
    public static String getZjChannel(){
        return ESA + "100";
    }

    //客户端收到此消息会强行关闭客户端
    public static String getSysExit(){
        return ESA + "999";
    }

    //获取空行，用作开头用
    public static String getEmptyLine(){

        return "\r\n";
    }
    //绘制最下方的主菜单
    public static String getMainMenuLine() {

        return "\u001B0000007\r\n";
    }

    //创建角色字符串
    public static String getCreatePlayerLine(){

        return "\u001B0000008\r\n";
    }

    //获取登陆成功字符串
    public static String getLoginSuccessLine(){

        return "\u001B015登录成功，正在加载世界。。。\r\n";
    }

    //向主屏幕输出一行，也就是out窗口，主输出窗口
    public static String getScreenLine(String content){

        return content + "\r\n";
    }

    public static String getLocationLine(PlayerLocation pl){

        String contact = "$zj#";
        StringBuffer mapBuffer = new StringBuffer();
        mapBuffer.append("\u001B003");

        if(pl.getNorth() != null){

            mapBuffer .append( "north:" + pl.getNorth().getShortDesc() + contact);
        }
        if(pl.getSouth() != null){

            mapBuffer.append("south:" + pl.getSouth().getShortDesc() + contact);
        }
        if(pl.getEast() != null){

            mapBuffer.append("east:" + pl.getEast().getShortDesc() + contact);
        }
        if(pl.getWest() != null){

            mapBuffer.append("west:" + pl.getWest().getShortDesc() + contact);
        }
        if(pl.getOut() != null){

            mapBuffer.append("out:" + pl.getOut().getShortDesc() + contact);
        }

        int index = mapBuffer.toString().lastIndexOf(contact);
        if(index > 0){

            mapBuffer = mapBuffer.delete(index, index + contact.length());
            if(mapBuffer.length() < 5){

                mapBuffer.setLength(0);
            }else{

                mapBuffer.append("\r\n");
            }
        }

        String msg = "↵\r\n" + "\u001B002" + pl.getLocation().getShortDesc() + "\r\n" +
                "\u001B004" + pl.getLocation().getLongDesc() + "\r\n" + mapBuffer.toString() +
                getScreenLine("你来到了" + pl.getLocation().getShortDesc() + "。");

        return msg;
    }

    public static String getSendChatLine(String line){

        String contact = "$zj#";

        String msg = "\u001B001" + "当前频道【" + line + "】，请输入内容：" +
                contact + "chat $txt#\r\n" + "\u001B009选择频道:liaotian pindao" + contact +
                "表情动作:emote" + contact + "发送语音:liaotian yuyin\r\n";

        return msg;
    }

    public static String getChatLine(String channel, String userName, String playerName, String msg){

        String chatStr = "\u001B100【" + channel + "】\u001B[2;37;0m\u001B[1;36m\u001B[u:cmds:look " + userName +
                "]\u001B[s:22]" + playerName + "\u001B[2;37;0m\u001B[1;36m：" + msg + "\u001B[2;37;0m\r\n";
        return chatStr;
    }

    public static String getHuDongDescLine(String desc){

        String msg = "\u001B007" + desc + "\r\n";
        return msg;
    }

    public static String getHuDongButtonLine(String button){

        String msg = "\u001B009" + button + "\r\n";
        return msg;
    }

    public static String getPlayerHuDongButtonLine(String button){

        String msg = "\u001B009" + button + "\r\n";
        return msg;
    }

    public static String getPlayerStatLine(PlayerEntity player){

        String msg = "\u001B012$5,5,28,45#" + player.getName() + ":100/100:#000000║气血." + player.getQi() +
                ":" + player.getQi() + "/" + player.getEffQi() + "/" + player.getMaxQi() + ":#99FF0000:exert recover║内力." +
                player.getNeili() + ":" + player.getNeili() +  "/" + player.getEffNeili() + "/" + player.getMaxQi() + ":#990066FF║精神." +
                player.getJing() + ":" + player.getJing() +  "/" + player.getEffJing() + "/" + player.getMaxJing() +
                ":#996600CC:exert regenerate║精力.0:0/0/200:#99006600║怒气.0:0/7000:#99990000║" +
                "食物.200:200/200:#99FF6600║饮水.200:200/200:#990000FF║经验.0:0/1000:#99FF0066║潜能.100:100/2900/3000:#99FF00FF\r\n";

        return msg;
    }

    public static String getObjectsLine(RoomObjects roomObjects, Player my){

        StringBuffer sb = new StringBuffer();
        String contact = "$zj#";

        List<Living> npcs = roomObjects.getNpcs();
        if(npcs.size() > 0) {

            sb.append("\u001B005");
            int temp = 0;

            for(Living npc : npcs){

                if(temp > 0){

                    sb.append(contact);
                }
                sb.append(npc.getName());
                sb.append(":look /npc/npc#" + npc.getUuid());
                temp++;
            }

            sb.append("\r\n");
        }

        List<Player> players = roomObjects.getPlayers();
        if(players.size() > 1){

            sb.append("\u001B005");
            int temp = 0;

            for(Player player : players){

                if(player.getUuid() == my.getUuid()){

                    continue;
                }

                if(temp > 0){

                    sb.append(contact);
                }
                sb.append(player.getName());
                sb.append(":look /user/user#" + player.getUuid());
                temp++;
            }

            sb.append("\r\n");
        }

        Map<String, RoomGateEntity> roomGates = roomObjects.getGates();
        if(roomGates.size() > 0){

            sb.append("\u001B005");
            int temp = 0;
            for (RoomGateEntity gate : roomGates.values()) {

                if(temp > 0){

                    sb.append(contact);
                }
                sb.append(gate.getName());
                sb.append(":look /gate/gate#" + gate.getName());
                temp++;
            }

            sb.append("\r\n");
        }

        return sb.toString();
    }

    public static String getEnterRoomLine(String name, String eqpt, Player player){

        String eqptDesc = eqpt.length() == 0 ? "一丝不挂的" : "身着" + eqpt;
        String msg = name + eqptDesc + "走了过来。" + "\r\n" +
                "\u001B005" + player.getName() + ":look /user/user#" + player.getUuid() + "\r\n";
        return msg;
    }

    public static String getLeaveRoomLine(String directionInfo, Player player){

        String msg = player.getName() + "往" + directionInfo + "离开。\r\n" +
                "\u001B905look /user/user#" + player.getUuid() + "\r\n";
        return msg;
    }

    public static String getLoginBoradcastLine(PlayerEntity player){

        String msg = player.getName() + "连线进入这个世界。\r\n" +
                "\u001B005" + player.getName() + ":look /user/user#" + player.getUuid() + "\r\n";
        return msg;
    }

    public static String getLogoutBoradcastLine(Player player){

        String msg = player.getName() + "离开了这个世界。\r\n" +
                "\u001B905look /user/user#" + player.getUuid() + "\r\n";
        return msg;
    }

}
