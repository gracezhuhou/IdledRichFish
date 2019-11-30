package com.sufe.idledrichfish;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
    private RelativeLayout layout_price;
    private PriceDialog priceDialog;

    private double price;
    private double oldPrice;

    public PublishmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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

        layout_price = view.findViewById(R.id.item_price);
        editText_description = view.findViewById(R.id.editText_description);
        editText_description.clearFocus();

        /*
        点击价格栏的响应
         */
        layout_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceDialog = new PriceDialog(v.getContext(), R.style.DialogStyle_Price);
                priceDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_ENTER)
//                                dialog.dismiss();
                        return false;
                    }
                });
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