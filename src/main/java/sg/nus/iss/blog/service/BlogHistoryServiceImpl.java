package sg.nus.iss.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.blog.model.BlogHistory;
import sg.nus.iss.blog.repository.BlogHistoryRepository;

@Service
@Transactional(readOnly=true)
public class BlogHistoryServiceImpl implements BlogHistoryService {

    @Autowired
    private BlogHistoryRepository blogHistoryRepository;

    @Override
    public List<BlogHistory> findAllBlogHistories() {
        return blogHistoryRepository.findAll();
    }
    @Override
    @Transactional(readOnly = false)
    public BlogHistory saveBlogHistory(BlogHistory blogHistory) {
        // 使用Repository的save方法保存BlogHistory对象
        return blogHistoryRepository.save(blogHistory);
    }
    
    
}
