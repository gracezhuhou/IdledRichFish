package com.sufe.idledrichfish.database;

public class StudentBLL {
    private StudentDAL studentDAL;

    public StudentBLL() {
        studentDAL = new StudentDAL();
    }

    public boolean insertStudent(Student student) {
        return studentDAL.insertStudent(student);
    }

    public boolean deleteStudentById(String id) {
        return studentDAL.deleteStudentById(id);
    }

    public Student getStudentById(String id) {
        return studentDAL.getStudentById(id);
    }
}
