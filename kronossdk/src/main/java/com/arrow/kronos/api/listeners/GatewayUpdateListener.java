package com.arrow.kronos.api.listeners;

import com.arrow.kronos.api.models.GatewayResponse;

/**
 * Created by osminin on 10/6/2016.
 */

public interface GatewayUpdateListener {
    void onGatewayUpdated(GatewayResponse response);
    void onGatewayUpdateFailed();
}
