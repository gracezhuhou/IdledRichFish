package com.sufe.idledrichfish.ui.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.sufe.idledrichfish.OrderInfoActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

import java.util.ArrayList;
import java.util.List;

//聊天界面
public class ChatActivity extends AppCompatActivity implements EMMessageListener {

    private String productId;
    private String sellerId;
    private String sellerName;
    private String orderId;
    static public Handler productHandler;
    static public Handler orderHandler;

    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private Button mSendBtn;
    // 显示内容的 TextView
    private TextView mContentText;

    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private EMConversation mConversation;
    private RecyclerView mRecyclerView;
    private List mList = new ArrayList();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mMessageListener = this;
        initView();
        initConversation();
        initData(); // 获取intent传入数据
        setToolbar(); // 设置Toorbar标题&返回键
        setHandler(); // 创建订单返，回Bmob数据

        final Button button_buy = findViewById(R.id.button_buy);
        button_buy.setOnClickListener(view -> buy()); // 创建订单
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mInputEdit = (EditText) findViewById(R.id.ec_edit_message_input);
        mSendBtn = (Button) findViewById(R.id.ec_btn_send);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
//                  环信部分的发送消息
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    mList.add(msg);
                    adapter = new MsgAdapter((List<Msg>) mList);
                    mRecyclerView.setAdapter(adapter);
                    //当有新消息时，刷新RecyclerView中的显示
                    adapter.notifyItemInserted(mList.size() - 1);
                    //使用RecyclerView加载新聊天页面
                    mRecyclerView.scrollToPosition(mList.size() - 1);
                    mInputEdit.setText("");
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on success");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {
        /*
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    // --------------------------------- Message Listener -------------------------------------
    // 环信消息监听主要方法

    /**
     * 收到新消息
     * @param messages 收到的新消息集合
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息
        String result = messages.get(0).getBody().toString();
        String msgReceived = result.substring(5, result.length() - 1);
//        bt_name.setText(messages.get(0).);
        final Msg msg = new Msg(msgReceived, Msg.TYPE_RECEIVED);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.add(msg);
                adapter = new MsgAdapter((List<Msg>) mList);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.scrollToPosition(mList.size() - 1);
            }
        });
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
            Log.i("lzan13", "收到 CMD 透传消息" + body.action());
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
     * 获取intent传入的数
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        sellerId = intent.getStringExtra("seller_id_extra");
        sellerName = intent.getStringExtra("seller_Name_extra");
        ProductRepository.getInstance(new ProductDataSource()).queryProduct(productId, "chat");
    }

    /**
     * 获取商品数据 & 获取订单创建数据
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        final TextView text_product_name = findViewById(R.id.text_product_name);
        final TextView text_price = findViewById(R.id.text_price);
        final ImageView image_product = findViewById(R.id.image_product);
        productHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 初始化界面
                    text_product_name.setText(b.getString("name"));
                    text_price.setText(String.valueOf(b.getDouble("price")));
                    Glide.with(getApplicationContext()).load(b.getByteArray("image")).into(image_product);
                    Log.i("Handler", "Query Product");
                } else {
                    // 9016 网络问题
                }
            }
        };

        orderHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    orderId = b.getString("orderId");
                    Log.i("Handler", "Save Order");
                    // 跳转至订单详细界面
                    Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
                    intent.putExtra("product_id_extra", productId);
                    intent.putExtra("seller_id_extra", sellerId);
                    intent.putExtra("seller_name_extra", sellerName);
                    intent.putExtra("order_id_extra", orderId);
                    startActivity(intent);
                } else {
                    // 9016 网络问题
                }
            }
        };
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(sellerName);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * 点击购买
     * 跳转至订单界面
     */
    private void buy() {
        AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this)
                .setTitle("确定创建订单？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        OrderRepository.getInstance(new OrderDataSource()).saveOrder(sellerId, productId);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }
}