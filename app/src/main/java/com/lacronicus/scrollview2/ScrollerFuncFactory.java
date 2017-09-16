package com.lacronicus.scrollview2;

/**
 * Created by frankd on 9/16/17.
 */

public class ScrollerFuncFactory {

  public final ScrollerView.ScrollFunc sinFunc = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      float sinY = (float) Math.sin((scrollX + targetWidth * index) * 3.14f / 2);
      return sinY * .2f;
    }
  };

  public final ScrollerView.ScrollFunc xyFunc = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      return -(scrollX + targetWidth * index);
    }
  };

  public final ScrollerView.ScrollFunc handY = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      float x = (scrollX + targetWidth * index);
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return (float) -Math.pow(x / 3, 2);
    }
  };

  public final ScrollerView.ScrollFunc handX = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      float x = (virtualScrollX + targetWidth * index);
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return x;
    }
  };

  public final ScrollerView.ScrollFunc handRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      float x = (virtualScrollX + targetWidth * index);
      if (x > 1 + targetWidth) {
        x = 1 + targetWidth;
      }

      if (x < -1 - targetWidth) {
        x = -1 - targetWidth;
      }
      return 10 * x;
    }
  };

  public final ScrollerView.ScrollFunc zero = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scrollerView, int index, float targetWidth, float targetHeight, float scrollX, float scrollY) {
      return 0;
    }
  };

  public final ScrollerView.ScrollFunc defaultX = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return (virtualScrollX + targetWidth * index);
    }
  };

  public final ScrollerView.ScrollFunc zeroRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return 0;
    }
  };

  public final ScrollerView.ScrollFunc spinnyRotation = new ScrollerView.ScrollFunc() {
    @Override
    public float getScreenPosition(ScrollerView scroller, int index, float targetWidth, float targetHeight, float virtualScrollX, float virtualScrollY) {
      return 360 * (virtualScrollX + targetWidth * index);

    }
  };
}
