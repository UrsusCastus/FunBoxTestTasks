package com.example.ecommerceapp;

public class SwitchButtonController {
    private static SwitchButtonController mSwitchButtonControllerInstance;
    private boolean mStoreFrontActivityRun = true;

    private SwitchButtonController() {
    }

    public static SwitchButtonController getSwitchButtonControllerInstance() {
        if (mSwitchButtonControllerInstance == null) {
            mSwitchButtonControllerInstance = new SwitchButtonController();
        }
        return mSwitchButtonControllerInstance;
    }

    public boolean getStoreFrontActivityRun() {
        return mStoreFrontActivityRun;
    }

    public void setStoreFrontActivityRun(boolean storeFrontActivityRun) {
        mStoreFrontActivityRun = storeFrontActivityRun;
    }
}
