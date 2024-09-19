package org.ligi.survivalmanual.runner;

import android.os.Bundle;
import androidx.test.runner.AndroidJUnitRunner;
import com.linkedin.android.testbutler.TestButler;

public class TestButlerJunitRunner extends AndroidJUnitRunner {
    @Override
    public void onStart() {
        TestButler.setup(getTargetContext());
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        TestButler.teardown(getTargetContext());
        super.finish(resultCode, results);
    }
}
