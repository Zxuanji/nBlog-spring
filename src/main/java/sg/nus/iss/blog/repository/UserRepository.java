package sg.nus.iss.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.blog.model.BlogUser;

public interface UserRepository extends JpaRepository<BlogUser, Integer> {
    
    @Query("SELECT u FROM BlogUser u WHERE u.displayName = :displayName")
    BlogUser findUserByDisplayName(@Param("displayName") String displayName);
}
