package com.example.bsproperty.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ResultBean;
import com.example.bsproperty.utils.AuthService;
import com.example.bsproperty.utils.FaceDetect;
import com.example.bsproperty.utils.Identify;
import com.example.bsproperty.utils.LQRPhotoSelectUtils;
import com.example.bsproperty.view.ProgressDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class Main2Activity extends BaseActivity {

    @BindView(R.id.btn_select)
    Button btnSelect;
    @BindView(R.id.iv_show)
    ImageView ivShow;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.tv_face)
    TextView tvFace;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private Bitmap selectBitmap;
    private String path;
    private String mToken;
    private ProgressDialog dialog;
    private MyAdapter adapter;
    private ArrayList<ResultBean.Bean> mData = new ArrayList<>();
    private DecimalFormat decimalFormat;

    @Override
    protected void initView(Bundle savedInstanceState) {

        decimalFormat = new DecimalFormat("##.00");

        adapter = new MyAdapter(Main2Activity.this, R.layout.item_img, mData);
        rvList.setLayoutManager(new GridLayoutManager(Main2Activity.this, 2));
        rvList.setAdapter(adapter);
        dialog = new ProgressDialog(Main2Activity.this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mToken = AuthService.getAuth();
            }
        }).start();


        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                path = outputFile.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getAbsolutePath());
                selectBitmap = bitmap;
                ivShow.setImageBitmap(selectBitmap);
                tvFace.setText("");
                rvList.setVisibility(View.INVISIBLE);
                btnSelect.setText("开始检测");
            }
        }, false);
    }

    private void getToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = FaceDetect.detect(path, mToken);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    final int face = jsonObject.getInt("result_num");
                    if (face > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                tvFace.setText("检测到" + face + "张人脸");
                                btnSelect.setText("开始识别");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                tvFace.setText("未检测到人脸");
                                btnSelect.setText("选择图片");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                String txt = btnSelect.getText().toString();
                switch (txt) {
                    case "选择图片":
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                        builder.setItems(new String[]{
                                "拍照选择", "本地相册选择", "取消"
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        PermissionGen.with(Main2Activity.this)
                                                .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                                                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.CAMERA
                                                ).request();
                                        break;
                                    case 1:
                                        PermissionGen.needPermission(Main2Activity.this,
                                                LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                        );
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        }).show();
                        break;
                    case "开始检测":
                        dialog.show("开始检测");
                        getToken();
                        break;
                    case "开始识别":
                        dialog.show("开始识别");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String json2 = Identify.identify(path, mToken);
                                final ResultBean bean = new Gson().fromJson(json2, ResultBean.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        mData.clear();
                                        int sum = 0;
                                        for (ResultBean.Bean bean1 : bean.getResult()) {
                                            if (bean1.getScores()[0] > 80) {
                                                mData.add(bean1);
                                                sum++;
                                            }
                                        }
                                        if (sum > 0) {
                                            tvFace.setText("搜索到" + sum + "张相似图片");
                                            rvList.setVisibility(View.VISIBLE);
                                        } else {
                                            tvFace.setText("未搜索到相似图片");
                                            rvList.setVisibility(View.INVISIBLE);
                                        }
                                        adapter.notifyDataSetChanged(mData);

                                        btnSelect.setText("选择图片");
                                    }
                                });
                            }
                        }).start();
                        break;
                }

                break;
        }
    }


    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    private class MyAdapter extends BaseAdapter<ResultBean.Bean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ResultBean.Bean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ResultBean.Bean bean, int position) {
            int id = getResources().getIdentifier(bean.getUid(), "mipmap", getPackageName());
            ((ImageView) holder.getView(R.id.iv_img)).setImageResource(id);
            holder.setText(R.id.tv_sc, decimalFormat.format(bean.getScores()[0]) + "%");
        }
    }

}
