package com.sufe.idledrichfish.database;

import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

public class StudentDAL {
    private DBHelper dbHelper;

    public StudentDAL() {
        dbHelper = new DBHelper();
    }

    /*
     * 添加用户
     */
    public Boolean insertStudent(Student student) {
//        Student student = new Student();
//        student.setName(name);
//        student.setName(student_id);
//        student.setPassword(password);
//        student.setGender(gender);
//        student.setAdmin(admin_id);

        // todo: image
        //获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片
//        }
        if (student.save()) {
            Log.i("Database", "Add Student Success");
            return true;
        }
        else {
            Log.i("Database", "Add Student Fail");
            return false;
        }
    }

    /*
     * 更新学生
     */
    public boolean updateStudent(Student student) {
        if (student.updateAll("studentId = ?", student.getStudentNumber()) >= 1) {
            Log.i("Database","Update Student Success");
            return true;
        }
        else {
            Log.i("Database","Update Student Fail");
            return false;
        }
    }

    /*
     * 根据id删除学生
     */
    public boolean deleteStudentById(String id) {
        int deleteNum = LitePal.deleteAll(Student.class, "studentId = ?", id);
        if (deleteNum >= 1) {
            Log.i("Database", "Delete Student Success :" + deleteNum);
            return true;
        }
        else {
            Log.i("Database", "Delete Student Fail");
            return false;
        }
    }

    /*
     * 根据id找唯一学生
     */
    public Student getStudentById(String id) {
        List<Student> students = LitePal.where("studentId = ?", id).find(Student.class);

        if (students.size() != 0)
            return students.get(0);
        else
            return null;
    }

    /*
     * 根据id判断学生是否存在
     */
    public Boolean isStudentExistById(String id) {
        List<Student> students = LitePal.where("studentId = ?", id).find(Student.class);

        if (students.size() != 0)
            return true;
        else
            return false;
    }

    public List<Student> getAllStudents() {
//        List<Student> students = LitePal.findAll(Student.class);
        return LitePal.findAll(Student.class);
    }
}

