/*
 * Copyright (C) 2024 The Android Open Source Project
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
package com.android.ravenwoodtest.bivalenttest;

import static org.junit.Assert.assertEquals;

import android.util.ArrayMap;
import android.util.Size;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

// Tests for calling simple Android APIs.
@RunWith(AndroidJUnit4.class)
public class RavenwoodAndroidApiTest {
    @Test
    public void testArrayMapSimple() {
        final Map<String, String> map = new ArrayMap<>();

        map.put("key1", "value1");
        assertEquals("value1", map.get("key1"));
    }

    @Test
    public void testSizeSimple() {
        final var size = new Size(1, 2);

        assertEquals(2, size.getHeight());
    }
}
