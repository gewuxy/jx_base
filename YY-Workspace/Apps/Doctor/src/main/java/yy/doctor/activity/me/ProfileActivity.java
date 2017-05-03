package yy.doctor.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import lib.ys.ex.NavBar;
import lib.ys.form.FormItemEx.TFormElem;
import lib.ys.network.image.NetworkImageView;
import lib.ys.network.image.renderer.CircleRenderer;
import lib.ys.util.PhotoUtil;
import lib.yy.activity.base.BaseFormActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.dialog.BottomDialog;
import yy.doctor.dialog.BottomDialog.OnDialogItemClickListener;
import yy.doctor.model.form.Builder;
import yy.doctor.model.form.FormType;
import yy.doctor.util.CacheUtil;
import yy.doctor.util.Util;


/**
 * 我的资料
 *
 * @author CaiXiang
 * @since 2017/4/13
 */
public class ProfileActivity extends BaseFormActivity {

    private static final int KColorNormal = Color.parseColor("#666666");
    private static final int KColorCancel = Color.parseColor("#01b557");

    private static final int KCodeAlbum = 100;
    private static final int KCodePhotograph = 200;


    private static final String KPhotoFileName = "avatar.jpg";

    private RelativeLayout mLayoutProfileHeader;
    private NetworkImageView mIvAvatar;

    //    private Uri mPhotoUri;
    private String mPhotoPath;

    @IntDef({
            RelatedId.name,
            RelatedId.hospital,
            RelatedId.departments,

            RelatedId.nickname,
            RelatedId.phone_number,
            RelatedId.email,

            RelatedId.certification_number,
            RelatedId.rank,
            RelatedId.position,
            RelatedId.sex,
            RelatedId.education_background,
            RelatedId.address,

            RelatedId.is_open,
    })
    private @interface RelatedId {

        int name = 0;
        int hospital = 1;
        int departments = 2;

        int nickname = 3;
        int phone_number = 4;
        int email = 5;

        int certification_number = 6;
        int rank = 7;
        int position = 8;
        int sex = 9;
        int education_background = 10;
        int address = 11;

        int is_open = 20;

    }

    @Override
    public void initNavBar(NavBar bar) {

        Util.addBackIcon(bar, "我的资料", this);
        bar.addTextViewRight("完成并保存", new OnClickListener() {

            @Override
            public void onClick(View v) {
                showToast("85654");
            }
        });

    }

    @Override
    protected View createHeaderView() {
        return inflate(R.layout.layout_profile_header);
    }

