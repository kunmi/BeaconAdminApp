package com.blogspot.kunmii.beaconadmin.Helpers;

import android.graphics.Bitmap;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import java.util.List;


public class FloorImageView extends SubsamplingScaleImageView implements Target{

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private PointF sPin;

    int imageWidth;
    int imageHeight;


    private Bitmap iBeaconPin;
    private Bitmap eddyPin;
    private Bitmap unsavedPin;

    List<Beacon> beacons = null;
    List<Beacon> unsavedBeacons = null;

    boolean dropPinMode = false;

    public FloorImageView(Context context) {
        this(context, null);
    }

    public FloorImageView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    public void setBeacons(List<Beacon> data, List<Beacon> unsavedBeacons){
        beacons = data;
        this.unsavedBeacons = unsavedBeacons;
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;


        float w = (density/420f) * Config.ICON_WIDTH;
        float h = (density/420f) * Config.ICON_HEIGHT;
        iBeaconPin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.beacon_icon), (int)w, (int)h, true);
        eddyPin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.beacon_icon_unsaved), (int)w, (int)h, true);
        unsavedPin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.beacon_icon_unsaved), (int)w, (int)h, true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);


        if(beacons!=null)
        {
            for(Beacon b : beacons)
            {

                //Todo make below computation more efficient
                PointF vPin = b.getCoordsAsPixel(imageWidth,imageHeight);
                PointF vCenter = sourceToViewCoord(vPin);

                Bitmap icon = null;

                if(b.isIbeacon())
                   icon = iBeaconPin;
                else
                    icon = eddyPin;


                float vX = vCenter.x - (icon.getWidth()/2);
                float vY = vCenter.y - icon.getHeight();

                canvas.drawBitmap(icon, vX, vY, paint);
            }

            if(unsavedBeacons!=null)
            for (Beacon b : unsavedBeacons)
            {
                PointF vPin = b.getCoordsAsPixel(imageWidth,imageHeight);
                PointF vCenter = sourceToViewCoord(vPin);

                Bitmap icon = unsavedPin;

                float vX = vCenter.x - (icon.getWidth()/2);
                float vY = vCenter.y - icon.getHeight();

                canvas.drawBitmap(icon, vX, vY, paint);
            }
        }


    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setImage(ImageSource.bitmap(bitmap));

        imageHeight = bitmap.getHeight();
        imageWidth = bitmap.getWidth();

    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setDropPinMode(boolean dropPinMode) {
        this.dropPinMode = dropPinMode;
    }
}