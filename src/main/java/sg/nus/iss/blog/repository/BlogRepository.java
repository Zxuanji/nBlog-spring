package sg.nus.iss.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.blog.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Integer>{
    // 随机展示推荐blog（暂时）
    @Query("SELECT b FROM Blog b ORDER BY RAND()")
    List<Blog> findRecommendedBlog();
    // 根据用户 找到所有已上传的blog
    @Query("SELECT b FROM Blog b WHERE b.blogUser.userId = :uid")
    List<Blog> findPostedBlogById(@Param("uid") int uid);
    // 根据blog Id，找到对应的blog
    @Query("SELECT b FROM Blog b WHERE b.blogId = :bid")
    Blog findPostedBlogByBlogId(@Param("bid") int bid);
    // 根据一组整数ID查找所有相关的博客条目
    List<Blog> findByblogIdIn(List<Integer> ids);
    @Query("SELECT b FROM Blog b")
    List<Blog> findAllBlogs();
    @Query("SELECT b FROM Blog b WHERE b.blogId IN :ids")
    List<Blog> findByBlogIds(@Param("ids") List<Integer> ids);


}
