package com.vis.entertainment.constants;

public enum OrderEnum {
    DEFAULT_ORDER("Default Order"),HIGHEST_RATING("Highest Rating"),LOWEST_RATING("Lowest Rating"),MOST_RECENT("Most Recent"),
    LEAST_RECENT("Least Recent");
    private String name;

    public String getName() {
        return name;
    }

    public static OrderEnum getEnum(String value){
        for(OrderEnum oe:OrderEnum.values()){
            if(oe.getName().equals(value))
                return oe;
        }
        return null;
    }

    OrderEnum(String s) {
        name=s;
    }
}
