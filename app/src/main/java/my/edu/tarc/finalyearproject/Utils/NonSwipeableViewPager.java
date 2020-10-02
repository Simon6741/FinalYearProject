package my.edu.tarc.finalyearproject.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    private void setMyScroller() throws NoSuchFieldException {
        try{
            Class<?> viewPager = ViewPager.class;
            Field scroller = viewPager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this,new MyScroller(getContext()));
            //scroller.set(this,new MyScroller(getContext()));

        }catch (NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public NonSwipeableViewPager(@NonNull Context context)  {
        super(context);
        try {
            setMyScroller();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public NonSwipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) throws NoSuchFieldException, IllegalAccessException {
        super(context, attrs);
        setMyScroller();
    }
    private  class MyScroller extends Scroller{
        public MyScroller(Context context){
            super(context,new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,int duration) {
            super.startScroll(startX, startY, dx, dy,400);

        }
    }

}
