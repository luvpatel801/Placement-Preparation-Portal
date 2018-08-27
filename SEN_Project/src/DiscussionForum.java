import java.util.ArrayList;

public class DiscussionForum {
    ArrayList<Doubt> doubts = new ArrayList<>();
    int total_doubt;

    DiscussionForum()
    {

    }

    public static void loadDoubts()
    {
        sortDoubts();
        for (Doubt doubt : Sys.discussion_forum.doubts)
        {
            Doubt.showDoubt(doubt);
        }
    }

    public static void sortDoubts()
    {
        // Doubts are already sorted in order to time in database.
    }

    public static void postDoubt(String username)
    {
        Doubt.createDoubt(username);

        System.out.print("Doubt posted successfully.");
    }

    public static void answerDoubt(String username,String doubt_id)
    {
        Doubt doubt = null;
        for (Doubt d:Sys.discussion_forum.doubts)
        {
            if (d.doubt_id.equals(doubt_id))
            {
                doubt = d;
                break;
            }
        }

        Doubt.answerDoubt(doubt,username);

    }
}
