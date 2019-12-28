package com.sufe.idledrichfish.ui.publish;

import android.support.annotation.Nullable;

/**
 * Data validation state of the publishment form.
 */
public class PublishFormState {
    @Nullable
    private Integer productNameError;
    @Nullable
    private Integer descriptionError;
    @Nullable
    private Integer priceError;
    @Nullable
    private Integer labelError;
    @Nullable
    private Integer categoryError;
    private boolean isDataValid;

    PublishFormState(@Nullable Integer productNameError, @Nullable Integer descriptionError,
                     @Nullable Integer priceError, @Nullable Integer labelError,
                     @Nullable Integer categoryError) {
        this.productNameError = productNameError;
        this.descriptionError = descriptionError;
        this.priceError = priceError;
        this.labelError = labelError;
        this.categoryError = categoryError;
        this.isDataValid = false;
    }

    PublishFormState(boolean isDataValid) {
        this.productNameError = null;
        this.descriptionError = null;
        this.priceError = null;
        this.labelError = null;
        this.categoryError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getProductNameError() {
        return productNameError;
    }

    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }

    @Nullable
    public Integer getPriceError() {
        return priceError;
    }

    @Nullable
    public Integer getLabelError() {
        return labelError;
    }

    @Nullable
    public Integer getCategoryError() {
        return categoryError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}