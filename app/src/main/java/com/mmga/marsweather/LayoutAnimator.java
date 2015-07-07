package com.mmga.marsweather;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mmga on 2015/7/6.
 */
public class LayoutAnimator {

    public static Animator ofHeight( final View view,int startHeight, int endHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.height = currentValue;
                view.setLayoutParams(lp);

            }
        });
        return animator;
    }

}
