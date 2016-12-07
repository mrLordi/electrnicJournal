//import entities.Director;
//import entities.HeadTeacher;
//import entities.School;
//
//import javax.persistence.*;
//import javax.transaction.Transaction;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by win10 on 21.11.2016.
// */
//public class Test {
//
//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//        EntityManager em = emf.createEntityManager();
//
//
//
//        HeadTeacher headTeacher = new HeadTeacher("headTeacher", "headTeacher", "headTeacher",
//                new Date(), "phone", "headTeacher", "headTeacher", "headTeacher",
//                (School) em.createQuery("SELECT s FROM School s WHERE s.schoolId = 1").getResultList().get(0), "headTeacher");
//
//        EntityTransaction transaction = em.getTransaction();
//        transaction.begin();
//        em.merge(headTeacher);
//        transaction.commit();
//
//        List<HeadTeacher> headTeachers = em.createQuery("SELECT h FROM HeadTeacher h WHERE h.school.schoolId = 1").getResultList();
//        for (HeadTeacher headTeacher1 : headTeachers) {
//            System.out.println(headTeacher1);
//        }
//
//    }
//
//}
