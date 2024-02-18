package sg.nus.iss.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.repository.BlogRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogRecommendationService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BlogRepository blogRepository;
    
    public List<Blog> getRecommendations(Integer userId) {
        String url = "http://localhost:5000/recommend";
        Map<String, Integer> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        
        // 发送POST请求并接收返回的博客ID数组
        Integer[] blogIdsArray = restTemplate.postForObject(url, requestMap, Integer[].class);
        
        // 将数组转换为列表
        List<Integer> blogIds = Arrays.asList(blogIdsArray);

        // 使用Repository查询这些ID对应的博客对象
        return blogRepository.findByBlogIds(blogIds);
    }
}
