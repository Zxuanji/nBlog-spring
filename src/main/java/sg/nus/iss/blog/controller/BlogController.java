package sg.nus.iss.blog.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.BlogHistory;
import sg.nus.iss.blog.model.BlogStatusEnum;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.model.Comment;
import sg.nus.iss.blog.repository.BlogRepository;
import sg.nus.iss.blog.service.BlogHistoryService;
import sg.nus.iss.blog.service.BlogHistoryServiceImpl;
import sg.nus.iss.blog.service.BlogRecommendationService;
import sg.nus.iss.blog.service.BlogService;
import sg.nus.iss.blog.service.CommentService;
import sg.nus.iss.blog.service.KeywordExtractionService;
import sg.nus.iss.blog.service.UserService;

@Controller
@RequestMapping("/home")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogHistoryService blogHistoryService;

    @Autowired
    private BlogRecommendationService blogRecommendationService;

    @Autowired
    private KeywordExtractionService keywordExtractionService;

    @Autowired
    private KeywordExtractionService keywordExtractionService1;

    @Autowired
    private KeywordExtractionService keywordExtractionService2;

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/api/recommend/list/{userId}")
    public ResponseEntity<List<Blog>> getRecommendedBlogs(@PathVariable("userId") int userId) {
        List<Blog> recommendBlogs = blogRecommendationService.getRecommendations(userId);
        List<Blog> allPostedBlogs = blogService.findAllPostedBlog();
        Collections.sort(allPostedBlogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
                return Integer.compare(o2.getBlogLikeCount(), o1.getBlogLikeCount());
            }
        });

        if (recommendBlogs.size() == 0) {
            return new ResponseEntity<>(toSimpleBlogs(allPostedBlogs), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(toSimpleBlogs(recommendBlogs), HttpStatus.OK);
        }
    }
    

