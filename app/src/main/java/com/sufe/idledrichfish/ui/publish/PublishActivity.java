package com.sufe.idledrichfish.ui.publish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.sufe.idledrichfish.GlideImageEngine;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.ui.myPublish.MyPublishActivity;
import com.sufe.idledrichfish.ui.tag.TagActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PublishActivity extends AppCompatActivity {

    private EditText text_description;
    private TextView text_price;
    private TextView text_category;
    private TextView text_tag;
    private EditText text_name;
    private Button button_publish;
    private LinearLayout layout_price;
    private LinearLayout layout_tag;
    private PriceDialog dialog_price;
    private CheckBox checkbox_is_new;
    private CheckBox checkbox_cannot_bargain;
    private NineGridView nine_grid_view;

    private double price = 0;
    private double oldPrice;
    private List<String> tags = new ArrayList<>();
    private ArrayList<ImageInfo> imagesInfo;
    private List<String> pathList;

    private PublishViewModel publishViewModel;
    private NineGridViewClickAdapter nineGridViewClickAdapter;
    private final int REQUEST_CODE_CHOOSE_PHOTO_ALBUM = 1;
    private final int REQUEST_CODE_GET_TAGS = 11;
    private final int REQUEST_CODE_GET_CATEGORY = 12;
    static public Handler publishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        initView();

        // todo: category 暂用例子
        text_category.setText("其他");

        NineGridView.setImageLoader(new GlideImageLoader());
        imagesInfo = new ArrayList<>();

        choosePhoto();

        setHandler();
        setPublishForm(); // 格式监听
        setClickListener(); // 按键监听

    }

    /**
     * 获取选择的照片 & 获取Tags & 获取类别
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO_ALBUM && resultCode == RESULT_OK) {
            // 获取选择的照片
            // 图片路径 根据requestCode
            pathList = Matisse.obtainPathResult(data);
            for (String path : pathList) {
//                String path = uri.getPath();
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(path);
                imageInfo.setThumbnailUrl(path);
                imagesInfo.add(imageInfo);
                Log.i("ImageByte", path);
                //System.out.println(_Uri.getPath());
            }
            nineGridViewClickAdapter = new NineGridViewClickAdapter(this, imagesInfo);
            nine_grid_view.setAdapter(nineGridViewClickAdapter);
        }
        else if (requestCode == REQUEST_CODE_GET_TAGS) {
            // 获取Tags
            if (data != null) {
                tags = data.getStringArrayListExtra("tags_extra");
                changeData();
                if (tags.size() > 1) {
                    String tagText = tags.get(0) + "...";
                    text_tag.setText(tagText);
                } else if (tags.size() == 1) {
                    text_tag.setText(tags.get(0));
                }
            }
        }
    }

    /**
     * 选择照片（多项）
     * Matisse
     */
    private void choosePhoto() {
        Matisse
                .from(this)
                // 选择图片  （ofAll选择视频和图片、ofVideo选择视频）
                .choose(MimeType.ofImage())
                // 是否只显示选择的类型的缩略图（就不会把所有图片视频都放在一起，而是需要什么展示什么）
                .showSingleMediaType(true)
                // 这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                // 有序选择图片 123456...
                .countable(true)
                // 最大选择数量为9
                .maxSelectable(9)
                // 选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                // 界面中缩略图的质量
                .thumbnailScale(0.8f)
                // 黑色主题 （蓝色主题 Matisse_Zhihu）
                .theme(R.style.Matisse_Dracula)
//                .addFilter(new GifSizeFilter())
                // Glide加载方式
                .imageEngine(new GlideImageEngine())
                //请求码
                .forResult(REQUEST_CODE_CHOOSE_PHOTO_ALBUM);
    }

    /**
     * 获取Bmob信息
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 商品添加成功与否
        publishHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {
                    clearData();
                    AlertDialog alertDialog = new AlertDialog.Builder(PublishActivity.this)
                            .setTitle("商品发布成功")
                            .setMessage("可至我的发布中查看")
                            .setIcon(R.drawable.ic_audit)
                            .setPositiveButton("前往我的发布", (dialogInterface, i) -> {
                                // 跳转至我的发布页面
                                Intent intent = new Intent(getApplicationContext(), MyPublishActivity.class);
                                startActivity(intent);
                            })
                            .create();
                    alertDialog.show();
                }
                else {
                    String fail = "失败原因:" + errorCode + msg.getData().getString("e");
                    AlertDialog alertDialog = new AlertDialog.Builder(PublishActivity.this)
                            .setTitle("商品发布失败")
                            .setMessage(fail)
                            .setIcon(R.drawable.ic_fail)
                            .create();
                    alertDialog.show();
                }
            }
        };
    }

    /**
     * 监听表单的变化
     */
    private void setPublishForm() {
        publishViewModel = ViewModelProviders.of(this, new PublishViewModelFactory())
                .get(PublishViewModel.class);

        publishViewModel.getPublishFormState().observe(this, publishFormState -> {
            if (publishFormState == null) {
                return;
            }
            button_publish.setEnabled(publishFormState.isDataValid());
            if (publishFormState.getProductNameError() != null) {
                text_name.setError(getString(publishFormState.getProductNameError()));
            }
            if (publishFormState.getDescriptionError() != null) {
                text_description.setError(getString(publishFormState.getDescriptionError()));
            }
            if (publishFormState.getPriceError() != null) {
                text_price.setError(getString(publishFormState.getPriceError()));
            } else {
                text_price.setError(null);
            }
            if (publishFormState.getTagsError() != null) {
                text_tag.setError(getString(publishFormState.getTagsError()));
            } else {
                text_tag.setError(null);
            }
            if (publishFormState.getCategoryError() != null) {
                text_category.setError(getString(publishFormState.getCategoryError()));
            } else {
                text_category.setError(null);
            }
            Log.i("PublishForm", "On Change:" + publishFormState.getTagsError());
        });

        // 监听输入文字的变化
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
        text_name.addTextChangedListener(afterTextChangedListener);
        text_description.addTextChangedListener(afterTextChangedListener);
    }

    /**
     * 表单按键监听
     */
    private void setClickListener() {
        // 选中or取消选中“全新”&“不讲价”
        checkbox_is_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_is_new.isChecked()) {
                    checkbox_is_new.setBackgroundColor(getResources().getColor(R.color.orange));
                } else {
                    checkbox_is_new.setBackgroundColor(getResources().getColor(R.color.banana));
                }
            }
        });

        checkbox_cannot_bargain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_cannot_bargain.isChecked()) {
                    checkbox_cannot_bargain.setBackgroundColor(getResources().getColor(R.color.orange));
                } else {
                    checkbox_cannot_bargain.setBackgroundColor(getResources().getColor(R.color.banana));
                }
            }
        });

        // 点击价格栏，弹出Dialog
        layout_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_price = new PriceDialog(v.getContext(), R.style.DialogStyle_Price);
                // 软键盘的按键监听
                dialog_price.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
                dialog_price.setOnDismissListener(dialogInterface -> {
                    if (!dialog_price.priceInput.getText().toString().equals("")) {
                        price = Double.valueOf(dialog_price.priceInput.getText().toString());
                        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00 "); // 保留小数点两位
                        text_price.setText(format.format(price));
                    }
                    if (!dialog_price.oldPriceInput.getText().toString().equals("")) {
                        oldPrice = Double.valueOf(dialog_price.oldPriceInput.getText().toString());
                    }
                    dialogInterface.dismiss();
                    changeData();
                });
                dialog_price.show();
            }
        });

        // 点击tag跳转
        layout_tag.setOnClickListener(v -> {
            // 跳转至tag页面
            Intent intent = new Intent(this, TagActivity.class);
            startActivityForResult(intent, REQUEST_CODE_GET_TAGS);
        });

        // 点击发布Button
        button_publish.setOnClickListener(view -> {
            publishViewModel.saveProduct(text_name.getText().toString(),
                    text_description.getText().toString(),
                    checkbox_is_new.isChecked(),
                    !checkbox_cannot_bargain.isChecked(),
                    price,
                    oldPrice,
                    text_category.getText().toString(),
                    pathList,
                    tags);
            button_publish.setBackgroundResource(R.drawable.card_banana);
        });

        // 点击取消，返回Home
        final Button button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(view -> finish());
    }

    /**
     * 改变输入数据publishViewModel状态
     */
    private void changeData() {
        publishViewModel.publishDataChanged(text_name.getText().toString(),
                text_description.getText().toString(), price, tags, text_category.getText().toString());
    }

    private void initView() {
        layout_price = findViewById(R.id.layout_price);
        layout_tag = findViewById(R.id.layout_tag);
        text_description = findViewById(R.id.editText_description);
        text_description.clearFocus();
        button_publish = findViewById(R.id.button_publish);
        text_name = findViewById(R.id.editText_name);
        checkbox_cannot_bargain = findViewById(R.id.if_bargain);
        checkbox_is_new = findViewById(R.id.if_new);
        text_price = findViewById(R.id.text_price_number);
        text_category = findViewById(R.id.text_category);
        text_tag = findViewById(R.id.text_tab);
        nine_grid_view = findViewById(R.id.nine_grid_view);
    }

    /**
     * 清除表单数据
     */
    private void clearData() {
        text_description.setText("");
        button_publish.setEnabled(false);
        text_name.setText("");
        checkbox_cannot_bargain.setChecked(false);
        checkbox_is_new.setChecked(false);
        text_price.setText("");
        text_category.setText("");
        text_tag.setText("");
        imagesInfo.clear();
        if (nineGridViewClickAdapter != null) {
            nineGridViewClickAdapter.notify();
        }
    }
}


/**
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


/**
 * 过滤GIF
 */
class GifSizeFilter extends Filter {

    private int mMinWidth;
    private int mMinHeight;
    private int mMaxSize;

    GifSizeFilter(int minWidth, int minHeight, int maxSizeInBytes) {
        mMinWidth = minWidth;
        mMinHeight = minHeight;
        mMaxSize = maxSizeInBytes;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return new HashSet<MimeType>() {{
            add(MimeType.GIF);
        }};
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;

        Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), item.getContentUri());
        if (size.x < mMinWidth || size.y < mMinHeight || item.size > mMaxSize) {
            return new IncapableCause(IncapableCause.TOAST, context.getString(R.string.error_file_type, mMinWidth,
                    String.valueOf(PhotoMetadataUtils.getSizeInMB(mMaxSize))));
        }
        return null;
    }
}
