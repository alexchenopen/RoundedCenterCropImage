package com.alexchen.rounded.centercrop.image.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.alexchen.rounded.centercrop.image.R;
import com.alexchen.rounded.centercrop.image.display.RoundedBitmapDisplayer;
import com.alexchen.rounded.centercrop.image.display.RoundedCenterBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Created by chenlin on 17/6/21.
 */

public class HomeActivity extends Activity {

    private String imgUrl;
    private ImageView imgNormal, imgRounded, imgRoundedCenterCrop;
    private DisplayImageOptions normalOptions, roundedOptions, roundedCenterCropOptions;
    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498022267311&di=5766410dc8c0332c3ac131cc3da17dd3&imgtype=0&src=http%3A%2F%2Fp8.qhimg.com%2Ft01593e0067e40d9645.jpg";

        normalOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_load_default)
                .showImageForEmptyUri(R.drawable.image_load_default)
                .showImageOnFail(R.drawable.image_load_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        roundedOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(20))
                .showImageOnLoading(R.drawable.image_load_default)
                .showImageForEmptyUri(R.drawable.image_load_default)
                .showImageOnFail(R.drawable.image_load_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        roundedCenterCropOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedCenterBitmapDisplayer(20, 2))
                .showImageOnLoading(R.drawable.image_load_default)
                .showImageForEmptyUri(R.drawable.image_load_default)
                .showImageOnFail(R.drawable.image_load_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imgNormal = (ImageView) findViewById(R.id.imgNormal);
        imgRounded = (ImageView) findViewById(R.id.imgRounded);
        imgRoundedCenterCrop = (ImageView) findViewById(R.id.imgRoundedCenterCrop);

        imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited())
            imageLoader.init(new ImageLoaderConfiguration.Builder(this)
                    .memoryCacheExtraOptions(400, 400)
                    .diskCacheExtraOptions(400, 400, null)
                    .threadPoolSize(5)
                    .threadPriority(Thread.NORM_PRIORITY)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13)
                    .diskCache(
                            new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
                                    this, true)))
                    .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .imageDownloader(new BaseImageDownloader(this))
                    .imageDecoder(new BaseImageDecoder(false))
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .defaultDisplayImageOptions(normalOptions).build());


        imageLoader.displayImage(imgUrl, imgNormal, normalOptions);
        imageLoader.displayImage(imgUrl, imgRounded, roundedOptions);
        imageLoader.displayImage(imgUrl, imgRoundedCenterCrop, roundedCenterCropOptions);
    }
}
