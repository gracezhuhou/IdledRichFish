package com.sufe.idledrichfish.ui.myOrder;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;
import com.sufe.idledrichfish.data.model.Product;

import java.util.List;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<List<MyOrderView>> orders = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public void setOrders(List<MyOrderView> orders) {
        this.orders.setValue(orders);
    }

    public LiveData<String> getText() {
        return mText;
    }

    LiveData<List<MyOrderView>> getOrders() {
        return orders;
    }
}