import com.mongodb.*;
import com.mongodb.client.MongoDatabase;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class Dashboard {

    static Scanner in = new Scanner(System.in);

    static Student clientStudent = new Student();
    static Alumni clientAlumni = new Alumni();
    static SPC clientSPC = new SPC();


    static String username = "";
    static String password = "";

    public static void main(String[] args) throws Exception {



        Sys.connectDatabase();
        Sys.loadDatabase();



        System.out.println("------------- Welcome to placement preparation portal ----------------");

        System.out.println("1: Login");
        System.out.println("2: SignUp");
        System.out.println("0: Exit");
        System.out.print("\nEnter your choice: ");

        int choice = in.nextInt();
        int count = 1;

        while(choice!=0)
        {
            if (count++>6)
            {
                System.out.println("You have reached maximum 5 tries. Now exiting portal");
                Thread.sleep(1300);
                return;
            }


            switch (choice)
            {
                case 1:
                    System.out.print("Enter your username: ");
                    username = in.next();in.nextLine();
                    System.out.print("Enter your password: ");
                    password = in.next();in.nextLine();

                    choice = Sys.authenticate(username,password);
                    break;


                case 2:
                    /*
                    System.out.print("Enter your username: ");
                    username = in.next();in.nextLine();
                    System.out.print("Enter your password: ");
                    password = in.next();in.nextLine();
                    */

                    choice = Sys.signUp();

                    break;


                case 3:
                    System.out.println("User not found with username: "+username);
                    System.out.print("Please re-enter username: ");
                    username = in.next();in.nextLine();
                    System.out.print("Enter your password: ");
                    password = in.next();in.nextLine();

                    choice = Sys.authenticate(username,password);
                    break;


                case 4:
                    System.out.println("You entered wrong password.");
                    System.out.print("Please re-enter your password: ");
                    password = in.next();in.nextLine();

                    choice = Sys.authenticate(username,password);
                    break;


                case 9:
                    handleSPC();
                    break;


                case 8:
                    handleStudent();
                    break;


                case 7:
                    handleAlumni();
                    break;


                    default:
                        System.out.println("You entered wrong choice... Please make your choice wisely...\n");
                        System.out.println("1: Login");
                        System.out.println("2: SignUp");
                        System.out.println("0: Exit");
                        System.out.print("\nEnter your choice: ");

                        break;

            }
        }



        /*
        Company c = Sys.companies.get(0);
        Company.viewCompanyProfile(c);
        */
    }

    public static void handleStudent() {
        System.out.println("Welcome Student" + username + " !\n");

        int outer_choice = 1;
        int first_level = 1;
        int second_level = 1;
        int third_level = 1;

        Student student = new Student();

        for (Student s : Sys.students) {
            if (s.username.equals(username)) {
                student = s;
            }
        }

        out:
        while (outer_choice != 0) {

            Student.showAnnouncements(student);

            System.out.println("1: View/Apply test");
            System.out.println("2: Discussion Forum");
            System.out.println("3: Company profile");
            System.out.println("4: Student Profile");
            System.out.println("0: Exit");

            System.out.print("\nSelect a section: ");
            outer_choice = Integer.valueOf(in.nextLine());


            switch (outer_choice) {
                case 0:
                    System.exit(0);
                    break;

                case 1:
                    // testUI

                    view_test:
                    while (first_level != 0) {
                        System.out.println("\n Tests: ");
                        for (Test tst : Sys.tests) {
                            Test.viewTest(tst);
                        }

                        System.out.println("\n\n1: Apply for test");
                        System.out.println("9: Back");
                        System.out.println("0: Exit");

                        System.out.print("Enter choice: ");
                        second_level = Integer.valueOf(in.nextLine());

                        switch (second_level) {
                            case 0:
                                System.exit(0);
                                break;

                            case 9:

                                break view_test;

                            case 1:

                                System.out.print("Enter test id: ");
                                String test_id = in.nextLine();
                                Test t = new Test();

                                for (Test tst : Sys.tests) {
                                    if (tst.test_id.equals(test_id)) {
                                        t = tst;
                                        break;
                                    }
                                }

                                Test.applyForTest(student, new Test(t));

                                break;

                            default:
                                System.out.println("You entered wrong choice. please re-try\n");
                                break;
                        }
                    }

                    break;

                case 2:
                    // Discussion Forum UI

                    discussion_forum:
                    while (first_level != 0) {
                        System.out.println("\n\n Discussion Forum: \n");

                        DiscussionForum.loadDoubts();

                        System.out.println("\n\n1: Post doubt");
                        System.out.println("2: Answer doubt");
                        System.out.println("9: Back");
                        System.out.println("0: Exit");

                        System.out.print("Enter choice: ");
                        second_level = Integer.valueOf(in.nextLine());

                        switch (second_level) {
                            case 0:
                                System.exit(0);
                                break;

                            case 9:
                                break discussion_forum;

                            case 1:
                                DiscussionForum.postDoubt(username);
                                break;

                            case 2:
                                System.out.print("Enter doubt id: ");
                                String doubt_id = in.nextLine();
                                DiscussionForum.answerDoubt(username,doubt_id);
                                break;

                            default:
                                System.out.println("You entered wrong choice. please re-try\n");
                                break;
                        }
                    }
                    break;

                case 3:

                    break;

                case 4:
                    Student.viewProfile(student);
                    break;
            }


            System.exit(0);

        }
    }

    public static void handleSPC()
    {
        int outer_choice=1;
        int first_level = 1;
        int second_level = 1;
        int third_level = 1;

        System.out.println("Welcome SPC "+username+" !\n");

        SPC spc = new SPC();
        for (SPC s : Sys.spcs)
        {
            if(s.username.equals(username))
            {
                spc = s;
            }
        }


        out:
        while(outer_choice!=0)
        {

            SPC.showAnnouncements(spc);

            System.out.println("1: Test");
            System.out.println("2: Discussion Forum");
            System.out.println("3: Company profile");
            System.out.println("4: Add announcement");
            System.out.println("0: Exit");

            System.out.print("\nSelect a section: ");
            outer_choice = Integer.valueOf(in.nextLine());


            switch (outer_choice)
            {
                case 0:
                    System.exit(0);
                    break;

                case 1:

                    test_first:
                    while(first_level!=0) {

                        System.out.println("1: Add test");
                        System.out.println("2: View test");
                        System.out.println("9: Back");
                        System.out.println("0: Exit");

                        System.out.print("\nSelect a section: ");
                        first_level = Integer.valueOf(in.nextLine());

                        switch (first_level) {
                            case 0:
                                System.exit(0);
                                break;

                            case 9:
                                //going back
                                break test_first;

                            case 1:
                                // adding test

                                Test test = new Test();
                                Test.addTest(test);

                                break;

                            case 2:
                                // view Test

                                view_test:
                                while(second_level!=0) {

                                    for (Test tst: Sys.tests)
                                    {
                                        Test.viewTest(tst);
                                    }

                                    System.out.println("\n\n1: View perticular test");
                                    System.out.println("9: Back");
                                    System.out.println("0: Exit");

                                    System.out.print("Enter choice: ");
                                    second_level = Integer.valueOf(in.nextLine());

                                    switch (second_level)
                                    {
                                        case 0:
                                            System.exit(0);
                                            break;

                                        case 9:

                                            break view_test;

                                        case 1:

                                            System.out.print("Enter test id: ");
                                            String test_id = in.nextLine();
                                            Test t = new Test();

                                            for (Test tst : Sys.tests)
                                            {
                                                if (tst.test_id.equals(test_id))
                                                {
                                                    t = tst;
                                                    break;
                                                }
                                            }

                                            Test.viewWholeTest(t);

                                            break;

                                            default:
                                                System.out.println("You entered wrong choice. please re-try\n");
                                                break;
                                    }
                                }

                                break;
                        }
                    }

                    break;

                case 2:

                    discussion_forum:
                    while (first_level != 0) {
                        System.out.println("\n\n Discussion Forum: \n");

                        DiscussionForum.loadDoubts();

                        System.out.println("\n\n1: Post doubt");
                        System.out.println("2: Answer doubt");
                        System.out.println("9: Back");
                        System.out.println("0: Exit");

                        System.out.print("Enter choice: ");
                        second_level = Integer.valueOf(in.nextLine());

                        switch (second_level) {
                            case 0:
                                System.exit(0);
                                break;

                            case 9:
                                break discussion_forum;

                            case 1:
                                DiscussionForum.postDoubt(username);
                                break;

                            case 2:
                                System.out.print("Enter doubt id: ");
                                String doubt_id = in.nextLine();
                                DiscussionForum.answerDoubt(username,doubt_id);
                                break;

                            default:
                                System.out.println("You entered wrong choice. please re-try\n");
                                break;
                        }
                    }
                    break;

                case 3:
                    // Company profile UI
                    break;

                case 4:

                    Announcement.addAnnouncement();
                    break;

                    default:
                        System.out.println("You entered wrong choice. please re-try\n");
                        break;
            }
        }



        System.exit(0);

    }

    public static void handleAlumni()
    {
        System.out.println("Welcome Alumni "+username+" !");

        int outer_choice = 1;
        int first_level = 1;
        int second_level = 1;
        int third_level = 1;

        Alumni alumni = new Alumni();

        for (Alumni s : Sys.alumni) {
            if (s.username.equals(username)) {
                alumni = s;
            }
        }

        out:
        while (outer_choice != 0) {

            Alumni.showAnnouncements(alumni);

            System.out.println("1: Discussion Forum");
            System.out.println("2: Company profile");
            System.out.println("3: View Profile");
            System.out.println("0: Exit");

            System.out.print("\nSelect a section: ");
            outer_choice = Integer.valueOf(in.nextLine());


            switch (outer_choice) {
                case 0:
                    System.exit(0);
                    break;

                case 1:
                    // Discussion Forum UI

                    discussion_forum:
                    while (first_level != 0) {
                        System.out.println("\n\n Discussion Forum: \n");

                        DiscussionForum.loadDoubts();

                        System.out.println("\n\n1: Post doubt");
                        System.out.println("2: Answer doubt");
                        System.out.println("9: Back");
                        System.out.println("0: Exit");

                        System.out.print("Enter choice: ");
                        second_level = Integer.valueOf(in.nextLine());

                        switch (second_level) {
                            case 0:
                                System.exit(0);
                                break;

                            case 9:
                                break discussion_forum;

                            case 1:
                                DiscussionForum.postDoubt(username);
                                break;

                            case 2:
                                System.out.print("Enter doubt id: ");
                                String doubt_id = in.nextLine();
                                DiscussionForum.answerDoubt(username,doubt_id);
                                break;

                            default:
                                System.out.println("You entered wrong choice. please re-try\n");
                                break;
                        }
                    }
                    break;

                case 2:

                    break;

                case 3:
                    Alumni.viewProfile(alumni);
                    break;
            }


            System.exit(0);

        }


        System.exit(0);
    }
}