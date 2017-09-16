package com.lacronicus.scrollview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {




  int totalWidth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final ViewGroup root = findViewById(R.id.container);
    final ScrollerView scrollerView = findViewById(R.id.scroller);
    final ScrollerFuncFactory factory = new ScrollerFuncFactory();
    findViewById(R.id.sin).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setScrollScaleX(0.8f);
        scrollerView.setEquationX(factory.defaultX);
        scrollerView.setEquationY(factory.sinFunc);
        scrollerView.setEquationRotation(factory.zeroRotation);
        scrollerView.updateViewPositions(true);
        scrollerView.setMinX(totalWidth * 0.8f);
      }
    });

    findViewById(R.id.xy).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setScrollScaleX(1);
        scrollerView.setEquationY(factory.xyFunc);
        scrollerView.setEquationX(factory.defaultX);
        scrollerView.setEquationRotation(factory.spinnyRotation);
        scrollerView.updateViewPositions(true);
        scrollerView.setMinX(totalWidth);
      }
    });

    findViewById(R.id.zero).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setScrollScaleX(1);
        scrollerView.setEquationY(factory.zero);
        scrollerView.setEquationX(factory.zero);
        scrollerView.setEquationRotation(factory.zeroRotation);
        scrollerView.updateViewPositions(true);
        scrollerView.setMinX(totalWidth);
      }
    });

    findViewById(R.id.hand).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollerView.setScrollScaleX(0.8f);
        scrollerView.setEquationX(factory.handX);
        scrollerView.setEquationY(factory.handY);
        scrollerView.setEquationRotation(factory.handRotation);
        scrollerView.updateViewPositions(true);
        scrollerView.setMinX(totalWidth * 0.8f);
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

    //last item should be at 9280/7733
    scrollerView.setEquationY(factory.sinFunc);
    root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        totalWidth = -29 * scrollerView.viewList.get(0).getMeasuredWidth();
        scrollerView.setMinX(totalWidth);
        scrollerView.updateViewPositions(true);
      }
    });
  }
}
