package yy.doctor.ui.activity.meeting;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lib.bd.location.Gps.TGps;
import lib.bd.location.Location;
import lib.bd.location.LocationNotifier;
import lib.bd.location.OnLocationNotify;
import lib.network.model.NetworkResp;
import lib.network.model.err.NetError;
import lib.ys.YSLog;
import lib.ys.config.AppConfig.RefreshWay;
import lib.ys.model.MapList;
import lib.ys.network.image.NetworkImageView;
import lib.ys.network.image.renderer.CircleRenderer;
import lib.ys.ui.decor.DecorViewEx.ViewState;
import lib.ys.ui.other.NavBar;
import lib.ys.util.LaunchUtil;
import lib.ys.util.TimeUtil;
import lib.ys.util.TimeUtil.TimeFormat;
import lib.ys.util.permission.Permission;
import lib.ys.util.permission.PermissionResult;
import lib.ys.util.res.ResLoader;
import lib.yy.network.Result;
import lib.yy.notify.Notifier.NotifyType;
import lib.yy.ui.activity.base.BaseActivity;
import yy.doctor.Extra;
import yy.doctor.R;
import yy.doctor.ui.activity.me.epn.EpnRechargeActivity;
import yy.doctor.ui.activity.me.unitnum.FileDataActivity;
import yy.doctor.ui.activity.me.unitnum.UnitNumDetailActivity.AttentionUnitNum;
import yy.doctor.dialog.HintDialogMain;
import yy.doctor.dialog.LocationDialog;
import yy.doctor.dialog.ShareDialog;
import yy.doctor.model.Profile;
import yy.doctor.model.Profile.TProfile;
import yy.doctor.model.home.RecUnitNum.Attention;
import yy.doctor.model.meet.CourseInfo;
import yy.doctor.model.meet.MeetDetail;
import yy.doctor.model.meet.MeetDetail.TMeetDetail;
import yy.doctor.model.meet.Module;
import yy.doctor.model.meet.Module.TModule;
import yy.doctor.model.meet.PPT;
import yy.doctor.model.meet.PPT.TPPT;
import yy.doctor.model.meet.Sign;
import yy.doctor.model.meet.Sign.TSign;
import yy.doctor.model.meet.Submit;
import yy.doctor.model.meet.Submit.TSubmit;
import yy.doctor.model.meet.exam.Intro;
import yy.doctor.model.unitnum.FileData;
import yy.doctor.network.JsonParser;
import yy.doctor.network.NetFactory;
import yy.doctor.network.UrlUtil;
import yy.doctor.network.UrlUtil.UrlMeet;
import yy.doctor.serv.CommonServ;
import yy.doctor.serv.CommonServ.ReqType;
import yy.doctor.util.Time;
import yy.doctor.util.UISetter;
import yy.doctor.util.Util;
import yy.doctor.view.ModuleView;

/**
 * 会议详情界面
 * <p>
 * 日期 : 2017/4/21
 * 创建人 : guoxuan
 */
public class MeetingDetailsActivity extends BaseActivity {

    private static final int KIdMeetDetail = 0; // 会议详情
    private static final int KIdCollection = 1; // 收藏
    private static final int KIdAttention = 2; // 关注

    private static final int KIdExam = 3; // 考试
    private static final int KIdQue = 4; // 问卷
    private static final int KIdVideo = 5; // 视频
    private static final int KIdSign = 6; // 签到
    private static final int KIdCourse = 7; // PPT

    private static final int KTextColor = ResLoader.getColor(R.color.text_333); // 模块没有时的颜色

    private final int KExamResId = R.mipmap.meeting_ic_exam_have; // 考试
    private final int KQueResId = R.mipmap.meeting_ic_que_have; // 问卷
    private final int KVideoResId = R.mipmap.meeting_ic_video_have; // 视频
    private final int KSignResId = R.mipmap.meeting_ic_sign_have; // 签到

    private ImageView mIvCollection; // navBar的收藏

    private NetworkImageView mIvPlay; // 播放图片
    private TextView mTvDate; // 会议开始时间
    private TextView mTvDuration; // 会议时长

