package com.sufe.idledrichfish.ui.publish;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductRepository;

import java.util.List;

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

    void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                            double price, double oldPrice, BmobRelation labels, String category,
                            List<String> imagePath) {
        // can be launched in a separate asynchronous job
        productRepository.saveProduct(productName, description, isNew, canBargain, price,
                oldPrice, labels, category, imagePath);
    }

    void publishDataChanged(String productName, String description, double price,
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
        return productName != null && !productName.equals("");
    }

    private boolean isDescriptionValid(String description) {
        return description != null && !description.equals("");
    }

    private boolean isPriceValid(double price) {
        return price != 0.0;
    }

    private boolean isLabelsValid(BmobRelation labels) {
        return labels != null;
    }

    private boolean isCategoryValid(String category) {
        return category != null && !category.equals("");
    }
}
