package ru.dienet.wolfy.tv.androidstb.model;

import android.content.Context;
import ru.dienet.wolfy.tv.R;
import ru.dienet.wolfy.tv.appcore.corea.LoggerUtil;
import ru.dienet.wolfy.tv.appcore.model.AppContext;

public class StbAppContext extends AppContext {

    public void initializeLogger(Context context) {
        // Check if logging (e.g., Sentry) is enabled in the resources
        if (context.getResources().getBoolean(R.bool.sentryEnabled)) {
            String sentryDsn = "http://78de4f18d0434337aae7972a1f27e349@sentry.mpls.im/4";  // This can be made configurable
            String buildRevision = context.getString(R.string.build_revision);

            // Initialize LoggerUtil with context, DSN, and build revision
            LoggerUtil.initialize(context, sentryDsn, buildRevision);
        }

        // Enable or disable debug logging based on a condition (or always disable if needed)
        LoggerUtil.setDebugEnabled(false);
    }

    @Override
    public void a() {
        initializeLogger(getApplicationContext());
    }
}
