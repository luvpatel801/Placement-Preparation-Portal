import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.*;

public class Sys {

    static Scanner in = new Scanner(System.in);
    static MongoClientURI uri;
    static MongoClient mongo_client;
    static MongoDatabase db;


    static HashMap<String,String> users = new HashMap<>();
    static ArrayList<Test> tests = new ArrayList<>();
    static ArrayList<Company> companies = new ArrayList<>();
    static ArrayList<Student> students = new ArrayList<>();
    static DiscussionForum discussion_forum = new DiscussionForum();
    static ArrayList<Announcement> announcements = new ArrayList<>();
    static ArrayList<TestQuestion> test_questions = new ArrayList<>();
    static ArrayList<SPC> spcs = new ArrayList<>();
    static ArrayList<Alumni> alumni = new ArrayList<>();



    static int test_count;
    static int test_question_count;
    static int an_count;
    static int doubt_count;
    static int student_count;


    public static int authenticate(String username, String password)
    {

        if (!users.containsKey(username))   return 3;           // re-enter your username...
        else if (users.get(username).equals(password))
        {
            if (username.startsWith("spc"))
            {
                return 9;                   // SPC
            }
            else if(username.startsWith("s"))   return 8;       // Student
            else                                return 7;       // Alumni
        }
        return 4;                                               // re-enter password...
    }


    public static int signUp()
    {
        System.out.println("1: Student");
        System.out.println("2: Alumni");
        System.out.println("0: Exit");
        System.out.print("Enter your choice: ");

        int choice = Integer.valueOf(in.nextLine());

        switch (choice)
        {
            case 1:
                Student.createProfile();
                break;
            case 2:
                Alumni.createProfile();
                break;
            case 0:
                System.exit(0);
                break;

                default:
                    System.out.println("You entered wrong choice please re-try.\n");
                    signUp();
                    break;
        }

        System.out.println("Please login ----\n");
        return 1;
    }



    static void connectDatabase()
    {
        uri = new MongoClientURI("mongodb://hk:12345678@ds229418.mlab.com:29418/ppp");
        mongo_client = new MongoClient(uri);
        db = mongo_client.getDatabase(uri.getDatabase());
    }




    static void loadDatabase()
    {


        // Loading user_pass maps...


        MongoCollection<Document> temp = db.getCollection("System");
        MongoCursor<Document> cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Document doc = cursor.next();
            String a = doc.getString("username");
            String b = doc.getString("password");
            users.put(a,b);
        }


        // Loading Company Profiles...

        temp = db.getCollection("Company");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Company c = new Company();
            Document doc = cursor.next();
            c.company_id = doc.get("company_id").toString();
            c.company_name = doc.get("company_name").toString();
            c.cpi_criteria = (double) Double.valueOf(doc.get("cpi_criteria").toString());
            c.open_jobs = (int) Integer.valueOf(doc.get("open_jobs").toString());
            c.website_link = doc.get("website_link").toString();
            c.rating = (double) Double.valueOf(doc.get("rating").toString());
            c.job_type_salary = (ArrayList<String>) doc.get("job_type_salary");

