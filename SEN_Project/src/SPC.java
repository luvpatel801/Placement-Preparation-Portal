import java.util.*;

public class SPC {
    String username;
    String member_name;
    String member_email_id;
    String member_contact;
    String member_address;
    ArrayList<Announcement> announcements = new ArrayList<>();

    SPC()
    {

    }

    public static void showAnnouncements(SPC spc)
    {
         System.out.print("Notifications for you: \n\n");

         for (Announcement an : spc.announcements)
         {
             an.viewAnnouncement(an);
         }

         System.out.println("--------------------------------------");
    }

    public boolean updateNotifications()
    {
        boolean sucess = false;

        return sucess;
    }

}