package tigerhacks.android.tigerhacksapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Conno on 5/11/2018.
 */

public final class NavBarFix {
    public static int fixNavBarOverlap(final View view) {
        Context context = view.getContext();
        Activity activity = null;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        if(activity == null)
        {
            return -1;
        }

        final Activity finalAct = activity;
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                //get LayoutParam class of view

                if(view.getLayoutParams() instanceof LinearLayout.LayoutParams)
                {
                    view.setLayoutParams(
                            new LinearLayout.LayoutParams(view.getLayoutParams().width, view.getHeight() - finalAct.findViewById(R.id.navigation).getHeight()));
                    Log.d("TEST1", view.getLayoutParams().toString());

                }
                else if(view.getLayoutParams() instanceof FrameLayout.LayoutParams)
                {
                    view.setLayoutParams(
                            new ScrollView.LayoutParams(view.getLayoutParams().width, view.getHeight() - finalAct.findViewById(R.id.navigation).getHeight()));
                    Log.d("TEST2", view.getLayoutParams().toString());
                }
                else
                {
                    Log.d("TEST3", view.getLayoutParams().toString());
                }

                ViewTreeObserver obs = view.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });
        return 1;
    }
}
