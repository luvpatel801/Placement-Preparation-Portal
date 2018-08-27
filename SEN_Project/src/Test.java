import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Test {
    static Scanner in = new Scanner(System.in);
    static int max_id_length = 3;

    String test_id;
    String test_title;
    ArrayList<String> student_personal_ans = new ArrayList<>();
    int test_difficulty_level;
    String test_date;
    String test_duration;
    ArrayList<TestQuestion> test_questions = new ArrayList<>();
    int total_questions;

    Test()
    {

    }

    Test(Test t)
    {
        this.total_questions = t.total_questions;
        this.test_questions = t.test_questions;
        this.test_difficulty_level = t.test_difficulty_level;
        this.test_date = t.test_date;
        this.test_title = t.test_title;
        this.test_id = t.test_id;
        this.test_duration = t.test_duration;
        this.student_personal_ans = t.student_personal_ans;
    }

    public static void viewTest(Test t)
    {
        if (t.student_personal_ans.isEmpty())   System.out.println(" "+t.test_id+")\t" + t.test_title+"\t\t" +t.test_date);
    }

    public static void viewWholeTest(Test t)
    {
        System.out.println(" "+t.test_id+")\t" + t.test_title+"\t\t" +t.test_date+" "+t.test_duration+"\n");
        System.out.println(" Total questions: "+t.total_questions+"\t\t Test difficulty level: "+t.test_difficulty_level+"\n");

        //System.out.println(t.test_questions);
        int i=1;

        for (TestQuestion q : t.test_questions)
        {
            System.out.print(" "+i+")\t");
            TestQuestion.showQuestion(q);
        }

    }

    public static void applyForTest(Student s, Test t)
    {
        System.out.println(" "+t.test_id+")\t" + t.test_title+"\t\t" +t.test_date+" "+t.test_duration+"\n");
        System.out.println(" Total questions: "+t.total_questions+"\t\t Test difficulty level: "+t.test_difficulty_level+"\n");

        //System.out.println(t.test_questions);
        int i=1;
        int count = 0;


        ArrayList<String> test_questions = new ArrayList<>();
        for (TestQuestion q : t.test_questions)
        {
            System.out.print(" "+i+")\t");
            TestQuestion.showQuestion(q);
            System.out.print("Enter your answer [ a/b/c/d ]: ");
            String answer = in.nextLine();
            t.student_personal_ans.add(answer);
            if (answer.equals(q.q_answer)) count++;
            test_questions.add(String.valueOf(q.q_id));
        }

        System.out.println("Correct answers: "+count+" out of "+t.total_questions+"\n\n");

        String test_id = Test.addTestToDatabase(t);
        t.test_id = test_id;
        s.tests_applied.add(t);
        Sys.tests.add(t);


        MongoCollection<Document> students = Sys.db.getCollection("Student");
        MongoCursor<Document> studentitr = students.find().iterator();


        while(studentitr.hasNext())
        {
            Document doc = studentitr.next();
            ArrayList<String> An = (ArrayList<String>) doc.get("tests_applied");
            An.add(test_id);

            Bson filter = new Document("username",s.username);
            Bson newValue = new Document("tests_applied",An);
            Bson updateOperationDocument = new Document("$set", newValue);
            students.updateOne(filter, updateOperationDocument);

        }


    }

    public static void addTest(Test t)
    {
        System.out.println("\n------Adding Test -----\n");

        System.out.print("Enter test title: ");
        t.test_title = in.nextLine();
        System.out.print("Enter test date [ DD/MM/YYYY ]: ");
        t.test_date = in.nextLine();
        System.out.print("Enter test duration [ HH:MM ]: ");
        t.test_duration = in.nextLine();
        System.out.print("Enter total questions: ");
        t.total_questions = Integer.valueOf(in.nextLine());
        System.out.print("Enter test difficulty level [on scale of 1 to 5]: ");
        t.test_difficulty_level = Integer.valueOf(in.nextLine());
        t.student_personal_ans = new ArrayList<>();

        // adding questions...

        // Contains list of q_id...
        ArrayList<String> test_questions = new ArrayList<>();


        for (int i=0;i<t.total_questions;i++)
        {
            System.out.print(" "+(i+1)+") ");
            TestQuestion q = new TestQuestion();
            TestQuestion.createQuestion(q);
            t.test_questions.add(q);
            test_questions.add(String.valueOf(q.q_id));
        }

        addTestToDatabase(t);

    }

    public static String addTestToDatabase(Test t)
    {
        ArrayList<String> test_questions = new ArrayList<>();


        for (TestQuestion q: t.test_questions)
        {
            test_questions.add(String.valueOf(q.q_id));
        }

        MongoCollection<Document> test = Sys.db.getCollection("Test");

        t.test_id = "t_";
        String test_count = String.valueOf((int) test.count()+1);
        for (int i=0;i<max_id_length-test_count.length();i++)    t.test_id+="0";
        t.test_id += test_count;


        Document docTest = new Document("test_id",t.test_id)
                .append("test_title",t.test_title)
                .append("test_difficulty_level",String.valueOf(t.test_difficulty_level))
                .append("total_questions",String.valueOf(t.total_questions))
                .append("test_date",t.test_date)
                .append("test_questions",test_questions)
                .append("test_duration",t.test_duration)
                .append("student_personal_ans",t.student_personal_ans);
        test.insertOne(docTest);
        Sys.tests.add(t);

        return t.test_id;
    }

}
