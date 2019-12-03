package com.sufe.idledrichfish.ui.publish;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductRepository;

import cn.bmob.v3.datatype.BmobRelation;

public class PublishViewModel extends ViewModel {
    private MutableLiveData<PublishFormState> publishFormState = new MutableLiveData<>();
    private ProductRepository productRepository;

    PublishViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    LiveData<PublishFormState> getPublishFormState() {
        return publishFormState;
    }

    public void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                                double price, double oldPrice, BmobRelation labels, String category) {
        // can be launched in a separate asynchronous job
        productRepository.saveProduct(productName, description, isNew, canBargain, price,
                oldPrice, labels, category);

//        if (result instanceof Result.Success) {
//            BmobStudent data = ((Result.Success<BmobStudent>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
    }

    public void publishDataChanged(String productName, String description, double price,
                                   BmobRelation labels, String category) {
        if (!(isProductNameValid(productName))) {
            publishFormState.setValue(new PublishFormState(R.string.invalid_product_name, null,
                    null, null, null));
        } else if (!isDescriptionValid(description)) {
            publishFormState.setValue(new PublishFormState(null, R.string.invalid_description,
                    null, null, null));
        } else if (!isPriceValid(price)) {
                publishFormState.setValue(new PublishFormState(null, null,
                        R.string.invalid_price, null, null));
        } else if (!isLabelsValid(labels)) {
            publishFormState.setValue(new PublishFormState(null, null,
                    null, R.string.invalid_label, null));
        } else if (!isCategoryValid(category)) {
            publishFormState.setValue(new PublishFormState(null, null,
                    null, null, R.string.invalid_category));
        } else {
            publishFormState.setValue(new PublishFormState(true));
        }
    }

    // validation check
    private boolean isProductNameValid(String productName) {
        return productName != null;
    }

    private boolean isDescriptionValid(String description) {
        return description != null;
    }

    private boolean isPriceValid(double price) {
        return price != 0;
    }
    private boolean isLabelsValid(BmobRelation labels) {
        return labels != null;
    }
    private boolean isCategoryValid(String category) {
        return category != null;
    }
}
