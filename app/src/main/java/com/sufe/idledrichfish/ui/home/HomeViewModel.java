package com.sufe.idledrichfish.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.Result;
import com.sufe.idledrichfish.data.StudentRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

import java.util.ArrayList;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

class HomeViewModel extends ViewModel {
//    private MutableLiveData<HomeStudentResult> homeStudentResult = new MutableLiveData<>();
    private MutableLiveData<List<HomeProductView>> homeProductsData = new MutableLiveData<>();
    private ProductRepository productRepository;
    private StudentRepository studentRepository;

    HomeViewModel(ProductRepository productRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.productRepository = productRepository;
    }

//    LiveData<HomeStudentResult> getHomeStudentResult() {
//        return homeStudentResult;
//    }
    LiveData<List<HomeProductView>> getHomeProductsData() {
        return homeProductsData;
    }

    /**
     * 获取首页需要显示的产品信息
     */
//    public void queryHomeProducts() {
//        Result<List<Product>> result = productRepository.queryProductsForHome();
//        List<HomeProductView> homeProductsView = new ArrayList<>();
//
//        if (result instanceof Result.Success) {
//            List<Product> data = ((Result.Success<List<Product>>) result).getData();
//            // todo
//            for (Product p : data) {
//                homeProductsView.add(new HomeProductView(p.getObjectId(), p.getName(), p.getPrice(),
//                        p.isNew(), p.isCanBargain(), p.getImage1().getUrl(), p.getSeller().getObjectId()));
//            }
//            homeProductsData.setValue(homeProductsView);
//        } else {
////            if (productsResult instanceof Result.Error) {
////                int pError = ((Result.Error) productsResult).getCode();
//            homeProductsData.setValue(homeProductsView);
//            Toast.makeText(getApplicationContext(),
//                    getApplicationContext().getString(R.string.home_products_fail), Toast.LENGTH_LONG).show();
////                if (error == 101) {
////                    loginFormState.setValue(new LoginFormState(R.string.wrong_student, null));
////                }
////            }
//        }
//    }

    /**
     * 获取首页需要显示的学生信息
     */
//    public void queryHomeStudent(String studentId) {
//        Result<Student> result = studentRepository.queryStudentForHome(studentId);
//
//        if (result instanceof Result.Success) {
//            Student data = ((Result.Success<Student>) result).getData();
//            homeStudentResult.setValue(new HomeStudentResult(new HomeStudentView(data.getObjectId(),
//                    data.getName(), data.getCredit(), data.getImage().getUrl())));
//        } else {
////            if (productsResult instanceof Result.Error) {
////                int pError = ((Result.Error) productsResult).getCode();
//            homeStudentResult.setValue(new HomeStudentResult(R.string.home_products_fail));
////                if (error == 101) {
////                    loginFormState.setValue(new LoginFormState(R.string.wrong_student, null));
////                }
////            }
//        }
//    }
}
