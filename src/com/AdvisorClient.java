package com;


import com.COMP.CompPublisher;
import com.INSE.InsePublisher;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdvisorClient {


    public static URL INSE_url;
    public static InterfaceService INSEObj;

    public static URL COMP_url;
    public static InterfaceService COMPObj;

    public static URL SOEN_url;
    public static InterfaceService SOENObj;

    /**
     * @param args
     *
     */
    public static void main(String[] args) {
        try {


            INSE_url = new URL("http://127.0.0.1:20000/INSE?wsdl");

            QName qname = new QName("http://INSE.com/", "ImplINSEService");

            Service service = Service.create(INSE_url, qname);
            INSEObj = service.getPort(InterfaceService.class);

            //******************************************************//

            COMP_url = new URL("http://127.0.0.1:10000/COMP?wsdl");

            QName _qname = new QName("http://COMP.com/", "ImplCOMPService");

            Service _service = Service.create(COMP_url, _qname);
            COMPObj = _service.getPort(InterfaceService.class);

            //******************************************************//

            SOEN_url = new URL("http://127.0.0.1:30000/SOEN?wsdl");

            QName _qname_ = new QName("http://SOEN.com/", "ImplSOENService");

            Service _service_ = Service.create(SOEN_url, _qname_);
            SOENObj = _service_.getPort(InterfaceService.class);

            //******************************************************//


            System.out.println("-----------------------------------");


            Runnable task1 = () ->
            {

                try {
                    swapCourse("COMPS1234", "COMP1111", "COMP2222", "fall" );
                } catch (ExecutionException e) {

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            };
            Runnable task2 = () -> {

                System.out.println(COMPObj.enrolCourse("COMPS4567", "COMP2222", "fall"));


            };


            Thread thread1 = new Thread(task1);
            Thread thread2 = new Thread(task2);


           // thread1.start();
           // thread2.start();




            Scanner c = new Scanner(System.in);
            System.out.println("Welcome to DCSR system!");
            for (;;) {
                System.out.println("please enter your id: ");
                String id = c.nextLine();

                if (id.startsWith("COMPA") || id.startsWith("SOENA") || id.startsWith("INSEA")) {
                    for (; ; ) {
                        System.out.println("-----------------------------------");
                        System.out.println("What do you want to do? Please enter a number:");
                        System.out.println("1- Add a student");
                        System.out.println("2- Add a course");
                        System.out.println("3- Enroll a student in a course");
                        System.out.println("4- Drop a student from a course");
                        System.out.println("5- Remove a course");
                        System.out.println("6- Get class schedule for a student");
                        System.out.println("7- Get a list of course availability");
                        System.out.println("8- Swap courses");
                        System.out.println("-----------------------------------");
                        String number = c.nextLine();
                        int option = Integer.parseInt(number);
                        switch (option) {
                            case 1:
                                System.out.println("Enter student id/Name");
                                String student = c.nextLine();
                                addUserToEnrolledUser(student.split(" ")[0], student.split(" ")[1]);
                                continue;
                            case 2:
                                System.out.println("Enter course id/semester/capacity/description");
                                String course = c.nextLine();
                                addCourse(course.split(" ")[0], course.split(" ")[1],
                                        Integer.parseInt(course.split(" ")[2]), course.split(" ")[3]);
                                continue;

                            case 3:
                                System.out.println("Enter student id/course id/semester");
                                String enroll = c.nextLine();
                                enrolCourse(enroll.split(" ")[0], enroll.split(" ")[1], enroll.split(" ")[2]);
                                continue;
                            case 4:
                                System.out.println("Enter student id/course id");
                                String drop = c.nextLine();
                                dropCourse(drop.split(" ")[0], drop.split(" ")[1]);
                                continue;
                            case 5:
                                System.out.println("Enter course id/semester");
                                String remove = c.nextLine();
                                removeCourse(remove.split(" ")[0], remove.split(" ")[1]);
                                continue;
                            case 6:
                                System.out.println("Enter student id");
                                String schedule = c.nextLine();
                                getClassSchedule(schedule);
                                continue;
                            case 7:
                                System.out.println("Enter a semester");
                                String available = c.nextLine();
                                listCourseAvailability(available);
                                continue;
                            case 8:
                                System.out.println("Enter student id/current course/ new course/ semester");
                                String swap= c.nextLine();
                                swapCourse(swap.split(" ")[0], swap.split(" ")[1],
                                        swap.split(" ")[2], swap.split(" ")[3]);

                                continue;
                            default:
                                System.out.println("Invalid input");
                                continue;

                        }
                    }
                } else {
                    System.out.println("Your id is not correct");
                }


            }
        } catch (Exception e) {
            System.out.println(" Client exception: " + e);
            e.printStackTrace();
        }
    }




    public static void addCourse(String courseID, String semester, int capacity, String desc) throws RemoteException

    {
        String res = null;
        if (courseID.startsWith("SOEN")) {
            res = SOENObj.addCourse(courseID , semester, capacity, desc);
        }
        else if (courseID.startsWith("COMP"))

        {
            res = COMPObj.addCourse(courseID , semester, capacity, desc);
        }

        else if (courseID.startsWith("INSE")) {
            res = INSEObj.addCourse(courseID , semester, capacity, desc);
        }

        System.out.println(res);

    }

    public static void removeCourse(String courseID, String semester) throws RemoteException

    {
        String res = null;
        if (courseID.startsWith("SOEN")) {
            res = SOENObj.removeCourse(courseID, semester);
        }
        else if (courseID.startsWith("COMP")) {
            res = COMPObj.removeCourse(courseID, semester);
        }
        else if (courseID.startsWith("INSE")) {
            res = INSEObj.removeCourse(courseID, semester);
        }

        System.out.println(res);
    }

    public static void enrolCourse (String studentID, String courseID, String semester) throws Exception {

        String res= null;
        if(courseID.startsWith("SOEN"))
        {

            res = SOENObj.enrolCourse(studentID, courseID, semester);
        }

        if(courseID.startsWith("COMP"))
        {
            res = COMPObj.enrolCourse(studentID, courseID, semester);
        }

        if(courseID.startsWith("INSE"))
        {
            res = INSEObj.enrolCourse(studentID, courseID, semester);
        }


        System.out.println(res);


    }

    public static String dropCourse(String studentID, String courseID)  throws RemoteException
    {
        String res= null;
        if(courseID.startsWith("SOEN"))
        {
            res = SOENObj.dropCourse(studentID, courseID);
        }

        else if(courseID.startsWith("COMP"))
        {
            res = COMPObj.dropCourse(studentID, courseID);
        }

        else if(courseID.startsWith("INSE"))
        {
            res = INSEObj.dropCourse(studentID, courseID);
        }

        System.out.println(res);
        return res;
    }

    public static void getClassSchedule(String studentID) throws RemoteException
    {
        ArrayList<String> res = new ArrayList<>();


        for(int i=0; i< SOENObj.getClassSchedule(studentID).length; i++ )
        {
            res.add(SOENObj.getClassSchedule(studentID)[i]);

        }

        for(int i=0; i< COMPObj.getClassSchedule(studentID).length; i++ )
        {
            res.add(COMPObj.getClassSchedule(studentID)[i]);

        }

        for(int i=0; i< INSEObj.getClassSchedule(studentID).length; i++ )
        {
            res.add(INSEObj.getClassSchedule(studentID)[i]);

        }



        System.out.println("Class Schedule for this student is:");
        for(String course:res )
        {
            System.out.println(course);
        }


    }

    public static void addUserToEnrolledUser(String id, String name) throws IOException
    {

        if(id.toLowerCase().startsWith("comps") || id.toLowerCase().startsWith("soens") || id.toLowerCase().startsWith("inses"))

        {

            System.out.println( SOENObj.addUserToEnrolledUser(id, name));

            System.out.println(COMPObj.addUserToEnrolledUser(id, name));

            System.out.println(INSEObj.addUserToEnrolledUser(id, name));
        }

        else
        {
            System.out.println("This id " + id + " is not acceptable.");
        }



    }


    public static void listCourseAvailability (String semester) throws RemoteException {

        Runnable task1 = () -> {
            CompPublisher.receive();
        };
        Runnable task2 = () -> {

            CompPublisher.sendMessage(1313, semester);

        };
        Runnable task3 = () -> {

            CompPublisher.sendMessage(1414, semester);

        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);

        thread1.start();
        thread2.start();
        thread3.start();



        String data= " ";
        if(semester.trim().toLowerCase().equals("fall"))
        {
            if(COMPObj.courseAvailability("fall").length>0) {

                for (int i = 0; i < COMPObj.courseAvailability("fall").length; i++) {
                    data += " " +(COMPObj.courseAvailability("fall")[i])+ " ";
                }
            }
        }
        else if (semester.trim().toLowerCase().equals("winter"))
        {
            if(COMPObj.courseAvailability("winter").length>0)
            {
                for (int i = 0; i < COMPObj.courseAvailability("winter").length; i++) {
                    data += " "+ (COMPObj.courseAvailability("winter")[i])+" ";
                }
            }
        }
        else if (semester.trim().toLowerCase().equals("summer"))
        {
            if(COMPObj.courseAvailability("summer").length>0)
            {
                for (int i = 0; i < COMPObj.courseAvailability("summer").length; i++) {
                    data += " "+ (COMPObj.courseAvailability("summer")[i])+" ";
                }
            }
        }

        else
        {

            data = "-0- ";
        }

        System.out.println("Reply received from the COMP server is: " + data);


    }



    public static void swapCourse (String student_id, String oldCourse_id, String newCourse_id, String semester) throws ExecutionException, InterruptedException, RemoteException {


        String result= "";
        String data= " ";


//********************COMP***********************//



        if(oldCourse_id.startsWith("COMP"))

        {
            if (newCourse_id.startsWith("COMP"))
            {

                String[] res = COMPObj.getClassSchedule(student_id);
                String[] res2 = COMPObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (COMPObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + COMPObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }

            if (newCourse_id.startsWith("SOEN"))
            {
                java.util.concurrent.Callable<String> callable = () -> {

                    return  CompPublisher.sendMessage(1313, semester);
                };

                Runnable task1 = () -> {
                    CompPublisher.receive();
                };


                Thread thread1 = new Thread(task1);
                thread1.start();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<String> future = executorService.submit(callable);
                String courseList = future.get();

                // System.out.println(heh);

                String[] old = COMPObj.getClassSchedule(student_id);

                String [] splitted = courseList.split(" ");
                for(String s : splitted) {
                    if (s.equals(newCourse_id) && Arrays.stream(old).anyMatch(oldCourse_id::equals))

                    {
                        if (s.indexOf(newCourse_id) + 1 > 0) // capacity of new course
                        {
                            result += dropCourse(student_id, oldCourse_id) + " and //";
                            result += " " + SOENObj.enrolCourse(student_id, newCourse_id, semester);

                        } else {
                            System.out.println("Unfortunately the course you want to swap with is full");
                        }


                    }


                }

            }



            if (newCourse_id.startsWith("INSE"))
            {
                java.util.concurrent.Callable<String> callable = () -> {

                    return  CompPublisher.sendMessage(1414, semester);
                };

                Runnable task1 = () -> {
                    CompPublisher.receive();
                };


                Thread thread1 = new Thread(task1);
                thread1.start();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<String> future = executorService.submit(callable);
                String courseList = future.get();

                // System.out.println(heh);

                String[] old = COMPObj.getClassSchedule(student_id);

                String [] splitted = courseList.split(" ");
                for(String s : splitted) {
                    if (s.equals(newCourse_id) && Arrays.stream(old).anyMatch(oldCourse_id::equals))

                    {
                        if (s.indexOf(newCourse_id) + 1 > 0) // capacity of new course
                        {
                            result += dropCourse(student_id, oldCourse_id) + " and //";
                            result += " " + INSEObj.enrolCourse(student_id, newCourse_id, semester);

                        } else {
                            System.out.println("Unfortunately the course you want to swap with is full");
                        }


                    }


                }

            }





        }


//**********************SOEN*******************************//


        else  if(oldCourse_id.startsWith("SOEN"))
        {
            if (newCourse_id.startsWith("COMP"))
            {

                String[] res = SOENObj.getClassSchedule(student_id);
                String[] res2 = COMPObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (COMPObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + COMPObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }

            else if (newCourse_id.startsWith("SOEN"))
            {

                String[] res = SOENObj.getClassSchedule(student_id);
                String[] res2 = SOENObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (SOENObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + SOENObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }

            else if (newCourse_id.startsWith("INSE"))
            {

                String[] res = SOENObj.getClassSchedule(student_id);
                String[] res2 = INSEObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (INSEObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + INSEObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }
        }


//*************************************INSE****************************//


        else  if(oldCourse_id.startsWith("INSE"))
        {
            if (newCourse_id.startsWith("COMP"))
            {

                String[] res = INSEObj.getClassSchedule(student_id);
                String[] res2 = COMPObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (COMPObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + COMPObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }

            else if (newCourse_id.startsWith("SOEN"))
            {

                String[] res = INSEObj.getClassSchedule(student_id);
                String[] res2 = SOENObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (SOENObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + SOENObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }

            else if (newCourse_id.startsWith("INSE"))
            {

                String[] res = INSEObj.getClassSchedule(student_id);
                String[] res2 = INSEObj.courseAvailability(semester);
                if (Arrays.stream(res).anyMatch(oldCourse_id::equals) && Arrays.stream(res2).anyMatch(newCourse_id::equals)) {

                    if (INSEObj.hasCapacity(newCourse_id)) {


                        result += dropCourse(student_id, oldCourse_id) + " and //";
                        result += " " + INSEObj.enrolCourse(student_id, newCourse_id, semester);

                    } else {
                        System.out.println("Unfortunately the course you want to swap with is full");
                    }


                } else {
                    result = "either you don't have this course or the new course is not defined";
                    System.out.println(result);
                }

            }
        }



        System.out.println(result);



    }



}

