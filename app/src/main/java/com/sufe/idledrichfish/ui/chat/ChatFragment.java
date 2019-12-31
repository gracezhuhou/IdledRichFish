package com.sufe.idledrichfish.ui.chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.sufe.idledrichfish.R;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    // Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    private RecyclerView recycler_view;

    private List<ChatView> chatList = new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private EMMessageListener msgListener;

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
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recycler_view = view.findViewById(R.id.recycler_view);
        setRecycler();
        initConversation();

        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                // 收到消息
                for (EMMessage message: messages) {
                    String id = message.getUserName();
                    chatList.add(new ChatView(id, message.getBody().toString(),
                            new Date(message.getMsgTime()).toString(), true));
                }
                chatRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                // 收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                // 收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                // 收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                // 消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                // 消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);

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

    private void setRecycler() {
        chatList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(layoutManager);
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(chatList);
        recycler_view.setAdapter(chatRecyclerViewAdapter);
        recycler_view.setHasFixedSize(true);
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {
        // 获取所有会话
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 遍历
        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
            EMMessage message = entry.getValue().getLastMessage();
            if (message != null) {
                Date date = new Date(message.getMsgTime());
                ChatView chat = new ChatView(entry.getKey(), message.getBody().toString(), date.toString(), !message.isUnread());
                chatList.add(chat);
            }
        }
        chatRecyclerViewAdapter.notifyDataSetChanged(); // 更新列表
    }
}
