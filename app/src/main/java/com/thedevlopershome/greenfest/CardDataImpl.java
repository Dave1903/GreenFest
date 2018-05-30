package com.thedevlopershome.greenfest;

import com.ramotion.expandingcollection.ECCardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardDataImpl implements ECCardData<String> {

    private String cardTitle;
    private Integer mainBackgroundResource;
    private Integer headBackgroundResource;
    private List<String> listItems;

    public CardDataImpl(String cardTitle, Integer mainBackgroundResource, Integer headBackgroundResource, List<String> listItems) {
        this.mainBackgroundResource = mainBackgroundResource;
        this.headBackgroundResource = headBackgroundResource;
        this.listItems = listItems;
        this.cardTitle = cardTitle;
    }

    @Override
    public Integer getMainBackgroundResource() {
        return mainBackgroundResource;
    }

    @Override
    public Integer getHeadBackgroundResource() {
        return headBackgroundResource;
    }

    @Override
    public List<String> getListItems() {
        return listItems;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public static List<ECCardData> generateExampleData() {
        List<ECCardData> list = new ArrayList<>();
        list.add(new CardDataImpl("", R.drawable.eventsback, R.drawable.events, createItemsList("events")));
        list.add(new CardDataImpl("", R.drawable.registerback, R.drawable.register , createItemsList("REGISTER FOR EVENTS")));
        list.add(new CardDataImpl("", R.drawable.aboutusback, R.drawable.aboutus , createItemsList("ABOUT PUKAAR")));
        list.add(new CardDataImpl("", R.drawable.teamback, R.drawable.support, createItemsList("ABOUT TEAM")));
        list.add(new CardDataImpl("", R.drawable.feedbackback, R.drawable.feedback, createItemsList("send Feedback")));
        list.add(new CardDataImpl("", R.drawable.visitwebsiteback, R.drawable.visitwebsite, createItemsList("VISIT WEBSITE")));
        list.add(new CardDataImpl("", R.drawable.visitwebsiteback, R.drawable.visitwebsite, createItemsList("ABOUT TEAM")));

        return list;
    }

    private static List<String> createItemsList(String cardName) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(
                cardName + " - Item 1",
                cardName + " - Item 2",
                cardName + " - Item 3",
                cardName + " - Item 4",
                cardName + " - Item 5",
                cardName + " - Item 6",
                cardName + " - Item 7"
        ));
        return list;
    }

}