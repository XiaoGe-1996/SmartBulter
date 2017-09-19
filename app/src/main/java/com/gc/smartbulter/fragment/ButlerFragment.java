package com.gc.smartbulter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gc.smartbulter.R;
import com.gc.smartbulter.adapter.ChatAdapter;
import com.gc.smartbulter.entity.ChatListData;
import com.gc.smartbulter.utils.ShareUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.gc.smartbulter.view.CustomLinearLayoutManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.fragment
 * 文件名  ：ButlerFragment
 * 创建者  ：GC
 * 创建时间：2017/8/7 14:35
 * 描述    ：语音机器人
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {

    private EditText et_text;
    private Button btn_send;
    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private List<ChatListData> mList = new ArrayList<>();
    //TTS
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {

        /**
         * 语音合成
         */
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");


        et_text= (EditText) view.findViewById(R.id.et_text);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mChatRecyclerView);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(getContext(), mList);
        mRecyclerView.setAdapter(mChatAdapter);

        addLeftItem("你好！");
    }

    private void addLeftItem(String s) {
        boolean isSpeak = ShareUtils.getBoolean(getContext(), "isSpeak", false);
        if(isSpeak){
            //开始说话
            startSpeak(s);
        }

        ChatListData date = new ChatListData();
        date.setType(ChatAdapter.VALUE_LEFT_TEXT);
        date.setText(s);
        mList.add(date);
        //通知adapter刷新
        mChatAdapter.notifyDataSetChanged();
        //滚动到底部
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getBottom());
    }

    private void addRightItem(String s) {
        ChatListData date = new ChatListData();
        date.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        date.setText(s);
        mList.add(date);
        //通知adapter刷新
        mChatAdapter.notifyDataSetChanged();
        //滚动到底部
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getBottom());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                String text = et_text.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    if(text.length()>30){
                        UtilTools.showShrotToast(getContext(),"输入内容不能超过30个字符");
                    }else {
                        //获取数据后清空输入框
                        et_text.setText("");
                        addRightItem(text);
                        //发送请求输入给机器人
                        String url = "http://op.juhe.cn/robot/index?info=" + text
                                + "&key=" + StaticClass.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingJson(t);
                            }
                        });
                    }
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObhect = new JSONObject(t);
            JSONObject jsonresult = jsonObhect.getJSONObject("result");
            //拿到返回值
            String text = jsonresult.getString("text");
            //7.拿到机器人的返回值之后添加在left item
            addLeftItem(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startSpeak(String text){
        //开始讲话
        mTts.startSpeaking( text, mSynListener );
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
