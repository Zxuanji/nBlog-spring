package sg.nus.iss.blog.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.Comment;
import sg.nus.iss.blog.service.BlogService;
import sg.nus.iss.blog.service.CommentService;

@Controller
public class HomeController {
       
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;

    @GetMapping("/nblog")
    public String nBlogHome(Model model) {
        List<Blog> allPostedBlogs = blogService.findAllPostedBlog();

        //sort
        Collections.sort(allPostedBlogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
                return Integer.compare(o2.getBlogLikeCount(), o1.getBlogLikeCount());
            }
        });

        //get top 3
        List<Blog> topLikedBlogs = new ArrayList<>();
        if (allPostedBlogs.size() > 3) {
            for (int i = 0; i < 3; i++) {
                topLikedBlogs.add(allPostedBlogs.get(i));
            }
        }
        else {
            topLikedBlogs = allPostedBlogs;
        }
        
        model.addAttribute("topLikedBlogs", topLikedBlogs);
        return "nblog-home";
    }

    @GetMapping("/nblog/{id}")
    public String showBlog(@PathVariable("id") Integer id, Model model){
        Blog blog = blogService.findPostedBlogByBlogId(id);
        List<Object[]> commentsData = commentService.findAllCommentsWithUserAndBlog(id);
        model.addAttribute("showedBlog", blog);
        model.addAttribute("commentsData", commentsData);
        model.addAttribute("commentPost", new Comment());
        model.addAttribute("blogId", id);
        if (!commentsData.isEmpty()) {
            System.out.println(Arrays.toString(commentsData.get(0)));
        } else {
            System.out.println("The commentsData list is empty.");
        }
        
        return "nblog-blog";
    }

}
