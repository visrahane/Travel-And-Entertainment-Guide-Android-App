package com.vis.entertainment.constants;

public enum ReviewEnum {

    GOOGLE_REVIEW("Google Reviews"),YELP_REVIEW("Yelp Reviews");
    private String name;

    public String getName() {
        return name;
    }

    public static ReviewEnum getEnum(String value){
        for(ReviewEnum re:ReviewEnum.values()){
            if(re.getName().equals(value))
                return re;
        }
        return null;
    }

    ReviewEnum(String s) {
        name=s;
    }
}
