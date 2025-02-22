/*
 * Copyright (C) 2020 The Android Open Source Project
 * Copyright (C) 2023 Paranoid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.biometrics.sensors.face.sense;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;

import com.android.server.biometrics.BiometricsProto;
import com.android.server.biometrics.log.BiometricContext;
import com.android.server.biometrics.log.BiometricLogger;
import com.android.server.biometrics.sensors.ClientMonitorCallback;
import com.android.server.biometrics.sensors.ClientMonitorCallbackConverter;
import com.android.server.biometrics.sensors.HalClientMonitor;

import java.util.function.Supplier;

import vendor.aospa.biometrics.face.ISenseService;

public class FaceGetFeatureClient extends HalClientMonitor<ISenseService> {

    private static final String TAG = "FaceGetFeatureClient";

    private final int mFeature;
    private final int mFaceId;
    private boolean mValue;

    FaceGetFeatureClient(@NonNull Context context, @NonNull Supplier<ISenseService> lazyDaemon,
            @NonNull IBinder token, @Nullable ClientMonitorCallbackConverter listener, int userId,
            @NonNull String owner, int sensorId,
            @NonNull BiometricLogger logger, @NonNull BiometricContext biometricContext,
            int feature, int faceId) {
        super(context, lazyDaemon, token, listener, userId, owner, 0 /* cookie */, sensorId,
                logger, biometricContext, false /* isMandatoryBiometrics */);
        mFeature = feature;
        mFaceId = faceId;
    }

    @Override
    public void unableToStart() {
        try {
            if (getListener() != null) {
                getListener().onFeatureGet(false /* success */, new int[0], new boolean[0]);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to send error", e);
        }
    }

    @Override
    public void start(@NonNull ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    @Override
    protected void startHalOperation() {
        try {
            final boolean result = getFreshDaemon().getFeature(mFeature, mFaceId);
            int[] features = new int[1];
            features[0] = mFeature;
            boolean[] featureState = {result};
            mValue = result;

            if (getListener() != null) {
                getListener().onFeatureGet(result, features, featureState);
            }
            mCallback.onClientFinished(this, true /* success */);
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to getFeature", e);
            mCallback.onClientFinished(this, false /* success */);
        }
    }

    boolean getValue() {
        return mValue;
    }

    @Override
    public int getProtoEnum() {
        return BiometricsProto.CM_GET_FEATURE;
    }
}
