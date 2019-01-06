package com.xuh.shopping.tools;

public class CartInfo {

    public Integer id;
    public String info,imgName,num,price,counts;
    public CartInfo(Integer id,String imgName,String info,String num,String price,String counts){
        this.id = id;
        this.imgName = imgName;
        this.info = info;
        this. num = num;
        this.price = price;
        this.counts = counts;
    }
}