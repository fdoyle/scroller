package com.lacronicus.scrollview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeTransform;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


  ScrollerView.ScrollFunc sinFunc = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      float sinY = (float) Math.sin((scrollX * 1.2f + targetWidth * index) * 3.14f);
      return sinY * .1f;
    }
  };

  ScrollerView.ScrollFunc xyFunc = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      return -(scrollX* 1.2f + targetWidth * index) / 1.2f;
    }
  };

  ScrollerView.ScrollFunc handY = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      float x = (scrollX * 1.2f + targetWidth * index) / 1.2f;
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return (float) -Math.pow(x / 3, 2);
    }
  };

  ScrollerView.ScrollFunc handX = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      float x = (virtualScrollX * 1.2f + targetWidth * index) / 1.2f;
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return x;
    }
  };

  ScrollerView.ScrollFunc handRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      float x = (virtualScrollX * 1.2f + targetWidth * index) / 1.2f;
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return 10 * x;
    }
  };

  ScrollerView.ScrollFunc zero = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      return 0;
    }
  };

  ScrollerView.ScrollFunc defaultX = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return (virtualScrollX * 1.2f + targetWidth * index) / 1.2f;
    }
  };

  ScrollerView.ScrollFunc zeroRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return 0;
    }
  };

  ScrollerView.ScrollFunc spinnyRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return 360 * (virtualScrollX * 1.2f + targetWidth * index) / 1.2f;

    }
  };


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final ViewGroup root = findViewById(R.id.container);
    final ScrollerView scrollerView = findViewById(R.id.scroller);
    findViewById(R.id.sin).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setEquationX(defaultX);
        scrollerView.setEquationY(sinFunc);
        scrollerView.setEquationRotation(zeroRotation);
        scrollerView.updateViewPositions(true);
      }
    });

    findViewById(R.id.xy).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setEquationY(xyFunc);
        scrollerView.setEquationX(defaultX);
        scrollerView.setEquationRotation(spinnyRotation);
        scrollerView.updateViewPositions(true);
      }
    });

    findViewById(R.id.inversion).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setEquationY(zero);
        scrollerView.setEquationX(zero);
        scrollerView.setEquationRotation(zeroRotation);
        scrollerView.updateViewPositions(true);
      }
    });

    findViewById(R.id.hand).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setEquationX(handX);
        scrollerView.setEquationY(handY);
        scrollerView.setEquationRotation(handRotation);
        scrollerView.updateViewPositions(true);
      }
    });


    List<View> viewList = new ArrayList<>();
    for (int i = 0; i != 30; i++) {
      View box = LayoutInflater.from(this).inflate(R.layout.card, root, false);
      box.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
      root.addView(box);
      viewList.add(box);
    }
    scrollerView.setViews(viewList);

    scrollerView.setEquationY(sinFunc);
    root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        scrollerView.updateViewPositions(true);
      }
    });
  }
}
