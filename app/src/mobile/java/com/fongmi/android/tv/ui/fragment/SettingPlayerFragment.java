package com.fongmi.android.tv.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.databinding.FragmentSettingPlayerBinding;
import com.fongmi.android.tv.impl.UaCallback;
import com.fongmi.android.tv.player.ExoUtil;
import com.fongmi.android.tv.player.Players;
import com.fongmi.android.tv.ui.base.BaseFragment;
import com.fongmi.android.tv.ui.custom.dialog.UaDialog;
import com.fongmi.android.tv.utils.Prefers;
import com.fongmi.android.tv.utils.ResUtil;

public class SettingPlayerFragment extends BaseFragment implements UaCallback {

    private FragmentSettingPlayerBinding mBinding;
    private String[] http;

    public static SettingPlayerFragment newInstance() {
        return new SettingPlayerFragment();
    }

    private String getSwitch(boolean value) {
        return getString(value ? R.string.setting_on : R.string.setting_off);
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return mBinding = FragmentSettingPlayerBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        mBinding.uaText.setText(Prefers.getUa());
        mBinding.tunnelText.setText(getSwitch(Prefers.isTunnel()));
        mBinding.httpText.setText((http = ResUtil.getStringArray(R.array.select_exo_http))[Prefers.getHttp()]);
        setVisible();
    }

    @Override
    protected void initEvent() {
        mBinding.ua.setOnClickListener(this::onUa);
        mBinding.http.setOnClickListener(this::setHttp);
        mBinding.tunnel.setOnClickListener(this::setTunnel);
    }

    private void setVisible() {
        mBinding.http.setVisibility(Players.isExo(Prefers.getPlayer()) ? View.VISIBLE : View.GONE);
        mBinding.tunnel.setVisibility(Players.isExo(Prefers.getPlayer()) ? View.VISIBLE : View.GONE);
    }

    private void onUa(View view) {
        UaDialog.create(this).show();
    }

    private void setHttp(View view) {
        int index = Prefers.getHttp();
        Prefers.putHttp(index = index == http.length - 1 ? 0 : ++index);
        mBinding.httpText.setText(http[index]);
        ExoUtil.reset();
    }

    private void setTunnel(View view) {
        Prefers.putTunnel(!Prefers.isTunnel());
        mBinding.tunnelText.setText(getSwitch(Prefers.isTunnel()));
    }

    @Override
    public void setUa(String ua) {
        mBinding.uaText.setText(ua);
        Prefers.putUa(ua);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) setVisible();
    }
}
