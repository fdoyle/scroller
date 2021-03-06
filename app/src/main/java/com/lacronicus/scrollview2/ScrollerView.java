package com.lacronicus.scrollview2;

import android.content.Context;
import android.os.Build;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.FloatValueHolder;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.animation.SpringForce.DAMPING_RATIO_NO_BOUNCY;
import static android.support.animation.SpringForce.STIFFNESS_MEDIUM;

/**
 * Created by frankd on 9/14/17.
 */

public class ScrollerView extends View {

  ScrollFunc funcX;
  ScrollFunc funcY;
  ScrollFunc rotationFunc;


  float scrollX = 0;
  float scrollY = 0;

  float minX = 0;
  float maxX = 0;
  float minY = 0;
  float maxY = 0;

  float scrollScaleX = 1;
  float scrollScaleY = 1;

  float overlapRatio = 1;

  List<View> viewList = new ArrayList<>();

  VelocityTracker tracker = VelocityTracker.obtain();

  Map<View, SpringAnimation> animationMapX = new HashMap<>();
  Map<View, SpringAnimation> animationMapY = new HashMap<>();
  Map<View, SpringAnimation> animationMapRotation = new HashMap<>();

  public ScrollerView(Context context) {
    super(context);
    init();
  }

  public ScrollerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }


  public void init() {
    funcX = new ScrollFunc() {
      @Override
      public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
        return scrollX + targetWidth * index;
      }
    };
    funcY = new ScrollFunc() {
      @Override
      public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
        return (float) Math.sin((scrollX + targetWidth * index) * 3.14f) / 2;
      }
    };
    rotationFunc = new ScrollFunc() {
      @Override
      public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
        return 0;
      }
    };


    setOnTouchListener(new OnTouchListener() {
      float lastX = -1;
      float lastY = -1;

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        float curX = motionEvent.getRawX();
        float curY = motionEvent.getRawY();
        tracker.addMovement(motionEvent);

        switch (motionEvent.getAction()) {
          case MotionEvent.ACTION_DOWN:


            break;
          case MotionEvent.ACTION_MOVE:
            float deltaX = curX - lastX;
            float deltaY = curY - lastY;
            scrollX += deltaX;
            scrollY += deltaY;
            constrainScrollPositionToBounds();
            updateViewPositions(false);


            break;
          case MotionEvent.ACTION_UP:
            //todo animate
            tracker.computeCurrentVelocity(1000);
            FlingAnimation animation = new FlingAnimation(new FloatValueHolder(0)).setStartVelocity(tracker.getXVelocity()).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
              float lastX;

              @Override
              public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (value == 0) {
                  lastX = value;
                } else {
                  scrollX += value - lastX;
                }
                constrainScrollPositionToBounds();
                lastX = value;
                updateViewPositions(false);
              }
            });
            animation.start();

            FlingAnimation animationY = new FlingAnimation(new FloatValueHolder(0)).setStartVelocity(tracker.getYVelocity()).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
              float lastY;

              @Override
              public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (value == 0) {
                  lastY = value;
                } else {
                  scrollY += value - lastY;
                }
                constrainScrollPositionToBounds();
                lastY = value;
                updateViewPositions(false);
              }
            });
            animationY.start();
            break;
        }

        lastX = curX;
        lastY = curY;

        return true;
      }
    });
  }

  private void constrainScrollPositionToBounds() {
    if (scrollX < minX)
      scrollX = minX;
    if (scrollX > maxX)
      scrollX = maxX;
    if (scrollY < minY)
      scrollY = minY;
    if (scrollY > maxY)
      scrollY = maxY;
  }

  public void setScrollScaleX(float scrollScaleX) {
    float oldScaleX = this.scrollScaleX;
    this.scrollScaleX = scrollScaleX;
    scrollX = scrollX / oldScaleX * scrollScaleX;
  }

  public void setScrollScaleY(float scrollScaleY) {
    float oldScaleY = this.scrollScaleY;
    this.scrollScaleY = scrollScaleY;
    scrollY = scrollY / oldScaleY * scrollScaleY;
  }

  public void setOverlapRatio(float overlapRatio) {
    this.overlapRatio = overlapRatio;
  }

  public void updateViewPositions(boolean animate) {
    Log.d("TAG", String.format("scroll %s %s", scrollX, scrollY));
    for (int i = 0; i != viewList.size(); i++) {
      View view = viewList.get(i);
      float normalizedX = funcX.getScreenPosition(this,
              i,
              view.getWidth() / getxNormalDistance(),
              view.getHeight() / getyNormalDistance(),
              scrollX / getxNormalDistance() / scrollScaleX,
              scrollY / getyNormalDistance() / scrollScaleY
      ) * scrollScaleX;
      float normalizedY = funcY.getScreenPosition(this,
              i,
              view.getWidth() / getxNormalDistance(),
              view.getHeight() / getyNormalDistance(),
              scrollX / getxNormalDistance() / scrollScaleX,
              scrollY / getyNormalDistance() / scrollScaleY
      ) * scrollScaleY;
      float rawX = getOriginX() + getxNormalDistance() * normalizedX;
      float rawY = getOriginY() + getyNormalDistance() * normalizedY;

      float rotation = rotationFunc.getScreenPosition(this,
              i,
              view.getWidth() / getxNormalDistance(),
              view.getHeight() / getyNormalDistance(),
              scrollX / getxNormalDistance() / scrollScaleX,
              scrollY / getyNormalDistance() / scrollScaleY);

      centerViewOnPoint(view, rawX, rawY, rotation);
    }
  }

  public float getxNormalDistance() {
    return getWidth() / 2;
  }

  public float getyNormalDistance() {
    return getHeight() / 2;
  }

  public void setEquationX(ScrollFunc funcX) {
    this.funcX = funcX;
  }

  public void setEquationY(ScrollFunc funcY) {
    this.funcY = funcY;
  }

  public void setEquationRotation(ScrollFunc rotationFunc) {
    this.rotationFunc = rotationFunc;
  }

  public void setMinX(float minX) {
    this.minX = minX;
  }

  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  public void setMinY(float minY) {
    this.minY = minY;
  }

  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  public void setViews(List<View> views) {
    this.viewList.clear();
    this.viewList.addAll(views);
    animationMapX.clear();
    animationMapY.clear();
    for (View view : views) {
      animationMapX.put(view, new SpringAnimation(view, DynamicAnimation.X));
      animationMapX.get(view).setSpring(new SpringForce().setStiffness(STIFFNESS_MEDIUM).setDampingRatio(DAMPING_RATIO_NO_BOUNCY).setFinalPosition(0));

      animationMapY.put(view, new SpringAnimation(view, DynamicAnimation.Y));
      animationMapY.get(view).setSpring(new SpringForce().setStiffness(STIFFNESS_MEDIUM).setDampingRatio(DAMPING_RATIO_NO_BOUNCY).setFinalPosition(0));

      animationMapRotation.put(view, new SpringAnimation(view, DynamicAnimation.ROTATION));
      animationMapRotation.get(view).setSpring(new SpringForce().setStiffness(STIFFNESS_MEDIUM).setDampingRatio(DAMPING_RATIO_NO_BOUNCY).setFinalPosition(0));

    }
  }

  private float getOriginX() {
    return getX() + getWidth() / 2;
  }

  private float getOriginY() {
    return getY() + getHeight() / 2;
  }


  public void centerViewOnPoint(View view, float x, float y, float rotation) {
    float halfWidth = view.getWidth() / 2.0f;
    float halfHeight = view.getHeight() / 2.0f;
    animationMapX.get(view).setMinimumVisibleChange(1).animateToFinalPosition(x - halfWidth);
    animationMapY.get(view).setMinimumVisibleChange(1).animateToFinalPosition(getHeight() - y - halfHeight);
    animationMapRotation.get(view).setMinimumVisibleChange(1).animateToFinalPosition(rotation);
  }

  interface ScrollFunc {
    float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY);
  }

  interface RotationFunc {
    float getRotation(ScrollerView scroller, int index, float virtualScrollX, float virtualScrollY);
  }
}
