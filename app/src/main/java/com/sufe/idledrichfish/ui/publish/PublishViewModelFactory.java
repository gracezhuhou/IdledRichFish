package com.sufe.idledrichfish.ui.publish;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

/**
 * ViewModel provider factory to instantiate PublishViewModel.
 * Required given PublishViewModel has a non-empty constructor
 */
public class PublishViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PublishViewModel.class)) {
            return (T) new PublishViewModel(ProductRepository.getInstance(new ProductDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

