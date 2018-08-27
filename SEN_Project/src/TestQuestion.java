import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Scanner;

public class TestQuestion {
    static Scanner in = new Scanner(System.in);

    int q_id;
    int q_difficulty;
    String q_statement;
    String option_a;
    String option_b;
    String option_c;
    String option_d;
    String q_answer;
    static int max_id_length = 6;


    TestQuestion()
    {

    }

    public static void createQuestion(TestQuestion q)
    {
        System.out.print("Enter question statement: \n");
        q.q_statement = in.nextLine();
        System.out.print("Enter option a: ");
        q.option_a = in.nextLine();
        System.out.print("Enter option b: ");
        q.option_b = in.nextLine();
        System.out.print("Enter option c: ");
        q.option_c = in.nextLine();
        System.out.print("Enter option d: ");
        q.option_d = in.nextLine();

        System.out.print("Enter question difficulty level [on scale of 1 to 5]: ");
        q.q_difficulty = Integer.valueOf(in.nextLine());

        System.out.print("Enter q_answer: ");
        q.q_answer = in.nextLine();


        MongoCollection<Document> testquestion = Sys.db.getCollection("TestQuestion");

        q.q_id = (int) testquestion.count()+1;

        Sys.test_questions.add(q);
        Document docQuestion = new Document("q_id",String.valueOf(q.q_id))
                .append("q_statement",q.q_statement)
                .append("option_a",q.option_a)
                .append("option_b",q.option_b)
                .append("option_c",q.option_c)
                .append("option_d",q.option_d)
                .append("q_difficulty",String.valueOf(q.q_difficulty))
                .append("q_answer",q.q_answer);
        testquestion.insertOne(docQuestion);

    }

    public static void showQuestion(TestQuestion q)
    {
        System.out.println(q.q_statement);
        System.out.println(" a) "+q.option_a);
        System.out.println(" b) "+q.option_b);
        System.out.println(" c) "+q.option_c);
        System.out.println(" d) "+q.option_d);

        //System.out.println(" Answer: " + q.q_answer);

        System.out.println();
    }

    public String getAnswer(int q_id)
    {
        String answer = "";

        // loads answer from database

        return answer;
    }
}