package edu.wj.sport.android.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.bumptech.glide.Glide;

import edu.wj.sport.android.common.BaseFragment;
import edu.wj.sport.android.databinding.FragmentMineBinding;
import edu.wj.sport.android.ui.EditInfoActivity;
import edu.wj.sport.android.ui.LoginActivity;
import edu.wj.sport.android.ui.SettingActivity;
import edu.wj.sport.android.ui.UserInfoActivity;
import edu.wj.sport.android.utils.HttpUtils;
import edu.wj.sport.android.utils.UserDefault;

public class MineFragment extends BaseFragment<FragmentMineBinding> {
    @Override
    protected void onLoad() {


        mViewBinding.rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(UserInfoActivity.class);
            }
        });

        mViewBinding.tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("设置");
                start(SettingActivity.class);
            }
        });
        mViewBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("修改资料");
                start(EditInfoActivity.class);
            }
        });
        mViewBinding.tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("是否要退出登录？")
                        .setCancelable(false)
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserDefault.getInstance().clear();
                                start(LoginActivity.class);
                                requireActivity().onBackPressed();
                            }
                        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                alertDialog.show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        String avatarUrl = HttpUtils.RES_URL + "avatar/" + UserDefault.getInstance().getUserInfo().getAvatar();

        String nickName = UserDefault.getInstance().getUserInfo().getNickname();

        String phone = UserDefault.getInstance().getUserInfo().getPhone();


        Glide.with(this)
                .load(avatarUrl)
                .circleCrop()
                .into(mViewBinding.ivAvatar);

        mViewBinding.tvNickname.setText(nickName);
        mViewBinding.tvPhone.setText(phone);
    }
}
