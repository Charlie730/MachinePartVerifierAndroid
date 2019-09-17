package com.humminbird.machinepartverifierandroid.DataClasses;

import android.app.Activity;

public class PartReel {
    private String expectedNumber;
    private String returnedNumber;
    private String feederPositionNum;
    private boolean isVerified;
    private Runnable statusChangedAction;


    public PartReel(String expectedName, String feederPosition){
        setExpectedNumber(expectedName);
        setFeederPositionNum(feederPosition);
    }


    public String getExpectedNumber() {
        return expectedNumber;
    }
    public String getReturnedNumber() {
        return returnedNumber;
    }
    public String getFeederPositionNum() {
        return feederPositionNum;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public Runnable getStatusChangedAction() {
        return statusChangedAction;
    }

    public void setExpectedNumber(String expectedNumber) {
        this.expectedNumber = expectedNumber;
    }
    public void setReturnedNumber(String returnedNumber) {
        this.returnedNumber = returnedNumber;
    }
    public void setVerified(boolean verified) {
        isVerified = verified;
        if(statusChangedAction != null){
            statusChangedAction.run();
        }
    }
    public void setFeederPositionNum(String feederPositionNum) {
        this.feederPositionNum = feederPositionNum;
    }
    public void startVerification(Activity activity){

    }

    //Private methods
    private void setStatusChangedAction(Runnable statusChangedActionval) {
        statusChangedAction = statusChangedActionval;
    }

    public void onStatusChanged(Runnable action){
        setStatusChangedAction(action);
    }
}