    @Override
    public void initData() {
        super.initData();

        addItem(new Builder(FormType.et)
                .related(RelatedId.name)
                .name("姓名")
                .text("sdfsdf")
                .hint(R.string.hint_not_fill)
                .enable(false)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.hospital)
                .drawable(R.mipmap.ic_more)
                .name("医院")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et_intent)
                .related(RelatedId.departments)
                .drawable(R.mipmap.ic_more)
                .name("科室")
                .intent(new Intent(this, DepartmentsActivity.class))
                .text(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider_large).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.nickname)
                .name("昵称")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.phone_number)
                .name("手机号")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.email)
                .name("电子邮箱")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider_large).build());

        /*addItem(new Builder(FormType.profile_checkbox)
                .related(RelatedId.is_open)
                .build());*/

        addItem(new Builder(FormType.et)
                .related(RelatedId.certification_number)
                .name("职业资格证号")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.rank)
                .name("职称")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.et)
                .related(RelatedId.position)
                .name("职务")
                .hint(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.text)
                .related(RelatedId.sex)
                .name("性别")
                .text(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.text)
                .related(RelatedId.education_background)
                .name("学历")
                .text(R.string.hint_not_fill)
                .build());

        addItem(new Builder(FormType.divider).build());

        addItem(new Builder(FormType.text_intent)
                .related(RelatedId.address)
                .name("所在城市")
                .intent(new Intent(this, ProvinceCityActivity.class))
                .text("广东-广州")
                .build());

        addItem(new Builder(FormType.divider_large)
                .build());
    }

    @Override
    public void findViews() {
        super.findViews();

        mLayoutProfileHeader = findView(R.id.layout_profile_header);
        mIvAvatar = findView(R.id.profile_header_iv_avatar);

    }

    @Override
    public void setViews() {
        super.setViews();

        mLayoutProfileHeader.setOnClickListener(this);
        mIvAvatar.placeHolder(R.mipmap.form_ic_personal_head)
                .renderer(new CircleRenderer())
                .load();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.layout_profile_header: {
                showDialogSelectPhoto();
            }
            break;
        }

    }

    @Override
    protected void onFormItemClick(View v, int position) {

        @RelatedId int relatedId = getItem(position).getInt(TFormElem.related);
        switch (relatedId) {
            case RelatedId.sex: {
                showDialogSelectSex(position);
            }
            break;
            case RelatedId.education_background: {
                showDialogSelectEducationBackground(position);
            }
            break;
        }
    }

    @Override
    protected void onFormViewClick(View v, int position, Object related) {
        super.onFormViewClick(v, position, related);

        @RelatedId int relatedId = getItem(position).getInt(TFormElem.related);

        if (v instanceof ImageView) {

            switch (relatedId) {
                case RelatedId.hospital: {
                    showToast("852");
                }
                break;
            }
        }

    }

    private void showDialogSelectPhoto() {

        final List<String> data = new ArrayList<>();
        data.add("从相册中选择照片");
        data.add("拍照");
        data.add("取消");

        final BottomDialog dialog = new BottomDialog(this, new OnDialogItemClickListener() {

            @Override
            public void onDialogItemClick(int position) {

                switch (position) {
                    case 0: {
                        getPhotoFromAlbum();
                    }
                    break;
                    case 1: {
                        getPhotoFromTakePhotos();
                    }
                    break;
                }

            }
        });

        for (int i = 0; i < data.size(); ++i) {

            if (i != (data.size() - 1)) {
                dialog.addItem(data.get(i), KColorNormal);
            } else {
                dialog.addItem(data.get(i), KColorCancel);
            }
        }

        dialog.show();

    }

    private void getPhotoFromAlbum() {
        PhotoUtil.fromLocal(this, KCodeAlbum);
    }

    private void getPhotoFromTakePhotos() {
        mPhotoPath = CacheUtil.getBmpCacheDir() + KPhotoFileName;
        PhotoUtil.photograph(this, mPhotoPath, KCodePhotograph);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        String path = null;
        switch (requestCode) {
            case KCodeAlbum: {
                // 查看相册获得图片返回
                path = PhotoUtil.getPath(this, data.getData());
            }
            break;
            case KCodePhotograph: {
                // 通过照相机拍的图片
                path = mPhotoPath;
            }
            break;
        }

        if (path != null) {
            Intent intent = new Intent(this, ClipImageActivity.class);
            intent.putExtra(Extra.KData, path);
            startActivity(intent);
        }
    }

    private void showDialogSelectSex(final int pos) {

        final List<String> data = new ArrayList<>();
        data.add("男");
        data.add("女");

        final BottomDialog dialog = new BottomDialog(this, new OnDialogItemClickListener() {

            @Override
            public void onDialogItemClick(int position) {

                getItem(pos).put(TFormElem.text, data.get(position));
                refreshItem(pos);
            }
        });

        for (int i = 0; i < data.size(); ++i) {
            dialog.addItem(data.get(i), KColorNormal);
        }

        dialog.show();

    }

    private void showDialogSelectEducationBackground(final int pos) {

        final List<String> data = new ArrayList<>();
        data.add("专科");
        data.add("本科");
        data.add("硕士");
        data.add("博士");
        data.add("博士后");

        final BottomDialog dialog = new BottomDialog(this, new OnDialogItemClickListener() {

            @Override
            public void onDialogItemClick(int position) {

                getItem(pos).put(TFormElem.text, data.get(position));
                refreshItem(pos);
            }
        });

        for (int i = 0; i < data.size(); ++i) {
            dialog.addItem(data.get(i), KColorNormal);
        }

        dialog.show();

    }



//    /**
//     * 解决小米手机上获取图片路径为null的情况
//     *
//     * @param intent
//     * @return
//     */
//    public Uri getUri(Intent intent) {
//        Uri uri = intent.getData();
//        String type = intent.getType();
//        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
//            String path = uri.getEncodedPath();
//            if (path != null) {
//                path = Uri.decode(path);
//                ContentResolver cr = this.getContentResolver();
//                StringBuffer buff = new StringBuffer();
//                buff.append("(").append(Images.ImageColumns.DATA).append("=")
//                        .append("'" + path + "'").append(")");
//                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
//                        new String[]{Images.ImageColumns._ID},
//                        buff.toString(), null, null);
//                int index = 0;
//                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
//                    index = cur.getColumnIndex(Images.ImageColumns._ID);
//                    // set _id value
//                    index = cur.getInt(index);
//                }
//                if (index == 0) {
//                    // do nothing
//                } else {
//                    Uri uri_temp = Uri
//                            .parse("content://media/external/images/media/"
//                                    + index);
//                    if (uri_temp != null) {
//                        uri = uri_temp;
//                    }
//                }
//            }
//        }
//        return uri;
//    }


}