    // 会议介绍
    private TextView mTvTitle; // 会议名称
    private TextView mTvEpn; // 象数相关
    private TextView mTvSection; // 会议科室

    // 单位号介绍
    private NetworkImageView mIvNumber; // 单位号图标
    private TextView mTvUnitNum; // 单位号

    //资料
    private View mLayoutData; // 查看资料
    private TextView mTvFileNum; // 文件个数
    private ImageView mIvFileArrow; // 箭头
    private LinearLayout mLayoutFile; // 文件布局
    private View mDividerFile; // 分割线(大)
    private View mDivider; // 分割线

    // 主讲者
    private NetworkImageView mIvGP; // 头像
    private TextView mTvGN; // 名字
    private TextView mTvGP; // 职位
    private TextView mTvGH; // 医院

    private TextView mTvIntro; // 会议简介

    // 底部按钮
    private ModuleView mLayoutExam; // 考试模块
    private ModuleView mLayoutQue; // 问卷模块
    private ModuleView mLayoutVideo; // 视频模块
    private ModuleView mLayoutSign; // 签到模块

    private TextView mTvSee; // 会议模块

    @EpnType
    private int mEpnType; // 需要还是奖励

    private int mEpn; // 象数
    private boolean mNoPPT; // 是否有PPT
    private String mMeetId; // 会议Id
    private String mMeetName; //  会议名字
    private String mLatitude; // 经度
    private String mLongitude; // 维度
    private MeetDetail mMeetDetail; // 会议详情信息
    private OnLocationNotify mObserver; // 定位通知

    private long mStartModuleTime;
    private long mMeetTime; // 统一用通知不用result

    private MapList<Integer, String> mMapList; // 记录模块ID

    private LocationDialog mLocationDialog; // 定位失败
    private HintDialogMain mPayEpnDialog; // 支付象数
    private HintDialogMain mNoEpnDialog; // 象数不足
    private HintDialogMain mAttentionDialog; // 关注
    private ShareDialog mShareDialog; // 分享

