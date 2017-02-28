/*
 * Copyright 2016-2017 tiagohm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils;

import android.bluetooth.BluetoothClass;
import android.util.Log;

import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.R;

public class Device
{

    private final String mName;
    private final String mAddress;
    private final boolean isPaired;
    private final int mDeviceClass;

    public Device(String name, String address, boolean paired, int deviceClass)
    {
        this.mName = name;
        this.mAddress = address;
        this.isPaired = paired;
        this.mDeviceClass = deviceClass;
    }

    public String getName()
    {
        return mName;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public boolean isPaired()
    {
        return isPaired;
    }

    public int getDeviceClass()
    {
        return mDeviceClass;
    }

    public int getDeviceClassIcon()
    {
        Log.d("TAG", "Device.getDeviceClass() = " + getDeviceClass());

        final int deviceClass = getDeviceClass();
        final int deviceClassMasked = deviceClass & 0x1F00;

        if(deviceClass == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES)
        {
            return R.drawable.headphone;
        }
        else if(deviceClass == BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE)
        {
            return R.drawable.microphone;
        }
        else if(deviceClassMasked == BluetoothClass.Device.Major.COMPUTER)
        {
            return R.drawable.computer;
        }
        else if(deviceClassMasked == BluetoothClass.Device.Major.PHONE)
        {
            return R.drawable.cell_phone;
        }
        else if(deviceClassMasked == BluetoothClass.Device.Major.HEALTH)
        {
            return R.drawable.heart;
        }
        else
        {
            return R.drawable.bluetooth;
        }
    }

    @Override
    public int hashCode()
    {
        return mAddress != null ? mAddress.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof Device && getAddress() == ((Device)o).getAddress();
    }
}
