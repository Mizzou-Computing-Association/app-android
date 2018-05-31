package tigerhacks.android.tigerhacksapp;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v7.widget.LinearLayoutCompat;
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
 * This class only contains a static method that keeps the bottom navigation bar
 * from overlapping UI elements on the phone screen. It takes in the view that is
 * being overlapped, and simply trims off the view's height a value equal to the
 * height of the navigation bar.
 *
 * Note: In order to programmatically change a layout's dimensions, you have to use a
 * ViewTreeObserver's addOnGlobalLayoutListener function, which requires all
 * variables inside to be final. Hence the reason you can only pass in a final
 * view variable
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

                //perform appropriate setLayoutParam for different kind of LayoutParams.
                //I was incapable of detecting the specific LayoutParam programmatically
                //and so I simply add a new instanceof check for each new LayoutParam I
                //need to fix.

                //layout fix for Map page
                if(view.getLayoutParams() instanceof LinearLayout.LayoutParams)
                {
                    view.setLayoutParams(
                            new LinearLayout.LayoutParams(view.getLayoutParams().width, view.getHeight() - finalAct.findViewById(R.id.navigation).getHeight()));
                }
                //layout fix for Sponsors page
                else if(view.getLayoutParams() instanceof FrameLayout.LayoutParams)
                {
                    view.setLayoutParams(
                            new FrameLayout.LayoutParams(view.getLayoutParams().width, view.getHeight() - finalAct.findViewById(R.id.navigation).getHeight());
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
