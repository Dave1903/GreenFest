package com.thedevlopershome.greenfest;


public class GreenFestData {

    String sno;
    String description;
    String eventrName;
    String imageUrl;
    String timeVenue;

 public GreenFestData(){

 }

    public GreenFestData(String sno,String eventrName,String description,String timeVenue,String imageUrl ){
               this.sno=sno;
               this.eventrName=eventrName;
               this.description=description;
               this.timeVenue=timeVenue;
               this.imageUrl=imageUrl;
    }

}


