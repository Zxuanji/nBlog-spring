package sg.nus.iss.blog.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.BlogHistory;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.service.BlogHistoryService;
import sg.nus.iss.blog.service.BlogService;
import sg.nus.iss.blog.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final int launchedYear = 2023;
    private final Month launchedMonth = Month.DECEMBER;
    private final DateTimeFormatter df1 = DateTimeFormatter.ofPattern("MMM yyyy");

    @Autowired
    UserService userService;
    @Autowired
    BlogService blogService;
    @Autowired
    BlogHistoryService blogHistoryService;

    // display main page after admin login
    @GetMapping("/main")
    public String displayDashboardMain(Model model) {
        
        LocalDate today = LocalDate.now();
        LocalDate firstDateOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        long numberOfDaysThisMonth = ChronoUnit.DAYS.between(firstDateOfMonth, today) + 1;

        String[] xDate = new String[(int) numberOfDaysThisMonth];
        for (int i = 0; i < numberOfDaysThisMonth; i++) {
            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMM d");
            xDate[i] = firstDateOfMonth.plusDays(i).format(df2);
        }
        
        int[] yBlogPost = new int[(int) numberOfDaysThisMonth];
        List<Blog> blogposts = blogService.findAllPostedBlog();
        for (int i = 0; i < numberOfDaysThisMonth; i++) {
            int postCounts = 0;
            for (Blog b: blogposts) {
                DateTimeFormatter df1 = DateTimeFormatter.ofPattern ("MMM dd, yyyy");
                LocalDate blogTime = LocalDate.parse(b.getBlogTime(), df1);
                if (blogTime.isEqual(firstDateOfMonth.plusDays(i))) {
                    postCounts += 1;
                }
            }
            yBlogPost[i] = postCounts;
        }

        List<Integer> yDataUser = new ArrayList<>();
        List<BlogUser> users = userService.findAllActiveUsers();
        for (int i = 0; i < numberOfDaysThisMonth; i++) {
            int numOfUsers = 0;
            for (BlogUser u: users) {
                if (u.getSignupTime().isBefore(firstDateOfMonth.plusDays(i)) || u.getSignupTime().isEqual(firstDateOfMonth.plusDays(i))) {
                    System.out.println(firstDateOfMonth.plusDays(i));
                    numOfUsers += 1;
                }
            }
            yDataUser.add(numOfUsers);
            System.out.println(numOfUsers);
        }

        List<BlogUser> yTopBlogUser = yTopBlogUserByPost(today);
        List<String> yTopBlogUserDisplayName = new ArrayList<>();
        List<String> yProfilePicture = new ArrayList<>();
        List<Integer> xTopNumBlogPost = new ArrayList<>();
        for (BlogUser u: yTopBlogUser) {
            yTopBlogUserDisplayName.add(u.getDisplayName());
            yProfilePicture.add(u.getProfilePicture());
            xTopNumBlogPost.add(u.getPostedBlogs().size());
        }

        //header
        model.addAttribute("allMonths", getAllMonths());
        model.addAttribute("month", YearMonth.from(today).format(df1));
        //row2
        model.addAttribute("xDate", xDate);
        model.addAttribute("yBlogPost", yBlogPost);
        model.addAttribute("yDataUser", yDataUser);
        //row3
        model.addAttribute("xCountry", xCountry(today));
        model.addAttribute("yUserCountry", yUserCountry(today));
        model.addAttribute("xTopNumBlogPost", xTopNumBlogPost);
        model.addAttribute("yTopBlogUserByPost", yTopBlogUserDisplayName);
        model.addAttribute("yProfilePicture", yProfilePicture);
        model.addAttribute("techniquesSelected", allTechniquesSelected(today));
        //row4
        model.addAttribute("monthActiveUsers", monthActiveUser(today));
        model.addAttribute("monthUserGrowth", monthUserGrowth(today));
        model.addAttribute("monthPostReadCount", monthPostReadCount(today));
        return "admin-dashboard";
    }

    // display admin selected month
    @GetMapping("/main/{m}")
    public String displayDashboardPast(@PathVariable("m") String monthSelected, Model model) {
        LocalDate today = LocalDate.now();
        YearMonth todayYearMonth = YearMonth.from(today);
        
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMM yyyy");
        YearMonth yearMonth = YearMonth.parse(monthSelected, df2);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<BlogUser> yTopBlogUser = yTopBlogUserByPost(lastDayOfMonth);
        List<String> yTopBlogUserDisplayName = new ArrayList<>();
        List<String> yProfilePicture = new ArrayList<>();
        List<Integer> xTopNumBlogPost = new ArrayList<>();
        for (BlogUser u: yTopBlogUser) {
            yTopBlogUserDisplayName.add(u.getDisplayName());
            yProfilePicture.add(u.getProfilePicture());
            xTopNumBlogPost.add(u.getPostedBlogs().size());
        }

        if (!todayYearMonth.equals(yearMonth)) {
            //header
            model.addAttribute("allMonths", getAllMonths());
            model.addAttribute("month", YearMonth.from(lastDayOfMonth).format(df1));
            //blogpost
            model.addAttribute("xDate", datesOfMonth(lastDayOfMonth));
            model.addAttribute("yBlogPost", monthBlogPostPerDay(lastDayOfMonth));
            model.addAttribute("yDataUser", monthUserPerDay(lastDayOfMonth));
            //row3
            model.addAttribute("xCountry", xCountry(lastDayOfMonth));
            model.addAttribute("yUserCountry", yUserCountry(lastDayOfMonth));
            model.addAttribute("xTopNumBlogPost", xTopNumBlogPost);
            model.addAttribute("yTopBlogUserByPost", yTopBlogUserDisplayName);
            model.addAttribute("yProfilePicture", yProfilePicture);
            model.addAttribute("techniquesSelected", allTechniquesSelected(lastDayOfMonth));
            //user
            model.addAttribute("monthActiveUsers", monthActiveUser(lastDayOfMonth));
            model.addAttribute("monthUserGrowth", monthUserGrowth(lastDayOfMonth));
            model.addAttribute("monthPostReadCount", monthPostReadCount(lastDayOfMonth));
            return "admin-dashboard";
        }
        else {
            return "redirect:/admin/main";
        }
    }

    public LocalDate getPreviousMonth(LocalDate date) {
        // dashboard data cut off date is end of last month
        // get the first day of the month and then subtract one day to get the last day of the previous month
        return date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

    // all months since the our application is launched
    public List<String> getAllMonths() {
        List<String> months = new ArrayList<>();
        LocalDate lastMonth = LocalDate.now();
        LocalDate launchedDate = LocalDate.of(launchedYear, launchedMonth, 1);
        
        while (YearMonth.from(launchedDate).isBefore(YearMonth.from(lastMonth)) || YearMonth.from(launchedDate).equals(YearMonth.from(lastMonth))) {
            if (!months.contains(YearMonth.from(launchedDate).format(df1))) {
                months.add(YearMonth.from(launchedDate).format(df1));
            }
            launchedDate = launchedDate.plusMonths(1);
        }
        return months;
    }

    // for x-axis of Daily Blog Post graph and Daily User graph
    public String[] datesOfMonth(LocalDate lastDayOfMonth) {
        YearMonth yearMonth = YearMonth.from(lastDayOfMonth);
        String[] xDate = new String[yearMonth.lengthOfMonth()];
        for (int i = 0; i < yearMonth.lengthOfMonth(); i++) {
            DateTimeFormatter df2 = DateTimeFormatter.ofPattern("MMM d");
            xDate[i] = lastDayOfMonth.minusDays(yearMonth.lengthOfMonth() - 1 - i).format(df2);
        }
        return xDate;
    }

    // for y-axis of Daily Blog Post graph
    public int[] monthBlogPostPerDay(LocalDate lastDayOfMonth) {
        List<Blog> blogposts = blogService.findAllPostedBlog();

        YearMonth yearMonth = YearMonth.from(lastDayOfMonth);
        int[] yData = new int[yearMonth.lengthOfMonth()];
        for (int i = 0; i < yearMonth.lengthOfMonth(); i++) {
            int postCounts = 0;
            for (Blog b: blogposts) {
                DateTimeFormatter df1 = DateTimeFormatter.ofPattern ("MMM dd, yyyy");
                LocalDate blogTime = LocalDate.parse(b.getBlogTime(), df1);
                if (blogTime.isEqual(lastDayOfMonth.minusDays(yearMonth.lengthOfMonth() - i))) {
                    postCounts += 1;
                }
            }
            yData[i] = postCounts;
        }
        return yData;
    }

    // number of active users as at the selected month
    public int monthActiveUser(LocalDate lastDayOfMonth) {
        int activeUsers = 0;
        for (BlogUser u: userService.findAllActiveUsers()) {
            if(u.getSignupTime().isBefore(lastDayOfMonth) || u.getSignupTime().isEqual(lastDayOfMonth)) {
                activeUsers += 1;
            }
        }
        return activeUsers;
    }

    // users growth as compared to the previous month
    public String monthUserGrowth(LocalDate lastDayOfMonth) {
        LocalDate previousMonth = getPreviousMonth(lastDayOfMonth);
        List<BlogUser> users = userService.findAllActiveUsers();

        List<BlogUser> usersMonth = new ArrayList<>();
        List<BlogUser> usersPreviousMonth = new ArrayList<>();
        for (BlogUser u: users) {
            if (u.getSignupTime().isBefore(lastDayOfMonth) || u.getSignupTime().isEqual(lastDayOfMonth)) {
                usersMonth.add(u);
            }
            if (u.getSignupTime().isBefore(previousMonth) || u.getSignupTime().isEqual(previousMonth)) {
                usersPreviousMonth.add(u);
            }
        }
        double monthUserGrowth = usersPreviousMonth.size() != 0 ? (double) (usersMonth.size() - usersPreviousMonth.size()) / usersPreviousMonth.size() * 100 : 0;
        String formattedGrowth = String.format("%.2f%%", monthUserGrowth);
        return formattedGrowth;
    }

    // total number of times user read a blog post for the selected month
    public int monthPostReadCount(LocalDate lastDayOfMonth) {
        LocalDate previousMonth = getPreviousMonth(lastDayOfMonth);
        List<BlogHistory> histories = blogHistoryService.findAllBlogHistories();

        List<BlogHistory> historiesOfMonth = new ArrayList<>();
        for (BlogHistory h: histories) {
            if (h.getReadDate().isBefore(lastDayOfMonth) || h.getReadDate().isEqual(lastDayOfMonth) && h.getReadDate().isAfter(previousMonth)) {
                historiesOfMonth.add(h);
            }
        }
        return historiesOfMonth.size();
    }

    // techniques selected in blog posts
    public List<String> allTechniquesSelected(LocalDate lastDayOfMonth) {
        List<Blog> allPostedBlogs = blogService.findAllPostedBlog();
        List<Blog> allPostedBlogsBefore = new ArrayList<>();

        for (Blog b: allPostedBlogs) {
            DateTimeFormatter df1 = DateTimeFormatter.ofPattern ("MMM dd, yyyy");
            LocalDate blogTime = LocalDate.parse(b.getBlogTime(), df1);
            if (blogTime.isBefore(lastDayOfMonth) || blogTime.isEqual(lastDayOfMonth)) {
                allPostedBlogsBefore.add(b);
            }
        }

        List<String> allTechniques = new ArrayList<>();
        for (Blog b: allPostedBlogsBefore) {
            String[] userTechniques = new String[]{};
            if (b.getTechniqueSelected() != null) {
                userTechniques = b.getTechniqueSelected().split(",");
            }
            for (String t: userTechniques) {
                allTechniques.add(t);
            }
        }
        return allTechniques;
    }

    // for y-axis of Daily User graph
    public List<Integer> monthUserPerDay(LocalDate lastDayOfMonth) {
        List<BlogUser> users = userService.findAllActiveUsers();
        YearMonth yearMonth = YearMonth.from(lastDayOfMonth);
        List<Integer> yData = new ArrayList<>();
        for (int i = 0; i < yearMonth.lengthOfMonth(); i++) {
            int numOfUsers = 0;
            for (BlogUser u: users) {
                if (u.getSignupTime().isBefore(lastDayOfMonth.minusDays(yearMonth.lengthOfMonth() - 1 - i)) || u.getSignupTime().isEqual(lastDayOfMonth.minusDays(yearMonth.lengthOfMonth() - 1 - i))) {
                    System.out.println(lastDayOfMonth.minusDays(yearMonth.lengthOfMonth() - 1 - i));
                    numOfUsers += 1;
                }
                
            }
            yData.add(numOfUsers);
            System.out.println(numOfUsers);
        }
        return yData;
    }

    // for x-axis of User Country graph
    public List<String> xCountry(LocalDate lastDayOfMonth) { 
        List<String> xCountry = new ArrayList<>();
        List<BlogUser> users = userService.findAllActiveUsers();

        for (BlogUser u: users) {
            if (u.getSignupTime().isBefore(lastDayOfMonth) || u.getSignupTime().isEqual(lastDayOfMonth)) {
                String uCountry = u.getLocation();
                String uCountryFormatted;
                if (!uCountry.equals("")) {
                    uCountryFormatted = uCountry.substring(0, 1).toUpperCase() + uCountry.substring(1).toLowerCase();
                }
                else {
                    uCountryFormatted = "";
                }
                if (!xCountry.contains(uCountryFormatted)) {
                    xCountry.add(uCountryFormatted);
                }
            }
        }
        return xCountry;
    }

    // for y-axis of User Country graph
    public List<Integer> yUserCountry(LocalDate lastDayOfMonth) {
        List<Integer> yUserCountry = new ArrayList<>();
        List<String> countries = xCountry(lastDayOfMonth);
        List<BlogUser> users = userService.findAllActiveUsers();

        for (int i = 0; i < countries.size(); i++) {
            int numberOfUserFromCountry = 0;
            for (BlogUser u: users) {
                if (u.getLocation().equalsIgnoreCase(countries.get(i))) {
                    numberOfUserFromCountry += 1;
                }
            }
            yUserCountry.add(numberOfUserFromCountry);
        }
        return yUserCountry;
    }

    // for x-axis of Top Blog User Post
    public List<BlogUser> yTopBlogUserByPost(LocalDate lastDayOfMonth) {
        List<BlogUser> yTopBlogUserByPost = new ArrayList<>();
        List<BlogUser> users = userService.findAllActiveUsers();
        List<BlogUser> usersBefore = new ArrayList<>();

        for (BlogUser u: users) {
            if (u.getSignupTime().isBefore(lastDayOfMonth) || u.getSignupTime().isEqual(lastDayOfMonth)) {
                usersBefore.add(u);
            }
        }
        Collections.sort(usersBefore, new Comparator<BlogUser>() {
            @Override
            public int compare(BlogUser o1, BlogUser o2) {
                return Integer.compare(o1.getPostedBlogs().size(), o2.getPostedBlogs().size());
            }
        });
        if (usersBefore.size() < 3) {
            for (int i = 0; i < usersBefore.size(); i++) {
                yTopBlogUserByPost.add(usersBefore.get(i));
            }
        }
        else {
            for (int i = 0; i < 3; i++) {
                yTopBlogUserByPost.add(usersBefore.get(i));
            }
        }
        return yTopBlogUserByPost;
    }
}