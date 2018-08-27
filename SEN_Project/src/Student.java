import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.Console;
import java.util.*;

public class Student {

    static Scanner in = new Scanner(System.in);
    static int max_id_length = 3;

    String username;
    String student_name;
    int student_batch_year;
    String student_batch_type;
    String student_contact;
    String student_email_id;
    String address;
    double student_cpi;
    String student_DOB;
    String resume_link;
    ArrayList<Test> tests_applied = new ArrayList<>();
    ArrayList<Announcement> announcements = new ArrayList<>();
    ArrayList<Doubt> doubts = new ArrayList<>();

    public static void createProfile()
    {
        Student s = new Student();

        System.out.print("Enter name: ");
        s.student_name = in.nextLine();
        System.out.print("Enter batch type [ B_Tech / M_Tech / MSC]: ");
        s.student_batch_type = in.nextLine();
        System.out.print("Enter batch year (ending): ");
        s.student_batch_year = Integer.valueOf(in.nextLine());
        System.out.print("Enter contact number: ");
        s.student_contact = in.nextLine();
        System.out.print("Enter email id: ");
        s.student_email_id = in.nextLine();
        System.out.print("Enter address: ");
        s.address = in.nextLine();
        System.out.print("Enter CPI: ");
        s.student_cpi = in.nextDouble();in.nextLine();
        System.out.print("Enter Date of birth: [ DD/MM/YYYY ] ");
        s.student_DOB = in.nextLine();
        System.out.print("Enter resume link: ");
        s.resume_link = in.nextLine();
        System.out.print("Enter password: ");
        String password = in.nextLine();

        MongoCollection<Document> student = Sys.db.getCollection("Student");
        MongoCollection<Document> system = Sys.db.getCollection("System");

        s.username = "s_";
        String student_count = String.valueOf((int) student.count()+1);
        for (int i=0;i<max_id_length-student_count.length();i++)    s.username+="0";
        s.username += student_count;


        Sys.users.put(s.username,password);
        Document user = new Document("username", s.username)
                .append("password", password);
        system.insertOne(user);



        Sys.students.add(s);
        Document docStudent = new Document("username",s.username)
                .append("student_name",s.student_name)
                .append("student_batch_year",s.student_batch_year)
                .append("student_batch_type",String.valueOf(s.student_batch_type))
                .append("student_contact",s.student_contact)
                .append("student_email_id",s.student_email_id)
                .append("address",s.address)
                .append("student_cpi",String.valueOf(s.student_cpi))
                .append("student_DOB",s.student_DOB)
                .append("resume_link",s.resume_link)
                .append("tests_applied",new BasicDBList())
                .append("announcements",new BasicDBList())
                .append("doubts",new BasicDBList());
        student.insertOne(docStudent);


        System.out.print("Registration Successful.\n");
        System.out.println("Your username for login is: "+ s.username+"\n\n");


    }

    public static void showAnnouncements(Student student)
    {
        System.out.print("Notifications for you: \n\n");

        for (Announcement an : student.announcements)
        {
            an.viewAnnouncement(an);
        }

        System.out.println("--------------------------------------");
    }

    public static void viewProfile(Student student)
    {
        System.out.println("\n\nProfile: \n");

        System.out.println("Username:      "+student.username);
        System.out.println("\nName:          "+student.student_name);
        System.out.println("Year:          "+student.student_batch_year);
        System.out.println("Batch type:    "+student.student_batch_type);
        System.out.println("Contact:       "+student.student_contact);
        System.out.println("Email id:      "+student.student_email_id);
        System.out.println("address:       "+student.address);
        System.out.println("CPI:           "+student.student_cpi);
        System.out.println("Date Of Birth: "+student.student_DOB);
        System.out.println("Resume link:   "+student.resume_link);
    }

}