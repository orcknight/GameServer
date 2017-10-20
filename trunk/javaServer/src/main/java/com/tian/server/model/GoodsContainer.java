package com.tian.server.model;

import com.tian.server.entity.GoodsEntity;
import com.tian.server.entity.PlayerPackageEntity;
import com.tian.server.entity.RoomEntity;
import com.tian.server.util.LuaBridge;
import com.tian.server.util.UserCacheUtil;
import net.sf.json.JSONObject;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.*;

/**
 * Created by PPX on 2017/9/19.
 */
public class GoodsContainer extends MudObject {

    private GoodsEntity goodsEntity; //物品对应的
    private Integer count;
    private Integer belongsId; //所有人ID
    private PlayerPackageEntity belongsInfo; //归属信息
    private JSONObject attr = new JSONObject();
    private Map<String ,BodyPart> parts = new HashMap<String, BodyPart>();
    private Boolean cuttable = false;
    private List<Integer> types = new ArrayList<Integer>();
    private String defaultClone;

    public GoodsEntity getGoodsEntity() {
        return goodsEntity;
    }

    public void setGoodsEntity(GoodsEntity goodsEntity) {
        this.goodsEntity = goodsEntity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getBelongsId() {
        return belongsId;
    }

    public void setBelongsId(Integer belongsId) {
        this.belongsId = belongsId;
    }

    public PlayerPackageEntity getBelongsInfo() {
        return belongsInfo;
    }

    public void setBelongsInfo(PlayerPackageEntity belongsInfo) {
        this.belongsInfo = belongsInfo;
    }

    public JSONObject getAttr() {
        return attr;
    }

    public void setAttr(JSONObject attr) {
        this.attr = attr;
    }

    public Map<String, BodyPart> getParts() {
        return parts;
    }

    public void setParts(Map<String, BodyPart> parts) {
        this.parts = parts;
    }

    public Boolean getCuttable() {
        return cuttable;
    }

    public void setCuttable(Boolean cuttable) {
        this.cuttable = cuttable;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }

    public String getDefaultClone() {
        return defaultClone;
    }

    public void setDefaultClone(String defaultClone) {
        this.defaultClone = defaultClone;
    }

    public void createDecayTask(Integer seconds, Integer phase){
        Timer timer=new Timer();//实例化Timer类
        timer.schedule(new DecayTask(this, phase), seconds * 1000);//五百毫秒
    }

    @Override
    public RoomEntity getLocation() {

        if(this.location != null){
            return location;
        }
        if(this.belongsId != null && this.belongsId > 0){

            Living player = UserCacheUtil.getPlayers().get(this.belongsId);
            return player.getLocation();
        }

        if(this.belongsInfo != null && this.belongsInfo.getPlayerId() != null && this.belongsInfo.getPlayerId() > 0){
            Living player = UserCacheUtil.getPlayers().get(this.belongsInfo.getPlayerId());
            return player.getLocation();
        }

        return null;
    }

    class DecayTask extends TimerTask {
        private GoodsContainer goods;
        private Integer phase;

        public DecayTask(GoodsContainer goods, Integer phase) {
            this.goods = goods;
            this.phase = phase;
        }

        @Override
        public void run() {
            LuaBridge bridge = new LuaBridge();
            String luaPath = this.getClass().getResource(goods.getResource()).getPath();
            Globals globals = JsePlatform.standardGlobals();
            //加载脚本文件login.lua，并编译
            globals.loadfile(luaPath).call();
            String funName = goods.getCmdActions().get("decay");
            //获取带参函数create
            LuaValue createFun = globals.get(LuaValue.valueOf(goods.getCmdActions().get(funName)));
            //执行方法初始化数据
            LuaValue retValue = createFun.call(CoerceJavaToLua.coerce(bridge), LuaValue.valueOf(goods.getUuid().toString()),
                    LuaValue.valueOf(phase));
            return;
        }

    }

}
