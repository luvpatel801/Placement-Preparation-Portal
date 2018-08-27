import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class Announcement {
    static Scanner in = new Scanner(System.in);

    String an_id;
    String an_type;
    String an_statement;
    String an_time = "";
    String an_from;
    String an_to;
    static int max_id_length = 4;

    Announcement()
    {

    }

    public static void sendAnnouncement(Announcement announcement)
    {
        MongoCollection<Document> an = Sys.db.getCollection("Announcement");

        announcement.an_id = "an_";

        String an_count = String.valueOf((int) an.count()+1);
        for (int i=0;i<max_id_length-an_count.length();i++)    announcement.an_id+="0";
        announcement.an_id += an_count;


        Sys.announcements.add(announcement);
        Document docAnnouncement = new Document("an_id",announcement.an_id)
                .append("an_type",announcement.an_type)
                .append("an_statement",announcement.an_statement)
                .append("an_time",announcement.an_time)
                .append("an_from",announcement.an_from)
                .append("an_to",announcement.an_to);
        an.insertOne(docAnnouncement);

        MongoCollection<Document> students = Sys.db.getCollection("Student");
        MongoCollection<Document> alumni = Sys.db.getCollection("Alumni");
        MongoCollection<Document> spc = Sys.db.getCollection("SPC");

        MongoCursor<Document> studentitr = students.find().iterator();
        MongoCursor<Document> alumniitr = alumni.find().iterator();
        MongoCursor<Document> spcitr = spc.find().iterator();


        if (announcement.an_to.equals("all"))
        {

            // for student

            for (Student s:Sys.students)
            {
                s.announcements.add(announcement);
            }

            while(studentitr.hasNext())
            {
                Document doc = studentitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);

                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                students.updateOne(filter, updateOperationDocument);

            }

            // for spc

            for (SPC spc1:Sys.spcs)
            {
                spc1.announcements.add(announcement);
            }

            while(spcitr.hasNext())
            {
                Document doc = spcitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);
                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                spc.updateOne(filter, updateOperationDocument);

            }

            // for alumni

            while(alumniitr.hasNext())
            {
                for (Alumni a:Sys.alumni)
                {
                    a.announcements.add(announcement);
                }

                Document doc = alumniitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);
                System.out.println(An);
                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                alumni.updateOne(filter, updateOperationDocument);

            }
        }

        else if (announcement.an_to.equals("stud_spc"))
        {
            // student

            for (Student s:Sys.students)
            {
                s.announcements.add(announcement);
            }


            while(studentitr.hasNext())
            {
                Document doc = studentitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);

                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                students.updateOne(filter, updateOperationDocument);

            }

            // for SPC

            for (SPC spc1:Sys.spcs)
            {
                spc1.announcements.add(announcement);
            }

            while(spcitr.hasNext())
            {
                Document doc = spcitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);
                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                spc.updateOne(filter, updateOperationDocument);

            }

        }

        else if (announcement.an_to.equals("alumni"))
        {
            for (Alumni a:Sys.alumni)
            {
                a.announcements.add(announcement);
            }

            while(alumniitr.hasNext())
            {
                Document doc = alumniitr.next();
                ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                An.add(announcement.an_id);


                System.out.println(An);
                Bson filter = new Document("username",doc.getString("username"));
                Bson newValue = new Document("announcements",An);
                Bson updateOperationDocument = new Document("$set", newValue);
                alumni.updateOne(filter, updateOperationDocument);



            }
        }

        else
        {
            if (announcement.an_to.startsWith("spc"))
            {
                for (SPC spc1:Sys.spcs)
                {
                    spc1.announcements.add(announcement);
                }

                while(spcitr.hasNext())
                {
                    Document doc = spcitr.next();
                    ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                    An.add(announcement.an_id);

                    Bson filter = new Document("username",announcement.an_to);
                    Bson newValue = new Document("announcements",An);
                    Bson updateOperationDocument = new Document("$set", newValue);
                    spc.updateOne(filter, updateOperationDocument);

                }

            }
            else if (announcement.an_to.startsWith("a"))
            {
                for (Alumni a:Sys.alumni)
                {
                    a.announcements.add(announcement);
                }

                while(alumniitr.hasNext())
                {
                    Document doc = alumniitr.next();
                    ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                    An.add(announcement.an_id);

                    Bson filter = new Document("username",announcement.an_to);
                    Bson newValue = new Document("announcements",An);
                    Bson updateOperationDocument = new Document("$set", newValue);
                    alumni.updateOne(filter, updateOperationDocument);

                }
            }
            else if (announcement.an_to.startsWith("s"))
            {
                for (Student s:Sys.students)
                {
                    s.announcements.add(announcement);
                }

                while(studentitr.hasNext())
                {
                    Document doc = studentitr.next();
                    ArrayList<String> An = (ArrayList<String>) doc.get("announcements");
                    An.add(announcement.an_id);

                    Bson filter = new Document("username",announcement.an_to);
                    Bson newValue = new Document("announcements",An);
                    Bson updateOperationDocument = new Document("$set", newValue);
                    students.updateOne(filter, updateOperationDocument);

                }
            }
        }



        System.out.print("Announcement added successful.\n");
    }

    public static void addAnnouncement()
    {
        Announcement announcement = new Announcement();


        System.out.print("Enter announcement: ");
        announcement.an_statement = in.nextLine();
        System.out.print("Enter announcement type: ");
        announcement.an_type = in.nextLine();
        System.out.print("Enter your username: ");
        announcement.an_from = in.nextLine();
        System.out.print("Enter the announcement's receiver: ");
        announcement.an_to = in.nextLine();

        Announcement.sendAnnouncement(announcement);

        //System.out.println("");

    }

    public static void viewAnnouncement(Announcement an)
    {
        System.out.println(" "+an.an_id+" =>\t"+an.an_statement);
        System.out.println(" Announcement type: "+an.an_type);
        System.out.println(" from: "+an.an_from+"\n");
    }
}
