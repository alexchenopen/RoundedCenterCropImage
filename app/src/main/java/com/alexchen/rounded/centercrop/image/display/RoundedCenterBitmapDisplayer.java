package com.alexchen.rounded.centercrop.image.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Created by chenlin on 17/6/21.
 */

public class RoundedCenterBitmapDisplayer implements BitmapDisplayer {

    protected final int cornerRadius;
    protected final int margin;
    protected final float targetWidthHeightRatio;

    public RoundedCenterBitmapDisplayer(int cornerRadiusPixels, float targetWidthHeightRatio) {
        this(cornerRadiusPixels, 0, targetWidthHeightRatio);
    }

    public RoundedCenterBitmapDisplayer(int cornerRadiusPixels, int marginPixels, float targetWidthHeightRatio) {
        this.cornerRadius = cornerRadiusPixels;
        this.margin = marginPixels;
        this.targetWidthHeightRatio = targetWidthHeightRatio;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new RoundedCenterDrawable(bitmap, cornerRadius, margin, targetWidthHeightRatio));
    }

    public static class RoundedCenterDrawable extends Drawable {

        protected final float cornerRadius;
        protected final int margin;
        protected final float targetWidthHeightRatio;

        protected final RectF mRect = new RectF(),
                mBitmapRect;
        protected final BitmapShader bitmapShader;
        protected final Paint paint;

        public RoundedCenterDrawable(Bitmap bitmap, int cornerRadius, int margin, float targetWidthHeightRatio) {
            this.cornerRadius = cornerRadius;
            this.margin = margin;
            this.targetWidthHeightRatio = targetWidthHeightRatio;

            float bWidth = (float)bitmap.getWidth();
            float bHeight =(float) bitmap.getHeight();
            float ratio = bWidth / bHeight;
            Bitmap targetBitmap = null;
            if (ratio > targetWidthHeightRatio) {
                int height = bitmap.getHeight();
                int width = (int) (height * targetWidthHeightRatio);
                int x = (bitmap.getWidth() - width) / 2;
                int y = 0;
                targetBitmap = bitmap.createBitmap(bitmap, x, y, width, height);
            } else {
                int width = bitmap.getWidth();
                int height = (int) (width / targetWidthHeightRatio);
                int x = 0;
                int y = (bitmap.getHeight() - height) / 2;
                targetBitmap = bitmap.createBitmap(bitmap, x, y, width, height);
            }

            bitmapShader = new BitmapShader(targetBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapRect = new RectF(margin, margin, targetBitmap.getWidth() - margin, targetBitmap.getHeight() - margin);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
            paint.setFilterBitmap(true);
            paint.setDither(true);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);

        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}
