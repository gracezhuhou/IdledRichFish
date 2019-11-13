package com.sufe.idledrichfish.database.helper;

import android.util.Log;

import com.sufe.idledrichfish.database.Student;

import org.litepal.LitePal;

import java.util.List;

public class StudentDbHelper extends DbHelper {

    public StudentDbHelper() {
        super();
    }

    public Boolean addStudent(String id, String name, String password, String gender, int admin_id) {
        Student student = new Student();
        student.setName(name);
        student.setStudentId(id);
        student.setPassword(password);
        student.setGender(gender);
        student.setAdminId(admin_id);

        // todo: image
        //获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片
//        }
        student.save();

        Log.v("Database","Add Student Success");
        return true;
    }

    // 根据id找唯一学生
    public Student getStudentById(String id) {
        List<Student> students = LitePal.where("student_id = ?", id).find(Student.class);

        if (students.size() != 0)
            return students.get(0);
        else
            return null;
    }

    public List<Student> getStudents() {
        List<Student> students = LitePal.findAll(Student.class);
        return students;
    }
}
