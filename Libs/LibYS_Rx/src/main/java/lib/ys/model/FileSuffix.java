package lib.ys.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        FileSuffix.txt,
        FileSuffix.xml,
        FileSuffix.html,
        FileSuffix.jpg,
        FileSuffix.png,
        FileSuffix.js,
        FileSuffix.ppt,
        FileSuffix.pptx,
        FileSuffix.pdf,
        FileSuffix.amr,
        FileSuffix.mp4,
        FileSuffix.mp3,
})
@Retention(RetentionPolicy.SOURCE)
public @interface FileSuffix {
    String txt = ".txt";
    String xml = ".xml";
    String html = ".html";
    String jpg = ".jpg";
    String png = ".png";
    String js = ".js";
    String ppt = ".ppt";
    String pptx = ".pptx";
    String pdf = ".pdf";
    String amr = ".amr";
    String mp4 = ".mp4";
    String mp3 = ".mp3";
}