package lib.ys.network.image;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.widget.ImageView.ScaleType;

import lib.ys.network.image.interceptor.Interceptor;
import lib.ys.network.image.shape.Renderer;

/**
 * @auther yuansui
 * @since 2017/11/16
 */

public interface Functions {
    NetworkImageView url(String url);

    NetworkImageView storage(String s);

    NetworkImageView res(@DrawableRes int id);

    NetworkImageView id(String id);

    NetworkImageView uri(Uri uri);

    NetworkImageView contentProvider(String path);

    NetworkImageView renderer(Renderer renderer);

    NetworkImageView addInterceptor(Interceptor i);

    NetworkImageView removeInterceptor(Interceptor i);

    /**
     * 设置图片加载监听
     *
     * @param listener
     */
    NetworkImageView listener(NetworkImageListener listener);

    NetworkImageView placeHolder(@DrawableRes int id);

    NetworkImageView fade(int duration);

    NetworkImageView resize(@IntRange(from = 1, to = Integer.MAX_VALUE) int w, @IntRange(from = 1, to = Integer.MAX_VALUE) int h);

    NetworkImageView scaleType(ScaleType type);

}
