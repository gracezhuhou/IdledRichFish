package com.sufe.idledrichfish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sufe.idledrichfish.database.Label;
import com.sufe.idledrichfish.database.LabelBLL;
import com.sufe.idledrichfish.database.Product;
import com.sufe.idledrichfish.database.ProductBLL;
import com.sufe.idledrichfish.database.StudentBLL;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublishmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublishmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishmentFragment extends Fragment {
    // TO DO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    // TO DO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private EditText editText_description;
    private EditText editText_price;
    private EditText editText_name;
    private Button button_publish;
    private RelativeLayout layout_price;
    private PriceDialog priceDialog;
    private CheckBox checkBox_isNew;
    private CheckBox checkBox_canNotBargain;

    private ProductBLL productBLL;
    private LabelBLL labelBLL;
    private StudentBLL studentBLL;
    private double price = 0.0;
    private double oldPrice = 0.0;
    private List<Label> labels;
    private String catogory = "无";

    public PublishmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment PublishmentFragment.
     */
    // TO DO: Rename and change types and number of parameters
    public static PublishmentFragment newInstance(String param1, String param2) {
        PublishmentFragment fragment = new PublishmentFragment();
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
        final View view = inflater.inflate(R.layout.fragment_publishment, container, false);

        layout_price = view.findViewById(R.id.layout_price);
        editText_description = view.findViewById(R.id.editText_description);
        editText_description.clearFocus();
        button_publish = view.findViewById(R.id.button_launch);
        editText_name = view.findViewById(R.id.editText_name);
        checkBox_canNotBargain = view.findViewById(R.id.if_bargain);
        checkBox_isNew = view.findViewById(R.id.if_new);

        productBLL = new ProductBLL();
        labelBLL = new LabelBLL();
        studentBLL = new StudentBLL();
        labels = new ArrayList<>();
        Label label = labelBLL.getLabelByName("高数");
        labels.add(label); //  例子
        catogory = "书籍"; //  例子

        /*
         * 选中or取消选中“全新”&“不讲价”
         */
        checkBox_isNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox_isNew.isChecked()) {
                    checkBox_isNew.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    checkBox_isNew.setTextColor(Color.WHITE);
                }
                else {
                    checkBox_isNew.setBackgroundColor(getResources().getColor(R.color.transparent));
                    checkBox_isNew.setTextColor(Color.BLACK);
                }
            }
        });

        checkBox_canNotBargain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox_canNotBargain.isChecked()) {
                    checkBox_canNotBargain.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    checkBox_canNotBargain.setTextColor(Color.WHITE);
                }
                else {
                    checkBox_canNotBargain.setBackgroundColor(getResources().getColor(R.color.transparent));
                    checkBox_canNotBargain.setTextColor(Color.BLACK);
                }
            }
        });

        /*
         * 点击价格栏，弹出Dialog
         */
        layout_price.setOnClickListener(new View.OnClickListener() {
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
                        TextView showPrice = view.findViewById(R.id.show_price);

                        if (!priceDialog.price_input.getText().toString().equals("")) {
                            price = Double.valueOf(priceDialog.price_input.getText().toString());
                            String priceText = "¥ " + price;
                            showPrice.setText(priceText);
                        }
                        if (!priceDialog.old_price_input.getText().toString().equals(""))
                            oldPrice = Double.valueOf(priceDialog.old_price_input.getText().toString());

                        dialogInterface.dismiss();
                    }
                });
                priceDialog.show();
            }
        });

        /*
         * 点击发布
         * TODO: 图片
         */
        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setName(editText_name.getText().toString());
                product.setDescription(editText_description.getText().toString());
                product.setNew(checkBox_isNew.isChecked());
                product.setCanBargain(!checkBox_canNotBargain.isChecked());
                product.setPrice(price);
                product.setOldPrice(oldPrice);
                product.setLabels(labels);
                product.setCategory(catogory);
                product.setPublisherId("2017110001"); //Todo:获取当前用户ID
                if (productBLL.insertProduct(product)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("商品发布成功") // 标题
//                            .setMessage("这是内容") // 内容
                            .setIcon(R.mipmap.ic_launcher) //图标
                            .create();
                    alertDialog.show();
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("商品发布失败") // 标题
                            .setMessage("哦噢~商品发布失败了，请稍后重新尝试") // 内容
                            .setIcon(R.mipmap.ic_launcher) //图标
                            .create();
                    alertDialog.show();
                }
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
}


/*
 * 自定义弹出的价格Dialog
 */
class PriceDialog extends Dialog {
    EditText price_input;
    EditText old_price_input;

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
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        price_input = findViewById(R.id.editText_price);
        old_price_input = findViewById(R.id.editText_oldPrice);
    }
}