package com.tian.server.util;

import com.tian.server.entity.PlayerEntity;
import com.tian.server.model.PlayerLocation;
import com.tian.server.model.RoomObjects;

import java.util.List;

/**
 * Created by PPX on 2017/6/13.
 */
public class CmdUtil {

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

            mapBuffer .append( "north:" + pl.getNorth().getCname() + contact);
        }
        if(pl.getSouth() != null){

            mapBuffer.append("south:" + pl.getSouth().getCname() + contact);
        }
        if(pl.getEast() != null){

            mapBuffer.append("east:" + pl.getEast().getCname() + contact);
        }
        if(pl.getWest() != null){

            mapBuffer.append("west:" + pl.getWest().getCname() + contact);
        }
        if(pl.getOut() != null){

            mapBuffer.append("out:" + pl.getOut().getCname() + contact);
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

        String msg = "↵\r\n" + "\u001B002" + pl.getLocation().getCname() + "\r\n" +
                "\u001B004" + pl.getLocation().getDescribe() + "\r\n" + mapBuffer.toString() +
                getScreenLine("你来到了" + pl.getLocation().getCname() + "。");

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

    public static String getObjectsLine(RoomObjects roomObjects, PlayerEntity my){

        StringBuffer sb = new StringBuffer();
        String contact = "$zj#";

        List<PlayerEntity> players = roomObjects.getPlayers();
        if(players.size() > 1){

            sb.append("\u001B005");
            int temp = 0;

            for(PlayerEntity player : players){

                if(player.getId() == my.getId()){

                    continue;
                }

                if(temp > 0){

                    sb.append(contact);
                }
                sb.append(player.getName());
                sb.append(":look /user/user#" + player.getId());
                temp++;
            }

            sb.append("\r\n");
        }

        return sb.toString();
    }

    public static String getEnterRoomLine(String name, String eqpt, PlayerEntity player){

        String eqptDesc = eqpt.length() == 0 ? "一丝不挂的" : "身着" + eqpt;
        String msg = name + eqptDesc + "走了过来。" + "\r\n" +
                "\u001B005" + player.getName() + ":look /user/user#" + player.getId() + "\r\n";
        return msg;
    }

    public static String getLeaveRoomLine(String directionInfo, PlayerEntity player){

        String msg = player.getName() + "往" + directionInfo + "离开。\r\n" +
                "\u001B905look /user/user#" + player.getId() + "\r\n";
        return msg;
    }

    public static String getLoginBoradcastLine(PlayerEntity player){

        String msg = player.getName() + "连线进入这个世界。\r\n" +
                "\u001B005" + player.getName() + ":look /user/user#" + player.getId() + "\r\n";
        return msg;
    }



}
