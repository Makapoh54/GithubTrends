package com.test.anton.githubtrends.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.test.anton.githubtrends.CustomApplication;

/**
 * Network availability handling.
 */
public class NetworkManager {
    private static NetworkManager sOurInstance = new NetworkManager();
    private NetworkStatus mStatus;

    public static NetworkManager getInstance() {
        return sOurInstance;
    }

    /**
     * Returns current network status.
     *
     * @param context
     * @return
     */
    public synchronized NetworkStatus getNetworkStatus(Context context) {
        mStatus = NetworkStatus.NOT_CONNECTED;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null) {
                if (!activeNetwork.isAvailable() || !activeNetwork.isConnected()) {
                    return mStatus;
                }
                switch (activeNetwork.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        mStatus = NetworkStatus.WIFI;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        mStatus = NetworkStatus.MOBILE;
                        break;
                }
            }

        }

        return mStatus;
    }

    /**
     * Is the app currently connected to the network?
     *
     * @return
     */
    public synchronized boolean isConnected() {
        return getNetworkStatus(CustomApplication.getInstance().getAppContext()) != NetworkStatus.NOT_CONNECTED;
    }

    public enum NetworkStatus {
        NOT_CONNECTED, WIFI, MOBILE, UNKNOWN
    }

}