            companies.add(c);
        }


        // Loading Announcements...

        temp = db.getCollection("Announcement");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Announcement an = new Announcement();
            Document doc = cursor.next();
            an.an_id = doc.get("an_id").toString();
            an.an_type = doc.get("an_type").toString();
            an.an_statement = doc.get("an_statement").toString();
            an.an_time = doc.get("an_time").toString();
            an.an_from = doc.get("an_from").toString();
            an.an_to = doc.get("an_to").toString();

            announcements.add(an);
        }


        // Loading doubts...

        temp = db.getCollection("Doubt");
        cursor = temp.find().iterator();

        doubt_count = 0;

        while(cursor.hasNext())
        {
            Doubt d = new Doubt();
            Document doc = cursor.next();
            discussion_forum.doubts = new ArrayList<>();
            d.doubt_id = doc.get("doubt_id").toString();
            d.doubt_statement = doc.get("doubt_statement").toString();
            d.doubt_author_id = doc.get("doubt_author_id").toString();
            d.doubt_time = doc.get("doubt_time").toString();
            d.is_answered = (boolean) Boolean.valueOf(doc.get("is_answered").toString());
            d.answer = doc.getString("answer");
            d.answer_author_id = doc.getString("answer_author_id");
            d.answered_time = doc.getString("answered_time");
            doubt_count++;
            discussion_forum.doubts.add(d);
        }

        discussion_forum.total_doubt = doubt_count;


        // Loading SPCs...

        temp = db.getCollection("SPC");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            SPC s = new SPC();
            Document doc = cursor.next();
            s.username = doc.getString("username");
            s.member_name = doc.getString("member_name");
            s.member_email_id = doc.getString("member_email_id");
            s.member_contact = doc.getString("member_contact");
            s.member_address = doc.getString("member_address");

            ArrayList<String> an_ar = (ArrayList<String>) doc.get("announcements");

            for (String str:an_ar)
            {
                for (Announcement an:announcements)
                {
                    if (str.equals(an.an_id))
                    {
                        s.announcements.add(an);
                    }
                }
            }

            spcs.add(s);
        }


        // Loading Test Questions...

        temp = db.getCollection("TestQuestion");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            TestQuestion q = new TestQuestion();
            Document doc = cursor.next();

            q.q_id = (int) Integer.valueOf(doc.getString("q_id"));
            q.q_statement = doc.getString("q_statement");
            q.q_answer = doc.getString("q_answer");
            q.q_difficulty = (int) Integer.valueOf(doc.getString("q_difficulty"));
            q.option_a = doc.getString("option_a");
            q.option_b = doc.getString("option_b");
            q.option_c = doc.getString("option_c");
            q.option_d = doc.getString("option_d");

            test_questions.add(q);
        }

        // Loading Tests...


        temp = db.getCollection("Test");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Test t = new Test();
            Document doc = cursor.next();

            t.test_id = doc.getString("test_id");
            t.test_title = doc.getString("test_title");
            t.test_difficulty_level = (int) Integer.valueOf(doc.getString("test_difficulty_level"));
            t.test_date = doc.getString("test_date");
            t.test_duration = doc.getString("test_duration");
            t.total_questions = (int) Integer.valueOf(doc.getString("total_questions"));
            t.student_personal_ans = (ArrayList<String>) doc.get("student_personal_ans");

            ArrayList<String> tst_arr = (ArrayList<String>) doc.get("test_questions");

            //System.out.println(tst_arr);

            for (String str : tst_arr)
            {
                for (TestQuestion q : test_questions)
                {
                    if (str.equals(String.valueOf(q.q_id)))
                    {
                        t.test_questions.add(q);
                    }
                }
            }

            tests.add(t);
        }

        // Loading Alumni...

        temp = db.getCollection("Alumni");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Alumni s = new Alumni();
            Document doc = cursor.next();

            s.username = doc.getString("username");
            s.alumni_name = doc.getString("alumni_name");
            s.alumni_company = doc.getString("alumni_company");
            s.alumni_job_position = doc.getString("alumni_job_position");
            s.batch_year = Integer.valueOf(doc.getString("batch_year"));
            s.alumni_email_id = doc.getString("alumni_email_id");
            s.batch_type = doc.getString("batch_type");
            s.cpi = (double) Double.valueOf(doc.getString("cpi"));
            s.resume_link = doc.getString("resume_link");

            ArrayList<String> an_ar = (ArrayList<String>) doc.get("announcements");

            for (String str:an_ar)
            {
                for (Announcement an:announcements)
                {
                    if (str.equals(an.an_id))
                    {
                        s.announcements.add(an);
                    }
                }
            }

            ArrayList<String> d_ar = (ArrayList<String>) doc.get("doubts");

            for (String str : d_ar)
            {
                for (Doubt d : discussion_forum.doubts)
                {
                    if (str.equals(d.doubt_id))
                    {
                        s.doubts.add(d);
                    }
                }
            }

            alumni.add(s);
        }

        temp = db.getCollection("Student");
        cursor = temp.find().iterator();

        while(cursor.hasNext())
        {
            Student s = new Student();
            Document doc = cursor.next();

            s.username = doc.getString("username");
            s.student_name = doc.getString("student_name");
            s.student_batch_year = Integer.valueOf(doc.getString("student_batch_year"));
            s.student_batch_type = doc.getString("student_batch_type");
            s.student_contact = doc.getString("student_contact");
            s.student_email_id = doc.getString("student_email_id");
            s.address = doc.getString("address");
            s.student_DOB = doc.getString("student_DOB");
            s.student_cpi = (double) Double.valueOf(doc.getString("student_cpi"));
            s.resume_link = doc.getString("resume_link");

            ArrayList<String> an_ar = (ArrayList<String>) doc.get("announcements");

            for (String str:an_ar)
            {
                for (Announcement an:announcements)
                {
                    if (str.equals(an.an_id))
                    {
                        s.announcements.add(an);
                    }
                }
            }

            ArrayList<String> d_ar = (ArrayList<String>) doc.get("doubts");

            for (String str : d_ar)
            {
                for (Doubt d : discussion_forum.doubts)
                {
                    if (str.equals(d.doubt_id))
                    {
                        s.doubts.add(d);
                    }
                }
            }

            ArrayList<String> t_ar = (ArrayList<String>) doc.get("tests_applied");

            for (String str : t_ar)
            {
                for (Test t : tests)
                {
                    if (str.equals(t.test_id))
                    {
                        s.tests_applied.add(t);
                    }
                }
            }

            students.add(s);
        }


        //       ALL DATA LOADED SUCCESSFULLY
    }
}