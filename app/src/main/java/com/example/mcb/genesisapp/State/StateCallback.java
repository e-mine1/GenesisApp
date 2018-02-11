package com.example.mcb.genesisapp.State;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


import Repository.IRepository;

/**
 * Created by mcb on 17.08.15.
 */
public interface StateCallback {

    // layers in app
    int EXPLORE_LAYER = 0;
    int BASE_LIST = 1;
    int WORK_LAYER = 2;
    int CONNECT_LAYER = 3;
    int SEARCH_LAYER = 4;
    int DETAIL_LAYER = 5;
    int OPTIONS_LAYER = 6;
    int BUSINESSCARD_LAYER =7;
    int PICTURE_LAYER =8;
    int MODIFY_LAYER = 9;
    int TINDER_LAYER=10;


    int PICTURE_FROM_GALLERY=20;
    int SCAN_QRCODE =21;



    /**
     * State Listener
     */

    public interface StateListener{

        /**
         * called when the backpressed Button is clicked
         * @return true if backpressed was handled by fragment, false if activity should care
         * about it
         */
        boolean onBackPressed();

        /**
         * called when type_of_object (takes values in StateListener defined Strings) was added,
         * removed resp. modified in database.
         * if null values are passed, then it means that more than one subtye/ type was modified
         */
        void dataBaseModified(String type_of_object, String subTypeObject);

    }



    void updateViews();

    /**
     * change the State - call a new layer
     * @param layer the new layer - takes values in as stated in this interface
     */
    void changeState(int layer);



    /**
     * getter Function
     * @return the Datasource
     */
    IRepository getRepository();

    void goToPreviousLayer();

    public void registerStateListener(StateActivity.StateListener newListener);

    public void unRegisterStateListener(StateActivity.StateListener oldListener);

    void displayView(int position, boolean addToBackstack, Bundle... extra);

    void searchItemClicked();




     Context getContext();

     ContentResolver getContentResolver();

    void dataBaseModified();

    void closeKeyBoard();

    void openKeyBoard();

    void openKeyBoard(View view);

    void openWebLinkInBrowser(String url);

    void call(String number);

    void text(String number);

    void email(String email);

    WindowManager getWindowManager();



}