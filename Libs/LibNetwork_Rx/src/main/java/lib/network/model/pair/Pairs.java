package lib.network.model.pair;

/**
 * @auther yuansui
 * @since 2017/11/10
 */

public class Pairs extends BasePairs<String> {

    public void add(String name, int val) {
        super.add(name, String.valueOf(val));
    }
}
