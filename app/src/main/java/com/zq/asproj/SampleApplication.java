package com.zq.asproj;


import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import dagger.hilt.android.HiltAndroidApp;

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.zq.asproj.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

}