    /**
     * functionId,模块功能ID
     */
    @IntDef({
            FunctionType.ppt,
            FunctionType.video,
            FunctionType.exam,
            FunctionType.que,
            FunctionType.sign,
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface FunctionType {
        int ppt = 1; // 微课
        int video = 2; // 视频
        int exam = 3; // 考试
        int que = 4; // 问卷
        int sign = 5; // 签到
    }

    @IntDef({
            CollectType.cancel,
            CollectType.collect,
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface CollectType {
        int cancel = 0; // 收藏
        int collect = 1; // 没有收藏
    }

    @IntDef({
            EpnType.need,
            EpnType.award,
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface EpnType {
        int need = 0; // 需要象数
        int award = 1; // 奖励象数
    }

    private boolean needPay(@EpnType int type) {
        if (type == EpnType.need) {
            return true;
        } else {
            return false;
        }
    }

    public static void nav(Context context, String meetId, String name) {
        Intent i = new Intent(context, MeetingDetailsActivity.class)
                .putExtra(Extra.KData, meetId)
                .putExtra(Extra.KName, name);
        LaunchUtil.startActivity(context, i);
    }

    @NonNull
    @Override
    public int getContentViewId() {
        return R.layout.activity_meeting_detail;
    }

    @Override
    public void initData() {
        mNoPPT = false; // 默认没有PPT
        mMeetTime = 0;
        mMeetId = getIntent().getStringExtra(Extra.KData);
        mMeetName = getIntent().getStringExtra(Extra.KName); // 没有请求到数据时也可以分享会议
    }

    @Override
    public void initNavBar(NavBar bar) {
        Util.addBackIcon(bar, R.string.meeting_detail, this);
        // 收藏
        ViewGroup layout = bar.addViewRight(R.drawable.meeting_ppt_collection_selector, v -> {
            boolean storedState = true; // 默认没有关注, 故点击时关注(MeetDetail还没获取到数据时)
            if (mMeetDetail != null) {
                storedState = !mMeetDetail.getBoolean(TMeetDetail.stored);
                mMeetDetail.put(TMeetDetail.stored, storedState);
            }
            mIvCollection.setSelected(storedState);
            showToast(storedState ? R.string.collect : R.string.cancel_collect);
            exeNetworkReq(KIdCollection, NetFactory.collectMeeting(mMeetId, storedState ? CollectType.collect : CollectType.cancel));
            // 从收藏会议进入会议详情页, 取消收藏时, 通知会议收藏列表去除会议
            if (!storedState) {
                notify(NotifyType.cancel_collection_meeting, mMeetId);
            }
        });
        getCollection(layout);
        // 分享
        bar.addViewRight(R.mipmap.nav_bar_ic_share, v -> {
            mShareDialog = new ShareDialog(MeetingDetailsActivity.this, UrlUtil.getBaseUrl() + UrlMeet.KMeetShare + mMeetId, mMeetName);
            mShareDialog.show();
        });
    }

    @Override
    public void findViews() {
        mIvPlay = findView(R.id.meeting_detail_iv_play);
        mTvDate = findView(R.id.meeting_detail_tv_date);
        mTvDuration = findView(R.id.meeting_detail_tv_time);

        // 会议相关
        mTvTitle = findView(R.id.meeting_detail_tv_title);
        mTvEpn = findView(R.id.meeting_detail_tv_award);
        mTvSection = findView(R.id.meeting_detail_tv_section);

        // 单位号相关
        mIvNumber = findView(R.id.meeting_detail_iv_number);
        mTvUnitNum = findView(R.id.meeting_detail_tv_unit_num);

        // 文件
        mLayoutData = findView(R.id.meeting_detail_layout_data);
        mLayoutFile = findView(R.id.meeting_detail_layout_file);
        mTvFileNum = findView(R.id.meeting_detail_tv_data);
        mIvFileArrow = findView(R.id.meeting_detail_iv_data);
        mDividerFile = findView(R.id.meeting_detail_view_divider);
        mDivider = findView(R.id.meeting_detail_divider);

        // 主讲者相关
        mTvGN = findView(R.id.meeting_tv_guest_name);
        mTvGP = findView(R.id.meeting_tv_guest_post);
        mTvGH = findView(R.id.meeting_tv_guest_hospital);
        mIvGP = findView(R.id.meeting_iv_guest_portrait);

        mTvIntro = findView(R.id.meeting_detail_tv_intro);

        // 模块相关
        mLayoutExam = findView(R.id.meet_module_layout_exam);
        mLayoutQue = findView(R.id.meet_module_layout_que);
        mLayoutVideo = findView(R.id.meeting_detail_layout_video);
        mLayoutSign = findView(R.id.meeting_detail_layout_sign);
        mTvSee = findView(R.id.meeting_detail_video_see);
    }

    @Override
    public void setViews() {
        refresh(RefreshWay.embed);
        exeNetworkReq(KIdMeetDetail, NetFactory.meetInfo(mMeetId));
    }

    @Override
    public void onClick(View v) {
        if (mMeetDetail.getBoolean(TMeetDetail.attention)) {
            // 关注了
            if (mMeetDetail.getBoolean(TMeetDetail.attendAble)) {
                // 可以参加
                if (mMeetDetail.getBoolean(TMeetDetail.attended)) {
                    // 参加过(奖励过和支付过)
                    toClickModule(v);
                } else {
                    // 没有参加过
                    if (mEpn > 0) {
                        int surplus = Profile.inst().getInt(TProfile.credits); // 剩余象数
                        if (needPay(mEpnType)) {
                            // 需要象数的
                            if (surplus < mEpn) {
                                // 象数不足
                                mNoEpnDialog = new HintDialogMain(MeetingDetailsActivity.this);
                                mNoEpnDialog.setHint("您的剩余象数不足所需象数值, 请充值象数后继续");
                                mNoEpnDialog.addButton("充值象数", v1 -> {
                                    mNoEpnDialog.dismiss();
                                    startActivity(EpnRechargeActivity.class);
                                });
                                mNoEpnDialog.addButton(R.string.cancel, "#666666", v1 -> mNoEpnDialog.dismiss());
                                mNoEpnDialog.show();
                            } else {
                                mPayEpnDialog = new HintDialogMain(MeetingDetailsActivity.this);
                                mPayEpnDialog.setHint("本会议需要支付" + mEpn + "象数");
                                mPayEpnDialog.addButton("确认支付", v1 -> {
                                    mMeetDetail.put(TMeetDetail.attended, true); // 支付象数 (参加过会议)
                                    Profile.inst().put(TProfile.credits, surplus - mEpn);
                                    Profile.inst().saveToSp();
                                    notify(NotifyType.profile_change);
                                    mPayEpnDialog.dismiss();
                                    toClickModule(v);
                                });
                                mPayEpnDialog.addButton(R.string.cancel, "#666666", v1 -> mPayEpnDialog.dismiss());
                                mPayEpnDialog.show();
                            }
                        } else {
                            // FIXME: 2017/6/30 应该后台返回正确的,多人操作的时候
                            if (mMeetDetail.getInt(TMeetDetail.remainAward) > 0) {
                                // 奖励人数大于0奖励象数的
                                Profile.inst().put(TProfile.credits, surplus + mEpn);
                                Profile.inst().saveToSp();
                                notify(NotifyType.profile_change);
                            }
                            mMeetDetail.put(TMeetDetail.attended, true); // 奖励象数 (参加过会议)
                            toClickModule(v);
                        }
                    } else {
                        // 免费直接参加
                        toClickModule(v);
                    }
                }
            } else {
                // 不能参加的原因
                showToast(mMeetDetail.getString(TMeetDetail.reason));
            }
        } else {
            // 提示关注
            mAttentionDialog = new HintDialogMain(MeetingDetailsActivity.this);
            mAttentionDialog.setHint("请先关注会议");
            mAttentionDialog.addButton("确认关注", v1 -> exeNetworkReq(KIdAttention, NetFactory.attention(mMeetDetail.getInt(TMeetDetail.pubUserId), 1)));
            mAttentionDialog.addButton(R.string.cancel, "#666666", v1 -> mAttentionDialog.dismiss());
            mAttentionDialog.show();
        }
    }

    /**
     * 模块的功能
     *
     * @param v
     */
    private void toClickModule(View v) {
        switch (v.getId()) {
            case R.id.meeting_detail_player_layout: {
                if (!mNoPPT) {
                    showToast("会议没有设置PPT");
                }
            }
            case R.id.meeting_detail_video_see: {
                getCourseInfo();
            }
            break;

            case R.id.meet_module_layout_exam: {
                getExamInfo();
            }
            break;

            case R.id.meet_module_layout_que: {
                getQueInfo();
            }
            break;

            case R.id.meeting_detail_layout_video: {
                getVideoInfo();
            }
            break;

            case R.id.meeting_detail_layout_sign: {
                if (checkPermission(0, Permission.location, Permission.phone, Permission.storage)) {
                    getSignInfo();
                }
            }
            break;
        }
    }

    // FIXME: 2017/6/12 除了视频列表进入要先请求

    /**
     * PPT
     */
    private void getCourseInfo() {
        refresh(RefreshWay.dialog);
        exeNetworkReq(KIdCourse, NetFactory.toPPT(mMeetId, mMapList.getByKey(FunctionType.ppt)));
    }

    /**
     * 考试
     */
    private void getExamInfo() {
        refresh(RefreshWay.dialog);
        exeNetworkReq(KIdExam, NetFactory.toExam(mMeetId, mMapList.getByKey(FunctionType.exam)));
    }

    /**
     * 问卷
     */
    private void getQueInfo() {
        refresh(RefreshWay.dialog);
        exeNetworkReq(KIdQue, NetFactory.toSurvey(mMeetId, mMapList.getByKey(FunctionType.que)));
    }

    /**
     * 视频
     */
    private void getVideoInfo() {
        Submit submit = new Submit();
        submit.put(TSubmit.meetId, mMeetId);
        submit.put(TSubmit.moduleId, mMapList.getByKey(FunctionType.video));
        mStartModuleTime = System.currentTimeMillis();
        VideoCategoryActivity.nav(MeetingDetailsActivity.this, submit, null);
    }

    /**
     * 签到
     */
    private void getSignInfo() {
        refresh(RefreshWay.dialog);
        Location.inst().start();
        mObserver = (isSuccess, gps) -> {
            if (isSuccess) {
                //定位成功
                mLatitude = gps.getString(TGps.latitude);
                mLongitude = gps.getString(TGps.longitude);
                YSLog.d(TAG, "Gps-Latitude:" + mLatitude);
                YSLog.d(TAG, "Gps-Longitude:" + mLongitude);
                exeNetworkReq(KIdSign, NetFactory.toSign(mMeetId, mMapList.getByKey(FunctionType.sign)));
            } else {
                runOnUIThread(() -> {
                    stopRefresh();
                    //定位失败
                    YSLog.d(TAG, "Gps:失败");
                    initDialog();
                    mLocationDialog.show();
                });
            }
            LocationNotifier.inst().remove(mObserver);
            Location.inst().onDestroy();
        };
        LocationNotifier.inst().add(mObserver);
    }

    @Override
    public Object onNetworkResponse(int id, NetworkResp r) throws Exception {
        if (id == KIdMeetDetail) {
            return JsonParser.ev(r.getText(), MeetDetail.class);
        } else if (id == KIdSign) {
            return JsonParser.ev(r.getText(), Sign.class);
        } else if (id == KIdQue) {
            return JsonParser.ev(r.getText(), Intro.class);
        } else if (id == KIdCourse) {
            return JsonParser.ev(r.getText(), PPT.class);
        } else {
            return JsonParser.error(r.getText());
        }
    }

    @Override
    public void onNetworkSuccess(int id, Object result) {
        switch (id) {
            case KIdMeetDetail: {
                Result<MeetDetail> r = (Result<MeetDetail>) result;
                if (r.isSucceed()) {
                    mMeetDetail = r.getData();
                    refreshViews(mMeetDetail);
                    setViewState(ViewState.normal);
                } else {
                    onNetworkError(id, new NetError(id, r.getError()));
                }
            }
            break;

            case KIdExam: {
                stopRefresh();
                Result r = (Result) result;
                if (r.isSucceed()) {
                    mStartModuleTime = System.currentTimeMillis();
                    ExamIntroActivity.nav(MeetingDetailsActivity.this,
                            mMeetId, mMapList.getByKey(FunctionType.exam),
                            mMeetDetail.getString(TMeetDetail.organizer));
                } else {
                    showToast(r.getError());
                }
            }
            break;

            case KIdQue: {
                stopRefresh();
                Result r = (Result) result;
                if (r.isSucceed()) {
                    mStartModuleTime = System.currentTimeMillis();
                    QueTopicActivity.nav(MeetingDetailsActivity.this, mMeetId, mMapList.getByKey(FunctionType.que));
                } else {
                    showToast(r.getError());
                }
            }
            break;

            case KIdVideo: {
            }
            break;

            case KIdSign: {
                stopRefresh();
                Result<Sign> r = (Result<Sign>) result;
                if (r.isSucceed()) {
                    Sign signData = r.getData();
                    // 判断是否已签到
                    if (signData.getBoolean(TSign.finished)) {
                        // 已签到直接显示结果
                        showToast("已签到");
                    } else {
                        // 未签到跳转再请求签到
                        mStartModuleTime = System.currentTimeMillis();
                        Intent i = new Intent(MeetingDetailsActivity.this, SignActivity.class)
                                .putExtra(Extra.KMeetId, mMeetId)
                                .putExtra(Extra.KModuleId, mMapList.getByKey(FunctionType.sign))
                                .putExtra(Extra.KData, signData.getString(TSign.id))
                                .putExtra(Extra.KLatitude, mLatitude)
                                .putExtra(Extra.KLongitude, mLongitude);
                        startActivity(i);
                    }
                } else {
                    showToast(r.getError());
                }
            }
            break;

            case KIdCourse: {
                stopRefresh();
                Result<PPT> r = (Result<PPT>) result;
                if (r.isSucceed()) {
                    PPT ppt = r.getData();
                    CourseInfo courseInfo = ppt.getEv(TPPT.course);
                    if (courseInfo != null) {
                        mStartModuleTime = System.currentTimeMillis();
                        MeetingCourseActivity.nav(MeetingDetailsActivity.this, mMeetId, mMapList.getByKey(FunctionType.ppt));
                    } else {
                        showToast("没有PPT");
                    }
                } else {
                    showToast(r.getError());
                }
            }
            break;

            case KIdAttention: {
                stopRefresh();
                Result r = (Result) result;
                if (r.isSucceed()) {
                    showToast("关注成功");
                    mMeetDetail.put(TMeetDetail.attention, true);
                    notify(NotifyType.unit_num_attention_change,
                            new AttentionUnitNum(mMeetDetail.getInt(TMeetDetail.pubUserId), Attention.yes));
                } else {
                    showToast("关注失败");
                }
                if (mAttentionDialog != null) {
                    mAttentionDialog.dismiss();
                }
            }
            break;
        }
    }

    @Override
    public void onNetworkError(int id, NetError error) {
        super.onNetworkError(id, error);

        if (id == KIdMeetDetail) {
            setViewState(ViewState.error);
        }
    }

    /**
     * 更新界面数据
     *
     * @param info
     */
    private void refreshViews(MeetDetail info) {
        mIvCollection.setSelected(info.getBoolean(TMeetDetail.stored));

        mIvPlay.placeHolder(R.mipmap.ic_default_meeting_content_detail)
                .url(info.getString(TMeetDetail.coverUrl))
                .load();
        long startTime = info.getLong(TMeetDetail.startTime); // 开始时间
        mTvDate.setText(TimeUtil.formatMilli(startTime, TimeFormat.from_y_to_m_24));
        mTvDuration.setText("时长:" + Time.milliFormat(info.getLong(TMeetDetail.endTime) - startTime));

        // 会议
        mTvTitle.setText(info.getString(TMeetDetail.meetName));
        @EpnType int epnType = info.getInt(TMeetDetail.eduCredits);
        mEpnType = epnType; // 需要还是奖励
        mEpn = info.getInt(TMeetDetail.xsCredits);
        if (mEpn == 0) {
            // 不奖励也不支付算免费, 隐藏
            goneView(mTvEpn);
        } else {
            if (needPay(mEpnType)) {
                mTvEpn.setText("本次会议需支付象数 : " + mEpn + "个");
            } else {
                mTvEpn.setText("本次会议奖励象数 : " + mEpn + "个,还有" + info.getString(TMeetDetail.remainAward) + "人可领取");
            }
        }
        mTvSection.setText(info.getString(TMeetDetail.meetType));

        // 单位号
        mIvNumber.placeHolder(R.mipmap.ic_default_unit_num_large)
                .url(info.getString(TMeetDetail.headimg))
                .renderer(new CircleRenderer())
                .load();
        mTvUnitNum.setText(info.getString(TMeetDetail.organizer));

        // 资料
        int fileNum = info.getInt(TMeetDetail.materialCount);
        if (fileNum > 3) {
            showView(mIvFileArrow);
            mTvFileNum.setText("查看全部" + fileNum + "个文件");
            mLayoutData.setOnClickListener(v -> FileDataActivity.nav(MeetingDetailsActivity.this, mMeetId, Extra.KMeetingType));
        }
        List<FileData> materials = info.getList(TMeetDetail.materials);
        if (materials == null || materials.size() == 0) {
            goneView(mDivider);
            goneView(mLayoutData);
            goneView(mDividerFile);
        } else {
            UISetter.setFileData(mLayoutFile, materials, info.getInt(TMeetDetail.pubUserId));
        }

        // 主讲者
        mTvGN.setText(info.getString(TMeetDetail.lecturer));
        mIvGP.placeHolder(R.mipmap.ic_default_meeting_guest)
                .url(info.getString(TMeetDetail.lecturerHead))
                .load();
        // 职责和医院没有的话就隐藏
        UISetter.viewVisibility(info.getString(TMeetDetail.lecturerTitle), mTvGP); // 职责
        UISetter.viewVisibility(info.getString(TMeetDetail.lecturerHos), mTvGH); // 医院

        mTvIntro.setText(Html.fromHtml(info.getString(TMeetDetail.introduction)));

        // 模块处理
        modules(info.getList(TMeetDetail.modules));
    }

    /**
     * 处理模块
     *
     * @param modules
     */
    private void modules(List<Module> modules) {
        int type;
        Module module;
        mMapList = new MapList<>();

        for (int i = 0; i < modules.size(); i++) {
            module = modules.get(i);
            type = module.getInt(TModule.functionId);
            mMapList.add(Integer.valueOf(type), module.getString(TModule.id));
            switch (type) {
                case FunctionType.exam: {
                    // 有考试模块
                    setOnClickListener(mLayoutExam);
                    mLayoutExam.setImage(KExamResId).setTextColor(KTextColor);
                }
                break;

                case FunctionType.que: {
                    // 有问卷模块
                    setOnClickListener(mLayoutQue);
                    mLayoutQue.setImage(KQueResId).setTextColor(KTextColor);
                }
                break;

                case FunctionType.video: {
                    // 有视频模块
                    setOnClickListener(mLayoutVideo);
                    mLayoutVideo.setImage(KVideoResId).setTextColor(KTextColor);
                }
                break;

                case FunctionType.sign: {
                    // 有签到模块
                    setOnClickListener(mLayoutSign);
                    mLayoutSign.setImage(KSignResId).setTextColor(KTextColor);

                }
                break;

                case FunctionType.ppt: {
                    // 有微课模块
                    mNoPPT = true;
                    setOnClickListener(mTvSee);
                    setOnClickListener(R.id.meeting_detail_player_layout);
                    showView(findView(R.id.meeting_detail_iv_play_course)); // 有PPT的时候显示
                    mTvSee.setBackgroundResource(R.drawable.meet_detail_see_bg_selector);
                }
                break;
            }
        }
    }

    @Override
    public void onPermissionResult(int code, @PermissionResult int result) {
        switch (result) {
            case PermissionResult.granted: {
                getSignInfo();
            }
            break;
            case PermissionResult.denied:
            case PermissionResult.never_ask: {
                initDialog();
                mLocationDialog.show();
            }
            break;
        }
    }

    /**
     * 初始化Dialog
     */
    private void initDialog() {
        mLocationDialog = new LocationDialog(MeetingDetailsActivity.this);
        mLocationDialog.setLocationListener(v -> {
            if (checkPermission(0, Permission.location, Permission.phone, Permission.storage)) {
                getSignInfo();
            }
        });
    }

    /**
     * 获取收藏按钮
     *
     * @param layout
     */
    private void getCollection(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View childView = layout.getChildAt(i);
            if (childView instanceof ViewGroup) {
                getCollection((ViewGroup) childView);
            } else if (childView instanceof ImageView) {
                mIvCollection = (ImageView) childView;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 防止Dialog泄露
        if (mLocationDialog != null) {
            mLocationDialog.dismiss();
        }
        if (mPayEpnDialog != null) {
            mPayEpnDialog.dismiss();
        }
        if (mShareDialog != null) {
            mShareDialog.dismiss();
        }
        if (mAttentionDialog != null) {
            mAttentionDialog.dismiss();
        }
        // 开启服务提交会议学习时间
        Intent intent = new Intent(this, CommonServ.class)
                .putExtra(Extra.KType, ReqType.meet)
                .putExtra(Extra.KMeetId, mMeetId)
                .putExtra(Extra.KData, mMeetTime / TimeUnit.SECONDS.toMillis(1));
        startService(intent);
    }

    @Override
    public void onNotify(@NotifyType int type, Object data) {
        if (type == NotifyType.study) {
            mMeetTime += System.currentTimeMillis() - mStartModuleTime;
            YSLog.d(TAG, "onNotify:" + mMeetTime);
        }
    }
}