@GetMapping("/api/search/{query}")
public ResponseEntity<List<Blog>> apiSearchBlogs(@PathVariable("query") String query) {
    String searchResults0 = keywordExtractionService1.extractKeywords(query);
    List<String> blogLabels = Arrays.asList(searchResults0.split(",\\s*"));
    List<Blog> searchresults = new ArrayList<>();
    if (!blogLabels.isEmpty()) {
        searchresults = keywordExtractionService.findBlogsByLabels(blogLabels);
        return new ResponseEntity<>(toSimpleBlogs(searchresults), HttpStatus.OK);
    } else {
        // 如果根据标签没有找到任何博客，则调用 findSimilarBlogs 方法
        List<Integer> similarBlogIds = keywordExtractionService1.findSimilarBlogs(query);

        // 使用返回的博客ID列表查询对应的博客
        searchresults = blogRepository.findByBlogIds(similarBlogIds);
        return new ResponseEntity<>(toSimpleBlogs(searchresults), HttpStatus.OK);
    }
}

    // 首页
    @GetMapping("/list")
    public String recommendBlog(Model model, HttpSession sessionObj) {
        // List<Blog> allPostedBlogs = blogService.findAllPostedBlog();
        BlogUser activeUser = (BlogUser) sessionObj.getAttribute("activeUser");
        int userId = activeUser.getUserId();
        List<Blog> recommendBlogs = blogRecommendationService.getRecommendations(userId);

        // sort all blogs by liked
        List<Blog> allPostedBlogs = blogService.findAllPostedBlog();
        Collections.sort(allPostedBlogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
                return Integer.compare(o2.getBlogLikeCount(), o1.getBlogLikeCount());
            }
        });
        if (recommendBlogs.size() == 0) {
            model.addAttribute("recommendBlogs", allPostedBlogs);
        } else {
            model.addAttribute("recommendBlogs", recommendBlogs);
        }

        // model.addAttribute("allPostedBlogs", allPostedBlogs);
        model.addAttribute("activeUser", activeUser);
        return "home-page";
    }

    // 点击写博客，呈现博客界面

    @GetMapping("/create")
    public String createBlog(Model model, HttpSession session) {
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        model.addAttribute("blog", new Blog());
        model.addAttribute("activeUser", user);
        return "blog-create";
    }
    // 处理搜索逻辑

    @GetMapping("/search")
    public String searchBlogs(@RequestParam("query") String query, Model model, HttpSession session) {
        String searchResults0 = keywordExtractionService1.extractKeywords(query);
        List<String> blogLabels = Arrays.asList(searchResults0.split(",\\s*"));
        List<Blog> searchresults = new ArrayList<>();
        if (!blogLabels.isEmpty()) {
            searchresults = keywordExtractionService.findBlogsByLabels(blogLabels);
        } else {
            // 如果根据标签没有找到任何博客，则调用 findSimilarBlogs 方法
            List<Integer> similarBlogIds = keywordExtractionService1.findSimilarBlogs(query);

            // 使用返回的博客ID列表查询对应的博客
            searchresults = blogRepository.findByBlogIds(similarBlogIds);
        }
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        model.addAttribute("activeUser", user);
        model.addAttribute("searchresults", searchresults);
        model.addAttribute("searchresults", searchresults);
        return "search-results"; // gggggggggggggggggg
    }

    // 用户编写并上传blog
    @PostMapping("/create")
    public String postBlog(@ModelAttribute("blogPost") Blog blog, HttpSession session, Model model) {
        blog.setReadingTime(calculateReadingTime(0));
        blog.setBlogTime(getPostTime());
        blog.setBlogStatus(BlogStatusEnum.POSTED);
        blog.setBlogLikeCount(countLikes());
        blog.setBlogCommentCount(countComments());

        model.addAttribute("blog", blog);
        blog.setLabeList(keywordExtractionService2.extractKeywords(blog.getContent()));

        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        int userId = user.getUserId();
        BlogUser currentUser = userService.findUserById(userId).get();

        List<Blog> userPostedBlog = currentUser.getPostedBlogs();
        if (userPostedBlog == null) {
            userPostedBlog = new ArrayList<>();
            userPostedBlog.add(blog);
        } else {
            userPostedBlog.add(blog);
        }

        // 存储用户和博客之间的对应关系
        blog.setBlogUser(currentUser);
        currentUser.setPostedBlogs(userPostedBlog);

        blogService.saveBlog(blog);
        userService.createUser(currentUser);

        return "redirect:/home/create/history";
    }

    // 用户上传评论
    @PostMapping("/comment")
    public String postComment(@ModelAttribute("commentPost") Comment comment, @RequestParam("blogId") int blogId,
            HttpSession session) {
        // 获取当前user
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        BlogUser currentuser = userService.findUserById(user.getUserId()).get();
        // 获取当前blog
        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        // 保存comment
        comment.setCommentBlogUser(currentuser);
        comment.setCommentBlog(blog);

        // 更新user
        currentuser.getBlogComments().add(comment);

        // // 更新blog
        blog.getBlogComments().add(comment);

        commentService.saveComment(comment);
        return "redirect:/home/show/" + blogId;
    }

    // 个人发博客历史
    @GetMapping("/create/history")
    public String createPostHistory(HttpSession session, Model model) {
        BlogUser user = (BlogUser) session.getAttribute("activeUser");

        int userId = user.getUserId();

        List<Blog> postedList = blogService.findPostedBlogById(userId);
        if (postedList == null) {
            postedList = new ArrayList<Blog>();
        }

        model.addAttribute("postedList", postedList);
        model.addAttribute("activeUser", user);
        // model.addAttribute("userName", userName);

        return "blog-history";
    }

    // 查看书签记录
    @GetMapping("/bookmark")
    public String viewBookMark(HttpSession session, Model model) {
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        BlogUser currentUser = userService.findUserById(user.getUserId()).get();

        List<String> FavouritedBlogList = new ArrayList<>(Arrays.asList(currentUser.getFavouriteBlogIds().split(",")));

        List<Integer> favouritedBlogListInt = FavouritedBlogList.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(s -> Integer.parseInt(s)) // 将每个字符串转换为整数
                .collect(Collectors.toList()); // 收集结果到一个新的 List

        List<Blog> FavouritedBlog = blogService.findByblogIdIn(favouritedBlogListInt);

        model.addAttribute("FavouritedBlog", FavouritedBlog);

        model.addAttribute("activeUser", currentUser);
        return "blog-bookmark";
    }

    // 看其他人的发博客历史
    @GetMapping("/@{userDisplayName}")
    public String createPostHistoryOfSelectedUser(@PathVariable("userDisplayName") String displayName, Model model) {
        BlogUser viewUser = userService.findUserByDisplayName(displayName);
        List<Blog> viewUserPosts = blogService.findPostedBlogById(viewUser.getUserId());
        model.addAttribute("postedList", viewUserPosts);

        return "home";
    }

    // 根据用户点击的文章，显示对应的文章
    @GetMapping("/show/{id}")
    public String showBlog(@PathVariable Integer id, Model model, HttpSession session) {
        // 找到当前blog
        Blog blog = blogService.findPostedBlogByBlogId(id);
        // 找到当前用户
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        int userId = user.getUserId();
        // 找到当前的user
        BlogUser currentUser = userService.findUserById(userId).get();

        if (currentUser.getFavouriteBlogIds() == null) {
            currentUser.setFavouriteBlogIds(new String());
        }

        if (currentUser.getLikedBlogIds() == null) {
            currentUser.setLikedBlogIds(new String());
        }
        // 评论区部分数据
        List<Object[]> commentsData = commentService.findAllCommentsWithUserAndBlog(id);

        boolean isliked = isLiked(currentUser.getLikedBlogIds(), blog.getBlogId());
        boolean isfavourited = isFavourited(currentUser.getFavouriteBlogIds(), blog.getBlogId());
        BlogHistory blogHistory = new BlogHistory();
        blogHistory.setBlog(blog);
        blogHistory.setBlogUser(currentUser);
        blogHistory.setReadDate(LocalDate.now());
        System.out.println(blogHistory + "1132132113213213213131999999");
        blogService.saveBlog(blog);
        userService.saveUser(currentUser);
        blogHistoryService.saveBlogHistory(blogHistory);

        model.addAttribute("isliked", isliked);
        model.addAttribute("isfavourited", isfavourited);
        model.addAttribute("likecount", blog.getBlogLikeCount());
        model.addAttribute("showedBlog", blog);
        model.addAttribute("commentsData", commentsData);
        model.addAttribute("commentPost", new Comment());
        model.addAttribute("blogId", id);
        model.addAttribute("activeUser", user);

        return "blog-page";
    }

    // 处理用户点赞逻辑
    @PostMapping("/like")
    public String handleLike(@RequestParam("blogId") int blogId, @RequestParam("isliked") boolean isliked,
            HttpSession session, Model model) {

        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        int userId = user.getUserId();
        // 找到当前的user
        BlogUser currentUser = userService.findUserById(userId).get();

        // 更新blog的点赞数 和 user的点赞blog list
        if (!isliked) {
            blog.setBlogLikeCount(blog.getBlogLikeCount() + 1);
            currentUser.setLikedBlogIds(currentUser.getLikedBlogIds() + "," + blog.getBlogId());
        } else {
            blog.setBlogLikeCount(blog.getBlogLikeCount() - 1);
            List<String> likedBlogIdList = new ArrayList<>(Arrays.asList(currentUser.getLikedBlogIds().split(",")));
            likedBlogIdList.remove(String.valueOf(blogId));
            String updatedLikedBlogIds = String.join(",", likedBlogIdList);
            currentUser.setLikedBlogIds(updatedLikedBlogIds);
        }

        userService.saveUser(currentUser);
        blogService.saveBlog(blog);

        return "redirect:/home/show/" + blogId;
    }

    // 处理收藏逻辑
    @PostMapping("/favourite")
    public String handleFavourite(@RequestParam("blogId") int blogId,
            @RequestParam("isfavourited") boolean isfavourited, HttpSession session, Model model) {

        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        BlogUser user = (BlogUser) session.getAttribute("activeUser");
        int userId = user.getUserId();
        // 找到当前的user
        BlogUser currentUser = userService.findUserById(userId).get();

        // 更新user的收藏 blog list
        if (!isfavourited) {

            if (currentUser.getFavouriteBlogIds().isEmpty()) {
                currentUser.setFavouriteBlogIds(String.valueOf(blog.getBlogId()));
            } else
                currentUser.setFavouriteBlogIds(currentUser.getFavouriteBlogIds() + "," + blog.getBlogId());

        } else {
            List<String> favouritedBlogIdList = new ArrayList<>(
                    Arrays.asList(currentUser.getFavouriteBlogIds().split(",")));
            favouritedBlogIdList.remove(String.valueOf(blogId));
            String updatedFavouritedBlogIds = String.join(",", favouritedBlogIdList);
            currentUser.setFavouriteBlogIds(updatedFavouritedBlogIds);
        }

        userService.saveUser(currentUser);
        blogService.saveBlog(blog);

        return "redirect:/home/show/" + blogId;

    }

    // 查看 blog是否已经点赞了
    public boolean isLiked(String likedBlogIds, int blogId) {
        String numberAsString = Integer.toString(blogId);

        if (likedBlogIds.contains(numberAsString)) {
            return true;
        } else {
            return false;
        }
    }

    // 查看 blog是否已经收藏了
    public boolean isFavourited(String favouritedBlogIds, int blogId) {
        String numberAsString = Integer.toString(blogId);

        if (favouritedBlogIds.contains(numberAsString)) {
            return true;
        } else {
            return false;
        }
    }

    // 返回格式化的创建时间
    public String getPostTime() {

        LocalDate createdTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = createdTime.format(formatter);

        return formattedDate;
    }

    // 根据文章字数计算阅读时间
    public int calculateReadingTime(int words) {
        return words / 200;
    }

    // 计算点赞数量（待完成）
    public int countLikes() {
        return 0;
    }

    // 计算评论数量（待完成）
    public int countComments() {
        return 0;
    }

    @GetMapping("/api/getmany/{userId}")
    public ResponseEntity<List<Blog>> getMany(@PathVariable("userId") int userId) {
        BlogUser currentUser = userService.findUserById(userId).get();

        List<String> FavouritedBlogList = new ArrayList<>(Arrays.asList(currentUser.getFavouriteBlogIds().split(",")));

        List<Integer> favouritedBlogListInt = FavouritedBlogList.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());

        List<Blog> FavouritedBlog = blogService.findByblogIdIn(favouritedBlogListInt);
        List<Blog> simpleBlogs = toSimpleBlogs(FavouritedBlog);
        return new ResponseEntity<>(simpleBlogs, HttpStatus.OK);
    }

    @GetMapping("/api/list")
    public ResponseEntity<List<Blog>> findAllPostedBlog() {
        List<Blog> blogs = blogService.findAllPostedBlog();
        List<Blog> simpleBlogs = toSimpleBlogs(blogs);

        return new ResponseEntity<>(simpleBlogs, HttpStatus.OK);
    }

    @PostMapping("/api/create/{id}")
    public ResponseEntity<String> createBlog(@RequestBody Blog newBlog, @PathVariable("id") int id) {
        try {
            Optional<BlogUser> user = userService.findUserById(id);
            newBlog.setBlogUser(user.get());
            newBlog.setLabeList(keywordExtractionService2.extractKeywords(newBlog.getContent()));
            blogService.saveBlog(newBlog);
            return ResponseEntity.ok("Blog created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/api/create/history/{id}")
    public ResponseEntity<List<List<Blog>>> getPostHistory(@PathVariable("id") int id) {

        List<Blog> postedList = blogService.findPostedBlogById(id);

        if (postedList == null) {
            postedList = new ArrayList<Blog>();
        }
        List<Blog> simpleBlogs = toSimpleBlogs111(postedList);
        List<List<Blog>> list = groupBlogByTime(simpleBlogs);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/api/getone/{blogId}")
    public ResponseEntity<Blog> getMethodName(@PathVariable("blogId") int blogId) {
        Blog blog = toSimpleBlog(blogService.findPostedBlogByBlogId(blogId));
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    public Blog toSimpleBlog(Blog blog) {
        List<Comment> simpleComments = new ArrayList<>();
        BlogUser user = blog.getBlogUser();
        BlogUser simpleUser = new BlogUser(user.getDisplayName(), user.getEmail(), user.getPassword(),
                user.getSignupTime(), user.getProfilePicture(), user.getProfileTagline(), user.getLocation(),
                user.getAboutMe(), user.getMyTechStack(), user.getGithubLink(), user.getLinkedinLink(),
                user.getUserStatus(), null, null, null, null, user.getLikedBlogIds(), user.getFavouriteBlogIds());

        simpleUser.setUserId(user.getUserId());

        Blog simpleBlog = new Blog(blog.getImage(), blog.getReadingTime(), blog.getContentTitle(),
                blog.getSubTitle(),
                blog.getContent(), blog.getBlogTime(), blog.getLanguageSelected(), blog.getTechniqueSelected(),
                blog.getBlogStatus(), blog.getBlogCommentCount(), blog.getBlogLikeCount(), null, new ArrayList<>(),
                simpleUser);
        simpleBlog.setBlogId(blog.getBlogId());

        for (Comment comment : blog.getBlogComments()) {
            Comment simpleComment = new Comment();
            simpleComment.setCommentContent(comment.getCommentContent());
            simpleComment.setCommentId(comment.getCommentId());
            simpleComment.setCommentBlog(null);

            BlogUser blogUser = new BlogUser();
            comment.getCommentBlogUser();
            blogUser.setDisplayName((comment.getCommentBlogUser().getDisplayName()));
            blogUser.setUserId(comment.getCommentBlogUser().getUserId());
            blogUser.setProfilePicture(comment.getCommentBlogUser().getProfilePicture());

            simpleComment.setCommentBlogUser(blogUser);
            simpleComments.add(simpleComment);
        }
        simpleBlog.setBlogComments(simpleComments);

        return simpleBlog;
    }

    public List<Blog> toSimpleBlogs111(List<Blog> blogs) {
        List<Blog> simpleBlogs = new ArrayList<>();

        for (Blog blog : blogs) {
            List<Comment> simpleComments = new ArrayList<>();
            // List<BlogHistory> blogHistory = blog.getBlogHistories();
            BlogUser user = blog.getBlogUser();
            BlogUser simpleUser = new BlogUser(user.getDisplayName(), user.getEmail(), user.getPassword(),
                    user.getSignupTime(), user.getProfilePicture(), user.getProfileTagline(), user.getLocation(),
                    user.getAboutMe(), user.getMyTechStack(), user.getGithubLink(), user.getLinkedinLink(),
                    user.getUserStatus(), null, null, null, null, user.getLikedBlogIds(), user.getFavouriteBlogIds());

            simpleUser.setUserId(user.getUserId());

            Blog simpleBlog = new Blog(blog.getImage(), blog.getReadingTime(), blog.getContentTitle(),
                    blog.getSubTitle(),
                    blog.getContent(), blog.getBlogTime(), blog.getLanguageSelected(), blog.getTechniqueSelected(),
                    blog.getBlogStatus(), blog.getBlogCommentCount(), blog.getBlogLikeCount(), null, new ArrayList<>(),
                    simpleUser);

            simpleBlog.setBlogId(blog.getBlogId());

            for (Comment comment : blog.getBlogComments()) {
                Comment simpleComment = new Comment();
                simpleComment.setCommentContent(comment.getCommentContent());
                simpleComment.setCommentId(comment.getCommentId());
                simpleComment.setCommentBlog(null);

                BlogUser blogUser = new BlogUser();
                comment.getCommentBlogUser();
                blogUser.setDisplayName((comment.getCommentBlogUser().getDisplayName()));
                blogUser.setUserId(comment.getCommentBlogUser().getUserId());

                simpleComment.setCommentBlogUser(blogUser);
                simpleComments.add(simpleComment);
            }
            simpleBlog.setBlogComments(simpleComments);
            simpleBlogs.add(simpleBlog);
        }
        return simpleBlogs;
    }

    public List<Blog> toSimpleBlogs(List<Blog> blogs) {
        List<Blog> simpleBlogs = new ArrayList<>();

        for (Blog blog : blogs) {
            // List<BlogHistory> blogHistory = blog.getBlogHistories();
            BlogUser user = blog.getBlogUser();
            BlogUser simpleUser = new BlogUser(user.getDisplayName(), user.getEmail(), user.getPassword(),
                    user.getSignupTime(), user.getProfilePicture(), user.getProfileTagline(), user.getLocation(),
                    user.getAboutMe(), user.getMyTechStack(), user.getGithubLink(), user.getLinkedinLink(),
                    user.getUserStatus(), null, null, null, null, user.getLikedBlogIds(), user.getFavouriteBlogIds());
            simpleUser.setUserId(user.getUserId());

            Blog simpleBlog = new Blog(blog.getImage(), blog.getReadingTime(), blog.getContentTitle(),
                    blog.getSubTitle(),
                    blog.getContent(), blog.getBlogTime(), blog.getLanguageSelected(), blog.getTechniqueSelected(),
                    blog.getBlogStatus(), blog.getBlogCommentCount(), blog.getBlogLikeCount(), new ArrayList<>(),
                    new ArrayList<>(), simpleUser);
            simpleBlog.setBlogId(blog.getBlogId());

            simpleBlogs.add(simpleBlog);
        }
        return simpleBlogs;
    }

    // @PostMapping
    // public ResponseEntity<String> createComment(String content,int blogId,int
    // userId){

    // }

    public List<List<Blog>> groupBlogByTime(List<Blog> blogs) {
        List<Blog> group1 = new ArrayList<>(); // 当天发的
        List<Blog> group2 = new ArrayList<>(); // 一天外三天内发的
        List<Blog> group3 = new ArrayList<>(); // 三天外七天内发的
        List<Blog> group4 = new ArrayList<>(); // 七天外发的

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");// 根据你的日期格式进行调整
        LocalDate now = LocalDate.now();

        for (Blog blog : blogs) {
            LocalDate blogDate = LocalDate.parse(blog.getBlogTime(), formatter);
            long daysBetween = ChronoUnit.DAYS.between(blogDate, now);

            if (daysBetween == 0) {
                group1.add(blog);
            } else if (daysBetween <= 3) {
                group2.add(blog);
            } else if (daysBetween <= 7) {
                group3.add(blog);
            } else {
                group4.add(blog);
            }
        }

        List<List<Blog>> blogList = new ArrayList<>();
        blogList.add(group1);
        blogList.add(group2);
        blogList.add(group3);
        blogList.add(group4);

        return blogList;
    }

    @PostMapping("/api/comment")
    public ResponseEntity<String> createComment(String commentContent, int blogId, int userId) {
        // 获取当前user
        BlogUser currentuser = userService.findUserById(userId).get();
        // 获取当前blog
        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        // 保存comment
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        comment.setCommentBlogUser(currentuser);
        comment.setCommentBlog(blog);

        // 更新user
        currentuser.getBlogComments().add(comment);

        // // 更新blog
        blog.getBlogComments().add(comment);

        commentService.saveComment(comment);
        return ResponseEntity.ok("okok");
    }

    @PostMapping("/api/like")
    public ResponseEntity<String> likeFromMobile(boolean isliked, int blogId, int userId) {
        // 当前blog
        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        // 找到当前的user
        BlogUser currentUser = userService.findUserById(userId).get();

        // 更新blog的点赞数 和 user的点赞blog list
        if (!isliked) {
            blog.setBlogLikeCount(blog.getBlogLikeCount() + 1);
            currentUser.setLikedBlogIds(currentUser.getLikedBlogIds() + "," + blog.getBlogId());
        } else {
            blog.setBlogLikeCount(blog.getBlogLikeCount() - 1);
            List<String> likedBlogIdList = new ArrayList<>(Arrays.asList(currentUser.getLikedBlogIds().split(",")));
            likedBlogIdList.remove(String.valueOf(blogId));
            String updatedLikedBlogIds = String.join(",", likedBlogIdList);
            currentUser.setLikedBlogIds(updatedLikedBlogIds);
        }

        userService.saveUser(currentUser);
        blogService.saveBlog(blog);

        return ResponseEntity.ok("okok");
    }

    @PostMapping("/api/favorite")
    public ResponseEntity<String> favoriteFromMobile(boolean isfavorited, int blogId, int userId) {

        Blog blog = blogService.findPostedBlogByBlogId(blogId);
        BlogUser currentUser = userService.findUserById(userId).get();

        // 更新user的收藏 blog list
        if (!isfavorited) {
            currentUser.setFavouriteBlogIds(currentUser.getFavouriteBlogIds() + "," + blog.getBlogId());
        } else {
            List<String> favouritedBlogIdList = new ArrayList<>(
                    Arrays.asList(currentUser.getFavouriteBlogIds().split(",")));
            favouritedBlogIdList.remove(String.valueOf(blogId));
            String updatedFavouritedBlogIds = String.join(",", favouritedBlogIdList);
            currentUser.setFavouriteBlogIds(updatedFavouritedBlogIds);
        }

        userService.saveUser(currentUser);
        blogService.saveBlog(blog);

        return ResponseEntity.ok("okok");
    }
}
