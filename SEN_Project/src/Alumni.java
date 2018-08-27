import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

public class Alumni {
    static Scanner in = new Scanner(System.in);

    String username;
    String alumni_name;
    String alumni_company;
    String alumni_job_position;
    int batch_year;
    String batch_type;
    String alumni_email_id;
    ArrayList<Announcement> announcements = new ArrayList<>();
    ArrayList<Doubt> doubts;
    double cpi;
    String resume_link;
    static int max_id_length = 5;

    Alumni()
    {

    }

    public static void showAnnouncements(Alumni alumni)
    {
        System.out.print("Notifications for you: \n\n");

        for (Announcement an : alumni.announcements)
        {
            an.viewAnnouncement(an);
        }

        System.out.println("--------------------------------------");
    }

    public static void viewProfile(Alumni alumni)
    {
        System.out.println("\n\nProfile: \n");

        System.out.println("Username:      "+alumni.username);
        System.out.println("\nName:          "+alumni.alumni_name);
        System.out.println("Company:       "+alumni.alumni_company);
        System.out.println("Job position:  "+alumni.alumni_job_position);
        System.out.println("Year:          "+alumni.batch_year);
        System.out.println("Batch type:    "+alumni.batch_type);
        System.out.println("Email id:      "+alumni.alumni_email_id);
        System.out.println("CPI:           "+alumni.cpi);
        System.out.println("Resume link:   "+alumni.resume_link);
    }

    public static void createProfile()
    {
        Alumni a = new Alumni();

        System.out.print("Enter name: ");
        a.alumni_name = in.nextLine();
        System.out.print("Enter batch type [ B_Tech / M_Tech / MSC]: ");
        a.batch_type = in.nextLine();
        System.out.print("Enter batch year (ending): ");
        a.batch_year = Integer.valueOf(in.nextLine());
        System.out.print("Enter company name: ");
        a.alumni_company = in.nextLine();
        System.out.print("Enter email id: ");
        a.alumni_email_id = in.nextLine();
        System.out.print("Enter CPI: ");
        a.cpi = in.nextDouble();in.nextLine();
        System.out.print("Enter resume link: ");
        a.resume_link = in.nextLine();
        System.out.print("Enter your job positions: ");
        a.alumni_job_position = in.nextLine();
        System.out.print("Enter password: ");
        String password = in.nextLine();

        MongoCollection<Document> alumni = Sys.db.getCollection("Alumni");
        MongoCollection<Document> system = Sys.db.getCollection("System");

        a.username = "a_";
        String student_count = String.valueOf((int) alumni.count()+1);
        for (int i=0;i<max_id_length-student_count.length();i++)    a.username+="0";
        a.username += student_count;


        Sys.users.put(a.username,password);
        Document user = new Document("username", a.username)
                .append("password", password);
        system.insertOne(user);



        Sys.alumni.add(a);
        Document docAlumni = new Document("username",a.username)
                .append("alumni_name",a.alumni_name)
                .append("batch_year",String.valueOf(a.batch_year))
                .append("batch_type",a.batch_type)
                .append("alumni_company",a.alumni_company)
                .append("alumni_email_id",a.alumni_email_id)
                .append("cpi",String.valueOf(a.cpi))
                .append("resume_link",a.resume_link)
                .append("alumni_job_position",a.alumni_job_position)
                .append("announcements",new BasicDBList())
                .append("doubts",new BasicDBList());
        alumni.insertOne(docAlumni);


        System.out.print("Registration Successful.\n");
        System.out.println("Your username for login is: "+ a.username+"\n\n");

    }
}