package com.tian.server.model;

/**
 * Created by PPX on 2017/7/11.
 */
public class SkillAction {

    private String action;
    private String postAction;
    private String skillName;
    private String damageType;
    private String weapon;

    private Integer level;
    private Integer force;
    private Integer attack;
    private Integer parry;
    private Integer damage;
    private Integer dodge;


    public SkillAction(){

    }

    public SkillAction(String action, String damageType){

        if(action != null){

            this.action = action;
        }

        if(damageType != null){

            this.damageType = damageType;
        }
    }

    public String getAction(){
        return this.action;
    }

    public void setAction(String action){
        this.action = action;
    }

    public String getPostAction() {
        return postAction;
    }

    public void setPostAction(String postAction) {
        this.postAction = postAction;
    }

    public String getSkillName(){
        return this.skillName;
    }

    public void setSkillName(String skillName){
        this.skillName = skillName;
    }

    public String getDamageType(){
        return this.damageType;
    }

    public void setDamageType(String damageType){
        this.damageType = damageType;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public Integer getLevel(){
        return this.level;
    }

    public void setLevel(Integer level){
        this.level = level;
    }

    public Integer getForce(){
        return this.force;
    }

    public void setForce(Integer force){
        this.force = force;
    }

    public Integer getAttack(){
        return this.attack;
    }

    public void setAttack(Integer attack){
        this.attack = attack;
    }

    public Integer getParry(){
        return this.parry;
    }

    public void setParry(Integer parry){
        this.parry = parry;
    }

    public Integer getDamage(){
        return this.damage;
    }

    public void setDamage(Integer damage){
        this.damage = damage;
    }

    public Integer getDodge() {
        return dodge;
    }

    public void setDodge(Integer dodge) {
        this.dodge = dodge;
    }

}
