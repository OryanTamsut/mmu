package com.mbj.driver;

import com.mbj.view.CacheUnitView;
import com.mbj.client.CacheUnitClientObserver;

public class CacheUnitClientDriver {

    public static void main(String[] args) {
        CacheUnitClientObserver cacheUnitClientObserver = new CacheUnitClientObserver();
        CacheUnitView view = new CacheUnitView();
        view.addPropertyChangeListener(cacheUnitClientObserver);
        view.start();
    }
}
