package personal.dhaval.kavita.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by user on 19-01-2018.
 */

public class FirebaseEventManager {
    public static final String EVENT_APP_LANCH = "App Launch";
    public static final String EVENT_NEW_KAVITA = "New Kavita";
    public static final String EVENT_SHARE_EMAIL = "Share Kavita : Email";
    public static final String EVENT_SHARE_WHATSAPP = "Share Kavita : Whatsapp";
    public static final String EVENT_DELETE_KAVITA = "Delete Kavita";

    public static void firebaseAnalyticsEvent(Context context, String event_name, String content_details) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event_name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT, content_details);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.d("*** Firebase Event", event_name + " - " + content_details);
    }
}
