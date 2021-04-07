package com.arcias.melocate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.arcias.melocate.Adapter.SlidePageAdapter;
import com.arcias.melocate.Model.SlideItem;
import com.arcias.melocate.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingScreen extends AppCompatActivity implements View.OnClickListener {
private ViewPager screenPage;
private SlidePageAdapter slidePageAdapter;
private List<SlideItem> itemList;
private Button next,previous,skip,start;
private LinearLayout dotsLayout;
private TextView[]dots;
private int currentPosition;
private Animation animation;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);
        screenPage=findViewById(R.id.slide_page);
        next=findViewById(R.id.move_next);
        previous=findViewById(R.id.move_previous);
        skip=findViewById(R.id.skip_button);
        start=findViewById(R.id.start_button);
dotsLayout=findViewById(R.id.linear_dot);
        start.setOnClickListener(this);
        previous.setOnClickListener(this);
        skip.setOnClickListener(this);
        next.setOnClickListener(this);

        itemList=new ArrayList<>();
        itemList.add(new SlideItem("My Location ", R.string.slide_my_information, R.drawable.mylocation));
        itemList.add(new SlideItem("Find Location", R.string.slide_find_location, R.drawable.find_location));
        itemList.add(new SlideItem("Personal Information", R.string.slide_my_information, R.drawable.personal));
        itemList.add(new SlideItem("Quick and Easy", R.string.slide_quick_easy, R.drawable.quick));
        slidePageAdapter=new SlidePageAdapter(this,itemList);
        screenPage.setAdapter(slidePageAdapter);

addDots(0);
screenPage.addOnPageChangeListener(listener);


    }
    ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPageSelected(int position) {
            currentPosition=position;
addDots(position);
if(position==3)
{

    animation= AnimationUtils.loadAnimation(OnBoardingScreen.this,
            R.anim.button_animation);
    animation.setDuration(400);
start.setAnimation(animation);
    start.setVisibility(View.VISIBLE);
}
else
{
    start.setVisibility(View.GONE);
}
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDots(int position) {

        dots=new TextView[itemList.size()];
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226",i));
            dots[i].setTextSize(45);
dotsLayout.addView(dots[i]);

        }
        if(dots.length>0)
        {
            dots[position].setTextColor(Color.RED);
        }

    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.move_next:
            {
                if(currentPosition!=3)
                {
                    screenPage.setCurrentItem(currentPosition+1,true);
                }
                break;
            }
            case R.id.move_previous:
            {
                if(currentPosition!=0)
                {
                    screenPage.setCurrentItem(currentPosition-1,true);
                }
                break;
            }
            case R.id.start_button:
            {
                startActivity(new Intent(OnBoardingScreen.this, MainActivity.class));
                finish();
            }
            case R.id.skip_button:
            {
                startActivity(new Intent(OnBoardingScreen.this, MainActivity.class));
                finish();
            }
        }
    }
}