package com.gc.smartbulter.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.MyUser;
import com.gc.smartbulter.ui.CourierActivity;
import com.gc.smartbulter.ui.LoginActivity;
import com.gc.smartbulter.ui.PhoneActivity;
import com.gc.smartbulter.utils.CameraUtil;
import com.gc.smartbulter.utils.L;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.gc.smartbulter.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class UserFragment extends Fragment implements View.OnClickListener {

    private Button btn_exit_user;
    private TextView edit_user;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;
    private Button btn_update_ok;
    private Button btn_update_no;
    private CircleImageView profile_image;
    private Dialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private TextView tv_courier;
    private TextView tv_phone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_user,null);

        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_desc = (EditText) view.findViewById(R.id.et_desc);
        btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        btn_update_no = (Button) view.findViewById(R.id.btn_update_no);
        btn_update_no.setOnClickListener(this);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        tv_courier = (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);



        //参数分别是   上下文，宽，高，内容布局，样式，居中显示，动画样式
        dialog = new CustomDialog(getActivity(),MATCH_PARENT,MATCH_PARENT,R.layout.dialog_photo,R.style.Theme_Dialog, Gravity.CENTER);
        //屏幕外点击无效
        dialog.setCancelable(false);

        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_picture=(Button) dialog.findViewById(R.id.btn_picture);
        btn_cancel =(Button) dialog.findViewById(R.id.btn_cancel);

        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //设置默认不可以输入
        setEnabled(false);
        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            et_username.setText(userInfo.getUsername());
            et_age.setText(userInfo.getAge()+"");
            et_sex.setText(userInfo.isSex()?"男":"女");
            et_desc.setText(userInfo.getDesc());
        }


    }
    //控制是否可以显示
    private void setEnabled(boolean is){

        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_user:
                //退出登录
                MyUser.logOut();
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                //设置显示
                setEnabled(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                btn_update_no.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //拿到输入框的值
                String username = et_username.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String sex = et_sex.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(username) &!TextUtils.isEmpty(age) &!TextUtils.isEmpty(sex) ){
                    //3.更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if(sex.equals("男")){
                        user.setSex(true);
                    }else {
                        user.setSex(false);
                    }
                    if(!TextUtils.isEmpty(desc)){
                        user.setDesc(desc);
                    }else{
                        user.setDesc("这个人很懒，什么都没留下！");
                    }

                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                //修改成功
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                UtilTools.showShrotToast(getContext(),"修改成功");
                            }else {
                                UtilTools.showShrotToast(getContext(),"修改失败");
                            }
                        }
                    });

                }else {
                    UtilTools.showShrotToast(getContext(),"输入框不能为空");
                }
                break;
            case R.id.btn_update_no:
                setEnabled(false);
                btn_update_ok.setVisibility(View.GONE);
                btn_update_no.setVisibility(View.GONE);
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                //相机
                toCamera();
                break;
            case R.id.btn_picture:
                //相册
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(),CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,StaticClass.PICTURE_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相机
    private void toCamera() {

//        CameraUtil.openCamera(getActivity());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用,可以的话进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), StaticClass.PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,StaticClass.CAMERA_REQUEST_CODE);

        dialog.dismiss();
    }

    private File tempFile =null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( resultCode != getActivity().RESULT_CANCELED){
            switch (requestCode){
                //相册
                case StaticClass.PICTURE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机
                case StaticClass.CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),StaticClass.PHOTO_IMAGE_FILE_NAME);
                    L.e(Uri.fromFile(tempFile).toString());
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case StaticClass.RESULT_REQUEST_CODE:
                    //有可能舍弃
                    if(data != null){
                        //拿到图片设置
                        setImageToView(data);
                        //设置了图片，就删除原先的
                        if(tempFile!=null){
                            tempFile.delete();
                        }
                    }
                    break;

            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri){
        if(uri == null){
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop","true");
        //宽高
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪的质量
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);
        //发送数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,StaticClass.RESULT_REQUEST_CODE);
    }
    private void setImageToView(Intent data){
        Bundle bundle = data.getExtras();
        if(bundle != null){
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(getActivity(),profile_image);

    }
}
