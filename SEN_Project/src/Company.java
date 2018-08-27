import java.util.ArrayList;

public class Company {
    String company_id;
    String company_name;
    double cpi_criteria;
    int open_jobs;
    ArrayList<String> job_type_salary = new ArrayList<>();      // separated by _
    String website_link;
    double rating;

    Company() {

    }

    public static void viewCompanyProfile(Company c)
    {

        System.out.print("Company id: "+c.company_id+"\t");
        System.out.println("Company name: "+c.company_name);
        System.out.println("CPI criteria: "+c.cpi_criteria);
        System.out.println("website_link: "+c.website_link);
        System.out.println("Company rating by alumni: "+c.rating);
        System.out.println("Open jobs: "+c.open_jobs);
        for (String s:c.job_type_salary)
        {
            System.out.println(s);
        }

        System.out.println();
    }

    public static boolean createCompanyProfile()
    {
        boolean sucess = false;

        return sucess;
    }

    public static boolean updateCompanyProfile()
    {
        boolean sucess = false;
        return sucess;
    }

    public static boolean giveFeedback()
    {
        boolean sucess = false;

        return sucess;
    }
}
