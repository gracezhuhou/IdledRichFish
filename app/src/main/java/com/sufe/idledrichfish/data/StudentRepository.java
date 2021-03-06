package com.sufe.idledrichfish.data;

import com.sufe.idledrichfish.data.model.Student;

public class StudentRepository {

    private static volatile StudentRepository instance;

    private StudentDataSource dataSource;

    // private constructor : singleton access
    private StudentRepository(StudentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static StudentRepository getInstance(StudentDataSource dataSource) {
        if (instance == null) {
            instance = new StudentRepository(dataSource);
        }
        return instance;
    }


    public void queryStudent(String id, int position){
        dataSource.queryStudentForUser(id, position);
    }

    // 查询某用户信息
//    public void queryStudentForHome(String ObjectId, int position) {
//        dataSource.queryStudentForHome(ObjectId, position);
//    }

    public void queryHistoryProducts() {
        dataSource.queryHistoryProducts();
    }

    public void addHistory(String productId) {
        dataSource.addHistory(productId);
    }
}
