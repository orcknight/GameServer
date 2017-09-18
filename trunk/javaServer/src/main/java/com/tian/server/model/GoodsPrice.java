package com.tian.server.model;

/**
 * Created by PPX on 2017/9/18.
 */
public class GoodsPrice {

    private String priceType; //ticket 点券　money游戏币
    private Integer price;

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
