import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class Doubt {
    static Scanner in = new Scanner(System.in);

    String doubt_id = "";
    String doubt_statement = "";
    String doubt_author_id = "";
    String doubt_time = "";
    boolean is_answered = false;
    String answer = "";
    String answer_author_id = "";
    String answered_time = "";

    static int max_id_length = 4;

    public static void createDoubt(String username)
    {
        Doubt doubt = new Doubt();

        System.out.print("Enter your doubt: ");
        doubt.doubt_statement = in.nextLine();
        doubt.answer_author_id = username;

        MongoCollection<Document> d = Sys.db.getCollection("Doubt");

        doubt.doubt_id = "d_";
        String doubt_count = String.valueOf((int) d.count()+1);
        for (int i=0;i<max_id_length-doubt_count.length();i++)    doubt.doubt_id+="0";
        doubt.doubt_id += doubt_count;



        d.insertOne(new Document("doubt_id",doubt.doubt_id)
                .append("doubt_statement",doubt.doubt_statement)
                .append("doubt_author_id",doubt.doubt_author_id)
                .append("doubt_time",doubt.doubt_time)
                .append("answered_time",doubt.answered_time)
                .append("is_answered",String.valueOf(doubt.is_answered))
                .append("answer",doubt.answer)
                .append("answer_author_id",doubt.answer_author_id)
        );
        Sys.discussion_forum.doubts.add(doubt);

    }

    public static void showDoubt(Doubt doubt)
    {
        System.out.println(" "+doubt.doubt_id+")  "+doubt.doubt_statement);
        if (doubt.is_answered)  System.out.println(" =>\t"+doubt.answer);
        else    System.out.println("\t\tThis doubt is still not answered.");
    }

    public static void answerDoubt(Doubt doubt,String username)
    {
        System.out.print("Enter answer: ");
        doubt.answer = in.nextLine();
        doubt.answer_author_id = username;
        doubt.is_answered = true;

        MongoCollection<Document> doubts = Sys.db.getCollection("Doubt");


        Bson filter = new Document("doubt_id",doubt.doubt_id);
        Bson newValue = new Document("answer",doubt.answer);
        Bson updateOperationDocument = new Document("$set", newValue);
        doubts.updateOne(filter, updateOperationDocument);

        Bson filter1 = new Document("doubt_id",doubt.doubt_id);
        Bson newValue1 = new Document("answer",doubt.answer_author_id);
        Bson updateOperationDocument1 = new Document("$set", newValue1);
        doubts.updateOne(filter1, updateOperationDocument1);

        Bson filter2 = new Document("doubt_id",doubt.doubt_id);
        Bson newValue2 = new Document("is_answered",doubt.is_answered);
        Bson updateOperationDocument2 = new Document("$set", newValue2);
        doubts.updateOne(filter2, updateOperationDocument2);
    }

}
