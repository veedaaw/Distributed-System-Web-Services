package com;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.util.ArrayList;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface InterfaceService
{

    public String addCourse (String courseID, String semester, int capacity, String desc);
    public String removeCourse (String courseID, String semester);
    public String[] courseAvailability (String semester);

    public String enrolCourse (String studentID, String courseID, String semester);
    public String[] getClassSchedule (String studentID);
    public String dropCourse (String studentID, String courseID);
    public String addUserToEnrolledUser(String name, String id ) throws IOException;

    public boolean hasCapacity(String courseID);
}
