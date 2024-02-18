package sg.nus.iss.blog.service;

import java.util.List;

import sg.nus.iss.blog.model.BlogHistory;

public interface BlogHistoryService {
    
    List<BlogHistory> findAllBlogHistories();
    BlogHistory saveBlogHistory(BlogHistory blogHistory);
}
