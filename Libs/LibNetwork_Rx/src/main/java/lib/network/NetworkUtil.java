package lib.network;

import android.support.annotation.NonNull;

import lib.network.model.pair.Pair;
import lib.network.model.pair.Pairs;


/**
 * http里使用的一些小工具
 *
 * @author yuansui
 */
public class NetworkUtil {

    public static final String KTextEmpty = "";

    private static final char KSymbolQuestion = '?';
    private static final char KSymbolAnd = '&';
    private static final char KSymbolEqual = '=';


    /**
     * @param url
     * @param params
     * @return
     */
    public static String generateGetUrl(@NonNull String url, @NonNull Pairs params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(url)
                .append(KSymbolQuestion)
                .append(generateGetParams(params));

        return sb.toString();
    }

    /**
     * 按照格式生成get参数
     *
     * @param params
     * @return
     */
    public static String generateGetParams(Pairs params) {
        if (params == null || params.isEmpty()) {
            return KTextEmpty;
        }

        StringBuilder sb = new StringBuilder();
        Pair p;
        int size = params.size();
        for (int i = 0; i < size; i++) {
            p = params.get(i);
            if (p == null) {
                continue;
            }
            sb.append(p.getName());
            sb.append(KSymbolEqual);
            sb.append(p.getVal());

            if (i != size - 1) {
                sb.append(KSymbolAnd);
            }
        }

        return sb.toString();
    }
}
