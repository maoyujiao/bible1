package com.iyuba.wordtest.sign;

import com.iyuba.module.mvp.MvpView;

public interface SignMVPView extends MvpView {

    void showSucess(String sucess);
    void showFail(String fail);


    void saveImageFinished(String imagePath);
}
