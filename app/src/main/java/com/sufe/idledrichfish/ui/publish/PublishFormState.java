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
    private Integer tagsError;
    @Nullable
    private Integer categoryError;
    private boolean isDataValid;

    PublishFormState(@Nullable Integer productNameError, @Nullable Integer descriptionError,
                     @Nullable Integer priceError, @Nullable Integer tagsError,
                     @Nullable Integer categoryError) {
        this.productNameError = productNameError;
        this.descriptionError = descriptionError;
        this.priceError = priceError;
        this.tagsError = tagsError;
        this.categoryError = categoryError;
        this.isDataValid = false;
    }

    PublishFormState(boolean isDataValid) {
        this.productNameError = null;
        this.descriptionError = null;
        this.priceError = null;
        this.tagsError = null;
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
    public Integer getTagsError() {
        return tagsError;
    }

    @Nullable
    public Integer getCategoryError() {
        return categoryError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}