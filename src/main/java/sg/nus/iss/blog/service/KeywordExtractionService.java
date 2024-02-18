package sg.nus.iss.blog.service;

import org.hibernate.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.repository.BlogRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordExtractionService {

    @Autowired
    private RestTemplate restTemplate;

    public String extractKeywords(String text) {
        String url = "http://localhost:5000/extract_keywords";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("text", text);

        // 发送POST请求并接收返回的关键词字符串数组
        String[] keywordsArray = restTemplate.postForObject(url, requestMap, String[].class);


        String keywordsString = String.join(", ", keywordsArray);

        return keywordsString;

        
    }
    public List<Integer> findSimilarBlogs(String queryString) {
        String url = "http://localhost:5000/find_similar_blogs";
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("text", queryString);

        // 发送POST请求并接收返回的相似博客ID数组
        Integer[] similarBlogIdsArray = restTemplate.postForObject(url, requestMap, Integer[].class);

        // 将博客ID数组转换为List
        List<Integer> similarBlogIdsList = Arrays.asList(similarBlogIdsArray);

        return similarBlogIdsList;
    }

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> findBlogsByLabels(List<String> labels) {
        List<Blog> allBlogs = blogRepository.findAllBlogs(); 
        Map<Blog, Integer> blogLabelCount = new HashMap<>();
        
        for (Blog blog : allBlogs) {
            List<String> blogLabels = Arrays.asList(blog.getLabelList().split(",\\s*"));
            int count = 0;
            for (String label : blogLabels) {
                if (labels.contains(label)) {
                    count++;
                }
            }
            if (count > 0) {
                blogLabelCount.put(blog, count);
            }
        }

        List<Blog> orderedBlogs = new ArrayList<>();

        
        for (int i = labels.size(); i >= 1; i--) {
            for (Map.Entry<Blog, Integer> entry : blogLabelCount.entrySet()) {
                if (entry.getValue() == i) {
                    
                    orderedBlogs.add(entry.getKey());
                }
            }
        }

        return orderedBlogs;
    }

}
