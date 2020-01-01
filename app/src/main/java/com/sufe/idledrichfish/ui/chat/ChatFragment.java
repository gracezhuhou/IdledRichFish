package com.sufe.idledrichfish.ui.chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.sufe.idledrichfish.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements EMMessageListener{
    // Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    // Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    private RecyclerView recycler_view;
    private TextView text_no_chat;

    private Map<String, EMConversation> conversations;
    private List<ChatView> chatList = new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private EMMessageListener mMessageListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view for this fragment
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recycler_view = view.findViewById(R.id.recycler_view);
        text_no_chat = view.findViewById(R.id.text_no_chat);
        setRecycler();
        initConversation();

        return view;
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity..
     */
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    // MessageView Listener 环信消息监听主要方法
    /**
     * 收到新消息
     * @param messages 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        // 收到消息
        initConversation();
        Log.i("ChatFragment", "Message Received");
    }

    /**
     * 收到新的 CMD 消息
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("ChatFragment", "收到 CMD 透传消息" + body.action());
        }
    }

    /**
     * 收到新的已读回执
     *
     * @param list 收到消息已读回执
     */
    @Override
    public void onMessageRead(List<EMMessage> list) {
    }

    /**
     * 收到新的发送回执
     * TODO 无效 暂时有bug
     *
     * @param list 收到发送回执的消息集合
     */
    @Override
    public void onMessageDelivered(List<EMMessage> list) {
    }

    /**
     * 消息撤回回调
     *
     * @param list 撤回的消息列表
     */
    @Override
    public void onMessageRecalled(List<EMMessage> list) {
    }

    /**
     * 消息的状态改变
     *
     * @param message 发生改变的消息
     * @param object  包含改变的消息
     */
    @Override
    public void onMessageChanged(EMMessage message, Object object) {
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {
        // 获取所有会话
        conversations = EMClient.getInstance().chatManager().getAllConversations();
        chatList.clear();
        // 遍历
        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
            EMMessage message = entry.getValue().getLastMessage();
            if (message != null) {
                Date date = new Date(message.getMsgTime());
                ChatView chat = new ChatView(message.getUserName(), message.getBody().toString(), date.toString(), !message.isUnread());
                chatList.add(chat);
            }
        }
        chatRecyclerViewAdapter.notifyDataSetChanged(); // 更新列表
        if (chatList.size() != 0) {
            text_no_chat.setVisibility(View.GONE);
        }
    }

    private void setRecycler() {
        chatList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(layoutManager);
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(chatList);
        recycler_view.setAdapter(chatRecyclerViewAdapter);
        recycler_view.setHasFixedSize(true);
    }
}
