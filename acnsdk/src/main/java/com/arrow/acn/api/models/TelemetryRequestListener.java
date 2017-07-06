/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.api.models;

/**
 * Created by osminin on 24.05.2017.
 */

public interface TelemetryRequestListener {
    void onTelemetrySuccess();

    void onTelemetryError(ApiError error);
}
