package sg.nus.iss.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.repository.BlogRepository;

@Service
@Transactional(readOnly = true)
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Override
    @Transactional(readOnly = false)
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public List<Blog> findAllPostedBlog() {
        return blogRepository.findAll();
    }

    @Override
    public List<Blog> findPostedBlogById(int uid) {
        return blogRepository.findPostedBlogById(uid);
    }

    @Override
    public List<Blog> findRecommendedBlog() {
        return blogRepository.findRecommendedBlog();
    }

    @Override
    public Blog findPostedBlogByBlogId(int bid) {
        return blogRepository.findPostedBlogByBlogId(bid);
    }

    @Override
    public List<Blog> findByblogIdIn(List<Integer> ids) {
        return blogRepository.findByblogIdIn(ids);
    }
    
}
