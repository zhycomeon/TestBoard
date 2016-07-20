package com.datai.safesoftboard.interfaces;

import com.datai.safesoftboard.SoftKey;

public interface SoftKeyStatusListener {

    void onPressed(SoftKey softKey);

    void onDeleted();

    void onConfirm();

    void onJMEAdd();

    void onJMEMin();

    void onAllPosition();

    void onHalfPosition();

    void onOneThirdsPosition();

    void onTwoThirdsPosition();

    void onOneFourthsPosition();
}
