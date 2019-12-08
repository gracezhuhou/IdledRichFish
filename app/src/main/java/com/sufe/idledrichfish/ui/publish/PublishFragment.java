package com.sufe.idledrichfish.ui.publish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sufe.idledrichfish.ui.myPublish.MyPublishActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.Tab;

import cn.bmob.v3.datatype.BmobRelation;

import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublishFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishFragment extends Fragment {
    // TO DO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    // TO DO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private EditText descriptionEditText;
    private EditText priceTextView;
    private EditText categoryTextView;
    private EditText tabTextView;
    private EditText nameEditText;
    private Button publishButton;
    private LinearLayout priceLayout;
    private PriceDialog priceDialog;
    private CheckBox isNewCheckBox;
    private CheckBox canNotBargainCheckBox;

    private double price;
    private double oldPrice;
    private BmobRelation tabs;

    private PublishViewModel publishViewModel;
    static public Handler publishmentHandler;

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment PublishFragment.
     */
    // TO DO: Rename and change types and number of parameters
    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
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
        // Inflate the dialog_price_publishment for this fragment
        final View view = inflater.inflate(R.layout.fragment_publish, container, false);

        publishViewModel = ViewModelProviders.of(this, new PublishViewModelFactory())
                .get(PublishViewModel.class);

        priceLayout = view.findViewById(R.id.layout_price);
        descriptionEditText = view.findViewById(R.id.editText_description);
        descriptionEditText.clearFocus();
        publishButton = view.findViewById(R.id.button_launch);
        nameEditText = view.findViewById(R.id.editText_name);
        canNotBargainCheckBox = view.findViewById(R.id.if_bargain);
        isNewCheckBox = view.findViewById(R.id.if_new);
        priceTextView = view.findViewById(R.id.text_price_number);
        categoryTextView = view.findViewById(R.id.text_category);
        tabTextView = view .findViewById(R.id.text_tab);

        // todo: labels和category 暂用例子
        categoryTextView.setText("其他");
        Tab tab = new Tab();
        tab.setObjectId("QOjbEEEF");
        tab.setName("高数");
        Tab tab2 = new Tab();
        tab2.setObjectId("u3ckAAAE");
        tab.setName("数学");
        tabs = new BmobRelation();
        tabs.add(tab);
        tabs.add(tab2);

        setHandler();
        setPublishForm();

        /*
         * 选中or取消选中“全新”&“不讲价”
         */
        isNewCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNewCheckBox.isChecked()) {
                    isNewCheckBox.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isNewCheckBox.setTextColor(Color.WHITE);
                }
                else {
                    isNewCheckBox.setBackgroundColor(getResources().getColor(R.color.transparent));
                    isNewCheckBox.setTextColor(Color.BLACK);
                }
            }
        });

        canNotBargainCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canNotBargainCheckBox.isChecked()) {
                    canNotBargainCheckBox.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    canNotBargainCheckBox.setTextColor(Color.WHITE);
                }
                else {
                    canNotBargainCheckBox.setBackgroundColor(getResources().getColor(R.color.transparent));
                    canNotBargainCheckBox.setTextColor(Color.BLACK);
                }
            }
        });

        /*
         * 点击价格栏，弹出Dialog
         */
        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceDialog = new PriceDialog(v.getContext(), R.style.DialogStyle_Price);
                // 软键盘的按键监听
                priceDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.i("KeyCode", keyCode + "");
//                        if (keyCode == KeyEvent.KEYCODE_ENTER)
//                                dialog.dismiss();
                        if (keyCode == KeyEvent.KEYCODE_BACK) // 返回键
                            dialog.dismiss();
                        return false;
                    }
                });
                // Dialog关闭(Dismiss)时的监听
                priceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (!priceDialog.priceInput.getText().toString().equals("")) {
                            priceTextView.setText(priceDialog.priceInput.getText().toString());
                            price = Double.valueOf(priceDialog.priceInput.getText().toString());
                        }
                        if (!priceDialog.oldPriceInput.getText().toString().equals("")) {
                            oldPrice = Double.valueOf(priceDialog.oldPriceInput.getText().toString());
                        }
                        dialogInterface.dismiss();

                        changeData();
                    }
                });
                priceDialog.show();
            }
        });

        /*
         * 点击发布Button
         */
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishViewModel.saveProduct(nameEditText.getText().toString(),
                        descriptionEditText.getText().toString(),
                        isNewCheckBox.isChecked(),
                        !canNotBargainCheckBox.isChecked(),
                        price,
                        oldPrice,
                        tabs,
                        categoryTextView.getText().toString());
            }
        });

        return view;
    }

    // TO DO: Rename method, update argument and hook method into UI event
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
     * activity.
     */
    public static interface OnFragmentInteractionListener {
        // TO DO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
     * 获取Bmob信息
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取Bmob返回的注册ErrorCode
        publishmentHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("商品发布成功")
                            .setMessage("可至我的发布中查看")
                            .setIcon(R.drawable.ic_audit)
                            .setPositiveButton("前往我的发布", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // 跳转至我的发布页面
                                        Intent intent = new Intent(getApplicationContext(), MyPublishActivity.class);
                                        startActivity(intent);
                                    }
                            })
                            .create();
                    alertDialog.show();
                }
                else {
                    String fail = "失败原因:" + errorCode + msg.getData().getString("e");

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("商品发布失败")
                            .setMessage(fail)
                            .setIcon(R.drawable.ic_fail)
                            .create();
//                    if (e.getErrorCode() == 202)
//                        alertDialog.setMessage("");
//                    else if (e.getErrorCode() == 301)
//                        alertDialog.setMessage("");
                    alertDialog.show();
                }
            }
        };
    }

    /*
     * 监听输入数据的Form
     */
    private void setPublishForm() {
        publishViewModel.getPublishFormState().observe(this, new Observer<PublishFormState>() {
            @Override
            public void onChanged(@Nullable PublishFormState publishFormState) {
                if (publishFormState == null) {
                    return;
                }
                publishButton.setEnabled(publishFormState.isDataValid());
                if (publishFormState.getProductNameError() != null) {
                    nameEditText.setError(getString(publishFormState.getProductNameError()));
                }
                if (publishFormState.getDescriptionError() != null) {
                    descriptionEditText.setError(getString(publishFormState.getDescriptionError()));
                }
                if (publishFormState.getPriceError() != null) {
                    priceTextView.setError(getString(publishFormState.getPriceError()));
                }
                if (publishFormState.getCategoryError() != null) {
                    categoryTextView.setError(getString(publishFormState.getCategoryError()));
                }
                if (publishFormState.getLabelError() != null) {
                    tabTextView.setError(getString(publishFormState.getLabelError()));
                }
            }
        });

        /*
         * 监听输入文字的变化
         */
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeData();
            }
        };
        nameEditText.addTextChangedListener(afterTextChangedListener);
        descriptionEditText.addTextChangedListener(afterTextChangedListener);
    }

    /*
     * 改变输入数据publishViewModel状态
     */
    private void changeData() {
        publishViewModel.publishDataChanged(nameEditText.getText().toString(),
                descriptionEditText.getText().toString(), price, tabs, categoryTextView.getText().toString());
    }

}


/*
 * 自定义弹出的价格Dialog
 */
class PriceDialog extends Dialog {
    EditText priceInput;
    EditText oldPriceInput;

    public PriceDialog(@NonNull Context context) {
        this(context, 0);
    }

    public PriceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_price_publishment);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        priceInput = findViewById(R.id.editText_price);
        oldPriceInput = findViewById(R.id.editText_oldPrice);
    }
}