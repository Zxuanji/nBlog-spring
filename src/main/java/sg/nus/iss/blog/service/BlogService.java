package sg.nus.iss.blog.service;

import java.util.List;

import sg.nus.iss.blog.model.Blog;

public interface BlogService {
    // 保存上传的blog
    Blog saveBlog(Blog blog);
    //找到所有已发送的blog
    List<Blog> findAllPostedBlog();
    // 根据用户 找到所有已上传的blog
    List<Blog> findPostedBlogById(int uid);
    //推荐blog
    List<Blog> findRecommendedBlog();
    // 根据blog Id，找到对应的blog
    Blog findPostedBlogByBlogId(int bid);
    // 根据根据一组整数ID查找所有相关的博客条目
    List<Blog> findByblogIdIn(List<Integer> ids);
}
