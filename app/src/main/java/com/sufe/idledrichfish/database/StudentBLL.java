package com.sufe.idledrichfish.database;

public class StudentBLL {
    private StudentDAL studentDAL;

    public StudentBLL() {
        studentDAL = new StudentDAL();
    }

    public void insertStudent(Student student) {
        studentDAL.insertStudent(student);
    }

    public boolean deleteStudentById(String id) {
        return studentDAL.deleteStudentById(id);
    }

    public Student getStudentById(String id) {
        return studentDAL.getStudentById(id);
    }

    /*
     * 将Bmob上的Student数据保存至本地数据库
     */
    public boolean storeStudent(BmobStudent bmobStudent) {
        Student student = new Student();
        student.setStudentId(bmobStudent.getObjectId());
        student.setStudentNumber(bmobStudent.getName());
        student.setName(bmobStudent.getUsername());
        student.setGender(bmobStudent.getGender());
        student.setCredit(bmobStudent.getCredit());
        student.setImage(bmobStudent.getImage());
        student.setLastRegisterDate(bmobStudent.getLastRegisterDate());

        if (studentDAL.isStudentExistById(student.getStudentId())) {
            return studentDAL.updateStudent(student);
        }
        else {
            return studentDAL.insertStudent(student);
        }
    }
}
