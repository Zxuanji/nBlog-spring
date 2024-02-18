package sg.nus.iss.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.blog.model.BlogHistory;

public interface BlogHistoryRepository extends JpaRepository<BlogHistory, Integer> {
    
}
