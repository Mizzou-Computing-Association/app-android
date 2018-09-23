package tigerhacks.android.tigerhacksapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Conno on 9/8/2018.
 */



//PrizeCardView is an extension of the CardView that adds onClick functionality for the prize cards
public class PrizeCardView extends CardView {
    private int clickCount = 0;
    private TextView titleView, descView, criteriaView, prizesView, subtitle1View, subtitle2View;
    private LinearLayout linear1View, linear2View;
    private ImageView imageView;

    public enum Type {BEGINNER, MAIN};

    private Type type;

    public PrizeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate()
    {
        super.onFinishInflate();
        initializeChildren();
    }

    //this function finds the appropriate views using android:contentDescription tags
    private void initializeChildren()
    {
        ArrayList<View> outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "title", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        titleView = (TextView)outputViews.get(0);

        outputViews.clear();
        outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "description", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        descView = (TextView)outputViews.get(0);
        descView.setVisibility(View.GONE);

        outputViews.clear();
        outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "prizes", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        prizesView = (TextView)outputViews.get(0);
        prizesView.setVisibility(View.GONE);

        outputViews.clear();
        outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "subtitle2", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        subtitle2View = (TextView)outputViews.get(0);
        subtitle2View.setVisibility(View.GONE);

        outputViews.clear();
        outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "linear2", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        linear2View = (LinearLayout)outputViews.get(0);
        linear2View.setVisibility(View.GONE);

        outputViews.clear();
        outputViews = new ArrayList<>();
        this.findViewsWithText(outputViews, "image", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        imageView = (ImageView)outputViews.get(0);
    }

    public void onClickAction(final View rootView)
    {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(clickCount++ % 2 == 0) //if handles expansion of card; else handles collapse
                {
                    TransitionManager.beginDelayedTransition((ViewGroup) rootView);
                    prizesView.setVisibility(View.VISIBLE);
                    descView.setVisibility(View.VISIBLE);
                    subtitle2View.setVisibility(View.VISIBLE);
                    linear2View.setVisibility(View.VISIBLE);
                    //ei.expand();
                }
                else
                {
                    TransitionManager.beginDelayedTransition((ViewGroup) rootView);
                    prizesView.setVisibility(View.GONE);
                    descView.setVisibility(View.GONE);
                    subtitle2View.setVisibility(View.GONE);
                    linear2View.setVisibility(View.GONE);
                    //ci.collapse();
                }
            }
        });
    }

    //the following functions are setters for various parts of the prize card
    public void setTitle(String s)
    {
        titleView.setText(s);
    }
    public void setDescription(String s)
    {
        descView.setText(s);
    }
    /*public void setCriteria(String list[])
    {
        String newString = "";
        for(String criteria : list)
        {
            newString += "\u2022 " + criteria + "\n";
        }
        newString = newString.substring(0, newString.length()-1);
        criteriaView.setText(newString);
    }*/
    public void setPrizes(ArrayList<String> list)
    {
        String newString = "";
        for(String prize : list)
        {
            newString += "\u2022 " + prize + "\n";
        }
        newString = newString.substring(0, newString.length()-1);
        prizesView.setText(newString);
    }

    public void setImage(int id)
    {
        imageView.setImageDrawable(getResources().getDrawable(id));
    }
    public void setType(Type t){type = t;}
    public Type getType(){return type;}
}
