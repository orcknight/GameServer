package com.tian.server.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.tian.server.common.Living;
import com.tian.server.entity.*;

import java.util.*;

/**
 * Created by PPX on 2017/6/14.
 */
public class PlayerCache implements Living{

    //用户的socket client
    private SocketIOClient socketClient;

    //用户信息
    private UserEntity user;

    //玩家信息
    private PlayerEntity player;

    //玩家辅助信息
    private PlayerInfoEntity playerInfo;

    //玩家当前位置
    private RoomEntity room;

    //玩家当前观察的物品id
    private String lookId;

    private List<Living> enemy;

    private Map<String, PlayerSkillEntity> skills;

    Map<String, Integer> limb_damage;

    String[] danger_limbs = new String[] {"头部", "颈部", "胸口", "后心","小腹",};

    public PlayerCache(){

    }

    public void setSocketClient(SocketIOClient socketClient){

        this.socketClient = socketClient;
    }

    public SocketIOClient getSocketClient(){

        return this.socketClient;
    }

    public void setUser(UserEntity user){

        this.user = user;
    }

    public UserEntity getUser(){

        return this.user;
    }

    public void setPlayer(PlayerEntity player){

        this.player = player;
    }

    public PlayerEntity getPlayer(){

        return this.player;
    }

    public void setPlayerInfo(PlayerInfoEntity playerInfo){

        this.playerInfo = playerInfo;
    }

    public PlayerInfoEntity getPlayerInfo(){

        return this.playerInfo;
    }

    public void setRoom(RoomEntity room){

        this.room = room;
    }

    public RoomEntity getRoom(){

        return this.room;
    }

    public void setLookId(String lookId){

        this.lookId = lookId;
    }

    public String getLookId(){

        return this.lookId;
    }

    public List<Living> getEnemy(){

        if(this.enemy == null){

            this.enemy = new ArrayList<Living>();
        }
        return this.enemy;
    }

    public void initLimbDamage(){

        // 人类身体部位
        this.limb_damage.put("头部", 100);
        this.limb_damage.put("颈部", 90);
        this.limb_damage.put("胸口", 90);
        this.limb_damage.put("后心", 80);
        this.limb_damage.put("左肩", 50);
        this.limb_damage.put("右肩", 55);
        this.limb_damage.put("左臂", 40);
        this.limb_damage.put("右臂", 45);
        this.limb_damage.put("左手", 20);
        this.limb_damage.put("右手", 30);
        this.limb_damage.put("腰间", 60);
        this.limb_damage.put("小腹", 70);
        this.limb_damage.put("左腿", 40);
        this.limb_damage.put("右腿", 50);
        this.limb_damage.put("左脚", 35);
        this.limb_damage.put("右脚", 40);

        //动物身体部位
        this.limb_damage.put("身体", 80);
        this.limb_damage.put("前脚", 40);
        this.limb_damage.put("后脚", 50);
        this.limb_damage.put("腿部", 40);
        this.limb_damage.put("尾巴", 10);
        this.limb_damage.put("翅膀", 50);
        this.limb_damage.put("爪子", 40);
    }

    public void heartBeat() {

        if(enemy.size() > 0){



            //获取准备技能 prepare技能

            //没有技能把 attack_skill 设置为 unarmed
            String attack_skill = "unarmed";

            //随机选择攻击身体的部位

            //计算ap

            int level = 3;

            int power = level * level * level/100;

            power = power/30 *  20;

            power += 5000/1000;


            int ap = power;
            if(ap < 1){
                ap = 1;
            }

            int dp = 2 * 2 * 2 / 100;
            dp = dp / 30 * 15;
            dp += 5000/1000;
            if( dp < 1 ){
                dp = 1;
            }

            Random r = new Random();
            int randomAp = r.nextInt(ap + dp);

            if(randomAp >= dp){


            }





        }

    }
}
