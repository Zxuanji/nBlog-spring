package sg.nus.iss.blog;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.nus.iss.blog.model.BlogUserStatusEnum;
import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.BlogHistory;
import sg.nus.iss.blog.model.BlogStatusEnum;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.model.Comment;
import sg.nus.iss.blog.repository.BlogHistoryRepository;
import sg.nus.iss.blog.repository.BlogRepository;
import sg.nus.iss.blog.repository.CommentRepository;
import sg.nus.iss.blog.repository.UserRepository;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	/*@Bean
	CommandLineRunner loadData(UserRepository userRepository, BlogRepository blogRepository, BlogHistoryRepository blogHistoryRepository, CommentRepository commentRepository) {
		return (args) -> {
			// 创建 user1 
			BlogUser user1 = new BlogUser();
			user1.setDisplayName("John Doe");
			user1.setEmail("johndoe@example.com");
			user1.setPassword("34secret!!!!!!");
			user1.setSignupTime(LocalDate.of(2023,12, 2));
			user1.setProfilePicture("https://cdn.pixabay.com/photo/2016/11/21/12/42/beard-1845166_1280.jpg");
			user1.setLocation("Singapore");
			user1.setAboutMe("Software Developer");
			user1.setMyTechStack("Java, Spring Boot");
			user1.setGithubLink("https://github.com/johndoe");
			user1.setLinkedinLink("https://linkedin.com/in/johndoe");
			user1.setUserStatus(BlogUserStatusEnum.ACTIVE);
			user1.setLikedBlogIds("1,2,3,4");
			user1.setFavouriteBlogIds("1,2,3,4");

			// 创建 user2
			BlogUser user2 = new BlogUser();
			user2.setDisplayName("Dev Leader");
			user2.setEmail("devleader@email.com");
			user2.setPassword("user2Password!");
			user2.setSignupTime(LocalDate.of(2023, 11, 10));
			user2.setProfilePicture("https://cdn.pixabay.com/photo/2020/03/20/18/52/fashion-4951644_1280.jpg");
			user2.setLocation("Singapore");
			user2.setAboutMe("Software Engineer");
			user2.setMyTechStack("C#, dotnet, Visual Studio, Unity");
			user2.setGithubLink("https://github.com/ncosentino");
			user2.setLinkedinLink("https://www.linkedin.com/in/nickcosentino/");
			user2.setUserStatus(BlogUserStatusEnum.ACTIVE);
			user2.setLikedBlogIds("5,6,7");
			user2.setFavouriteBlogIds("5,6,7");

			// 创建 user3
			BlogUser user3 = new BlogUser();
			user3.setDisplayName("Yves Kalume");
			user3.setEmail("yveskalume@email.com");
			user3.setPassword("user3Password!");
			user3.setSignupTime(LocalDate.of(2024, 1, 3));
			user3.setProfilePicture("https://cdn.pixabay.com/photo/2020/04/08/07/11/model-5016031_1280.jpg");
			user3.setLocation("Singapore");
			user3.setAboutMe("Developer Expert");
			user3.setMyTechStack("Kotlin, Firebase, Github");
			user3.setGithubLink("https://github.com/yveskalume");
			user3.setLinkedinLink("https://www.linkedin.com/in/yveskalume/");
			user3.setUserStatus(BlogUserStatusEnum.ACTIVE);
			user3.setLikedBlogIds("8,9,10");
			user3.setFavouriteBlogIds("8,9,10");


			// 创建 blog1
			Blog blog1 = new Blog();
			blog1.setImage("https://cdn.pixabay.com/photo/2023/04/14/08/47/moss-7924522_1280.jpg");
			blog1.setReadingTime(5);
			blog1.setContentTitle("Spring Boot Introduction");
			blog1.setSubTitle("Getting started with Spring Boot.");
			blog1.setContent("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.Spring is a fascinating season, marked by significant changes in weather, ecology, and daylight. It is one of the four temperate seasons, succeeding winter and preceding summer. The specific characteristics and timing of spring vary significantly with local climate, cultures, and customs. Here's an overview of some key aspects of spring.Spring is characterized by a warming trend, with snow melting and an increase in average daily temperatures. This season often brings more rainfall, which, along with melting snow, can cause flooding in some regions.");
			blog1.setBlogTime("Jan 14, 2024");
			blog1.setLanguageSelected("English,Chinese");
			blog1.setTechniqueSelected("Spring Boot,Hibernate");
			blog1.setBlogStatus(BlogStatusEnum.POSTED);
			blog1.setBlogCommentCount(10);
			blog1.setBlogLikeCount(100);
			String label;
			label="java, andriod, machine learning";
			blog1.setLabeList(label);
		
			// 创建blog2
			Blog blog2 = new Blog();
			blog2.setImage("https://cdn.pixabay.com/photo/2023/11/27/21/15/birds-8416209_1280.jpg");
			blog2.setReadingTime(4);
			blog2.setContentTitle("Java Introduction");
			blog2.setSubTitle("Getting started with Java.");
			blog2.setContent("Java makes it easy to create stand-alone, production-grade Spring based Applications.Astronomical Spring: This is determined by the vernal equinox, when day and night are approximately equal in length. In the Northern Hemisphere, it usually occurs around March 20th or 21st, while in the Southern Hemisphere, it begins around September 22nd or 23rd.\n" + //
								"Meteorological Spring: For meteorological records and forecasting purposes, spring is defined as comprising the months of March, April, and May in the Northern Hemisphere, and September, October, and November in the Southern Hemisphere.");
			blog2.setBlogTime("Jan 03, 2024");
			blog2.setLanguageSelected("English,Chinese");
			blog2.setTechniqueSelected("Spring Boot,Hibernate");
			blog2.setBlogStatus(BlogStatusEnum.POSTED);
			blog2.setBlogCommentCount(135);
			blog2.setBlogLikeCount(167);
			String label2;
			label2="Java, andriod, C#";
			blog2.setLabeList(label2);

			// 创建blog3
			Blog blog3 = new Blog();
			blog3.setImage("https://cdn.pixabay.com/photo/2024/02/04/17/10/leaves-8552657_1280.jpg");
			blog3.setReadingTime(10);
			blog3.setContentTitle("Python Introduction");
			blog3.setSubTitle("Getting started with Java.");
			blog3.setContent("The arrival of spring can affect people's health in several ways. It can improve mood and physical health due to more opportunities for outdoor activities and sunlight exposure. However, it can also trigger allergies due to pollen from blooming plants.\n" + //
								"\n" + //
								"Spring's arrival is a reminder of the Earth's constant cycle of renewal and change, offering a chance to enjoy nature's beauty and prepare for the warmer months ahead.");
			blog3.setBlogTime("Jan 23, 2022");
			blog3.setLanguageSelected("English,Chinese");
			blog3.setTechniqueSelected("Spring Boot,Hibernate");
			blog3.setBlogStatus(BlogStatusEnum.POSTED);
			blog3.setBlogCommentCount(15);
			blog3.setBlogLikeCount(57);
			blog3.setLabeList(label2);

			// 创建blog4
			Blog blog4 = new Blog();
			blog4.setImage("https://cdn.pixabay.com/photo/2023/04/14/08/47/moss-7924522_1280.jpg");
			blog4.setReadingTime(3);
			blog4.setContentTitle("c Introduction");
			blog4.setSubTitle("Getting started with Java.");
			blog4.setContent("Python has two major versions that are widely used: Python 2 and Python 3. Python 3 is the future of the language and has many improvements over Python 2, but some legacy code still runs on Python 2. As of my last update, Python 2 has reached the end of its life, and no longer receives updates or support, so new projects should use Python 3.");
			blog4.setBlogTime("Jan 06, 2024");
			blog4.setLanguageSelected("English,Chinese");
			blog4.setTechniqueSelected("Python,Hibernate");
			blog4.setBlogStatus(BlogStatusEnum.POSTED);
			blog4.setBlogCommentCount(78);
			blog4.setBlogLikeCount(95);
			blog4.setLabeList(label2);

			// 创建blog5
			Blog blog5 = new Blog();
			blog5.setImage("https://cdn.pixabay.com/photo/2023/11/27/21/15/birds-8416209_1280.jpg");
			blog5.setReadingTime(18);
			blog5.setContentTitle("Simulation Introduction");
			blog5.setSubTitle("Getting started with Simulation.");
			blog5.setContent("Python's design philosophy emphasizes code readability and the importance of programmer effort over computer effort, which aligns with its motto, \"Simple is better than complex.\" This philosophy has contributed to Python's widespread adoption and its use in critical applications across various domains.");
			blog5.setBlogTime("Jan 12, 2024");
			blog5.setLanguageSelected("English, Chinese");
			blog5.setTechniqueSelected("Python,Hibernate");
			blog5.setBlogStatus(BlogStatusEnum.POSTED);
			blog5.setBlogCommentCount(78);
			blog5.setBlogLikeCount(92);
			blog5.setLabeList(label2);

			// 创建blog6
			Blog blog6 = new Blog();
			blog6.setImage("https://cdn.pixabay.com/photo/2024/02/04/17/10/leaves-8552657_1280.jpg");
			blog6.setReadingTime(3);
			blog6.setContentTitle("Spring Boot");
			blog6.setSubTitle("Getting started with Java.");
			blog6.setContent("C# has seen regular updates and enhancements since its inception, with new versions adding features to improve performance, simplify code, and enhance security. The language continues to evolve, guided by the C# Language Design Team, with input from the community, to meet modern development needs.");
			blog6.setBlogTime("Jan 18, 2024");
			blog6.setLanguageSelected("English, Chinese");
			blog6.setTechniqueSelected("Spring Boot,Hibernate");
			blog6.setBlogStatus(BlogStatusEnum.POSTED);
			blog6.setBlogCommentCount(48);
			blog6.setBlogLikeCount(99);
			blog6.setLabeList(label2);

			// 创建blog7
			Blog blog7 = new Blog();
			blog7.setImage("https://cdn.pixabay.com/photo/2023/04/14/08/47/moss-7924522_1280.jpg");
			blog7.setReadingTime(3);
			blog7.setContentTitle("Machine Learning");
			blog7.setSubTitle("Getting started with Java.");
			blog7.setContent("Machine learning (ML) is a branch of artificial intelligence (AI) focused on building systems that learn from data. Unlike traditional software programming, where developers explicitly code the decisions or behaviors based on known conditions, machine learning enables computers to learn and make decisions based on patterns and inferences from data. This approach allows for the development of models that can adapt to new data independently and perform tasks without being explicitly programmed for every possible scenario. Here's an overview of key concepts, types, and applications of machine learning:");
			blog7.setBlogTime("Jan 04, 2024");
			blog7.setLanguageSelected("English, Chinese");
			blog7.setTechniqueSelected("Spring Boot,Hibernate");
			blog7.setBlogStatus(BlogStatusEnum.POSTED);
			blog7.setBlogCommentCount(78);
			blog7.setBlogLikeCount(95);
			blog7.setLabeList(label2);

			// 创建blog8
			Blog blog8 = new Blog();
			blog8.setImage("https://cdn.pixabay.com/photo/2024/02/04/17/10/leaves-8552657_1280.jpg");
			blog8.setReadingTime(3);
			blog8.setContentTitle("Java Introduction");
			blog8.setSubTitle("Getting started with Java.");
			blog8.setContent("Python has two major versions that are widely used: Python 2 and Python 3. Python 3 is the future of the language and has many improvements over Python 2, but some legacy code still runs on Python 2. As of my last update, Python 2 has reached the end of its life, and no longer receives updates or support, so new projects should use Python 3.");
			blog8.setBlogTime("Jan 03, 2024");
			blog8.setLanguageSelected("English, Chinese");
			blog8.setTechniqueSelected("Spring Boot,Hibernate");
			blog8.setBlogStatus(BlogStatusEnum.POSTED);
			blog8.setBlogCommentCount(78);
			blog8.setBlogLikeCount(95);
			blog8.setLabeList(label2);

			// 创建blog9
			Blog blog9 = new Blog();
			blog9.setImage("https://cdn.pixabay.com/photo/2023/04/14/08/47/moss-7924522_1280.jpg");
			blog9.setReadingTime(3);
			blog9.setContentTitle("Java Introduction");
			blog9.setSubTitle("Getting started with Java.");
			blog9.setContent("Python has two major versions that are widely used: Python 2 and Python 3. Python 3 is the future of the language and has many improvements over Python 2, but some legacy code still runs on Python 2. As of my last update, Python 2 has reached the end of its life, and no longer receives updates or support, so new projects should use Python 3.");
			blog9.setBlogTime("Jan 16, 2024");
			blog9.setLanguageSelected("English, Chinese");
			blog9.setTechniqueSelected("Spring Boot,Hibernate");
			blog9.setBlogStatus(BlogStatusEnum.POSTED);
			blog9.setBlogCommentCount(78);
			blog9.setBlogLikeCount(95);
			blog9.setLabeList("Python");

			// 创建blog10
			Blog blog10 = new Blog();
			blog10.setImage("https://cdn.pixabay.com/photo/2023/11/27/21/15/birds-8416209_1280.jpg");
			blog10.setReadingTime(3);
			blog10.setContentTitle("Java Introduction");
			blog10.setSubTitle("Getting started with Java.");
			blog10.setContent("Python has two major versions that are widely used: Python 2 and Python 3. Python 3 is the future of the language and has many improvements over Python 2, but some legacy code still runs on Python 2. As of my last update, Python 2 has reached the end of its life, and no longer receives updates or support, so new projects should use Python 3.");
			blog10.setBlogTime("Jan 02, 2024");
			blog10.setLanguageSelected("English, Chinese");
			blog10.setTechniqueSelected("Spring Boot,Hibernate");
			blog10.setBlogStatus(BlogStatusEnum.POSTED);
			blog10.setBlogCommentCount(78);
			blog10.setBlogLikeCount(95);
			blog10.setLabeList("Python");



			// 创建 bloghistory1
			BlogHistory history1 = new BlogHistory();
			history1.setReadDate(LocalDate.of(2024,1,18));
			history1.setBlog(blog1);
			history1.setBlogUser(user2);

			// 创建 bloghistory2
			BlogHistory history2 = new BlogHistory();
			history2.setReadDate(LocalDate.of(2024,1,27));
			history2.setBlog(blog1);
			history2.setBlogUser(user3);

			// 创建 bloghistory3
			BlogHistory history3 = new BlogHistory();
			history3.setReadDate(LocalDate.of(2024,1,15));
			history3.setBlog(blog2);
			history3.setBlogUser(user1);

			// 创建 bloghistory4
			BlogHistory history4 = new BlogHistory();
			history4.setReadDate(LocalDate.of(2024,1,4));
			history4.setBlog(blog2);
			history4.setBlogUser(user3);

			// 创建comment1
			Comment comment1 = new Comment();
			comment1.setCommentContent("This blog is very helpful for me");
		
			// 创建 comment2
			Comment comment2 = new Comment();
			comment2.setCommentContent("Do you use java Spring or asp.net");

			// 创建 comment3
			Comment comment3 = new Comment();
			comment3.setCommentContent("Why kyrie is so handsome and charming");

			// 创建 comment4
			Comment comment4 = new Comment();
			comment4.setCommentContent("I will share this blog with my friends!");

			// 创建 comment5
			Comment comment5 = new Comment();
			comment5.setCommentContent("Everyone should go to NUS-ISS");

			// 创建 comment6
			Comment comment6 = new Comment();
			comment6.setCommentContent("I love Tin ");

			// 创建 comment7
			Comment comment7 = new Comment();
			comment7.setCommentContent("Why you say c is better than java");

			// 创建 comment8
			Comment comment8 = new Comment();
			comment8.setCommentContent("Everyone should learn java");

			// 创建 comment9
			Comment comment9 = new Comment();
			comment9.setCommentContent("I love all teachers in ISS");

			// 创建 comment10
			Comment comment10 = new Comment();
			comment10.setCommentContent("I love all teachers in ISS");

			// 创建 comment11
			Comment comment11 = new Comment();
			comment11.setCommentContent("I love all teachers in ISS");

			// 创建 comment12
			Comment comment12 = new Comment();
			comment12.setCommentContent("I love all teachers in ISS");

			// 创建 comment13
			Comment comment13 = new Comment();
			comment13.setCommentContent("I love all teachers in ISS");

			// 创建 comment14
			Comment comment14 = new Comment();
			comment14.setCommentContent("I love all teachers in ISS");

			// 创建 comment15
			Comment comment15 = new Comment();
			comment15.setCommentContent("I love all teachers in ISS");

			// 创建 comment16
			Comment comment16 = new Comment();
			comment16.setCommentContent("I love all teachers in ISS");

			// 处理user1的model关系
			user1.setPostedBlogs(Arrays.asList(blog1, blog2, blog3, blog4));
			user1.setBlogHistories(Arrays.asList(history3));
			user1.setBlogComments(Arrays.asList(comment1, comment2,comment3,comment4,comment5, comment6));
			user1.setFollowing(Arrays.asList(user3));
			user1.setFollowing(Arrays.asList(user2));

			//处理user2的model关系
			user1.setPostedBlogs(Arrays.asList(blog5, blog6, blog7));
			user2.setBlogHistories(Arrays.asList(history1));
			user2.setBlogComments(Arrays.asList(comment7, comment8,comment9,comment10));
			user2.setFollowers(Arrays.asList(user1));

			//处理user3的model关系
			user3.setPostedBlogs(Arrays.asList(blog8, blog9, blog10));
			user3.setBlogHistories(Arrays.asList(history2, history4));
			user3.setBlogComments(Arrays.asList(comment11, comment12, comment13, comment14,comment15, comment16));
			user3.setFollowers(Arrays.asList(user1));

			//处理blog1的model关系
			blog1.setBlogUser(user1);
			blog1.setBlogHistories(Arrays.asList(history1, history2));
			blog1.setBlogComments(Arrays.asList(comment1,comment2,comment3,comment4));

			//处理blog2的model关系
			blog2.setBlogUser(user1);
			blog2.setBlogHistories(Arrays.asList(history3, history4));
			blog2.setBlogComments(Arrays.asList(comment5,comment6,comment7));

			//处理blog3的model关系
			blog3.setBlogUser(user1);
			blog3.setBlogHistories(Arrays.asList(history3, history4));
			blog3.setBlogComments(Arrays.asList(comment8,comment9));

			//处理blog4的model关系
			blog4.setBlogUser(user1);
			blog4.setBlogHistories(Arrays.asList(history3, history4));
			blog4.setBlogComments(Arrays.asList(comment10));

			//处理blog5的model关系
			blog5.setBlogUser(user2);
			blog5.setBlogHistories(Arrays.asList(history3, history4));
			blog5.setBlogComments(Arrays.asList(comment11));

			//处理blog6的model关系
			blog6.setBlogUser(user2);
			blog6.setBlogHistories(Arrays.asList(history3, history4));
			blog6.setBlogComments(Arrays.asList(comment12));

			//处理blog7的model关系
			blog7.setBlogUser(user2);
			blog7.setBlogHistories(Arrays.asList(history3, history4));
			blog7.setBlogComments(Arrays.asList(comment13));

			//处理blog8的model关系
			blog8.setBlogUser(user3);
			blog8.setBlogHistories(Arrays.asList(history3, history4));
			blog8.setBlogComments(Arrays.asList(comment14));

			//处理blog9的model关系
			blog9.setBlogUser(user3);
			blog9.setBlogHistories(Arrays.asList(history3, history4));
			blog9.setBlogComments(Arrays.asList(comment15));

			//处理blog10的model关系
			blog10.setBlogUser(user3);
			blog10.setBlogHistories(Arrays.asList(history3, history4));
			blog10.setBlogComments(Arrays.asList(comment16));

			//处理comment的model关系
			comment1.setCommentBlog(blog1);
			comment1.setCommentBlogUser(user1);

			comment2.setCommentBlog(blog1);
			comment2.setCommentBlogUser(user1);

			comment3.setCommentBlog(blog1);
			comment3.setCommentBlogUser(user1);

			comment4.setCommentBlog(blog1);
			comment4.setCommentBlogUser(user1);

			comment5.setCommentBlog(blog2);
			comment5.setCommentBlogUser(user1);

			comment6.setCommentBlog(blog2);
			comment6.setCommentBlogUser(user1);

			comment7.setCommentBlog(blog2);
			comment7.setCommentBlogUser(user2);

			comment8.setCommentBlog(blog3);
			comment8.setCommentBlogUser(user2);

			comment9.setCommentBlog(blog3);
			comment9.setCommentBlogUser(user2);

			comment10.setCommentBlog(blog4);
			comment10.setCommentBlogUser(user2);

			comment11.setCommentBlog(blog5);
			comment11.setCommentBlogUser(user3);

			comment12.setCommentBlog(blog6);
			comment12.setCommentBlogUser(user3);

			comment13.setCommentBlog(blog7);
			comment13.setCommentBlogUser(user3);

			comment14.setCommentBlog(blog8);
			comment14.setCommentBlogUser(user3);

			comment15.setCommentBlog(blog9);
			comment15.setCommentBlogUser(user3);

			comment16.setCommentBlog(blog10);
			comment16.setCommentBlogUser(user3);


			// 保存history
			blogHistoryRepository.save(history1);
			blogHistoryRepository.save(history2);
			blogHistoryRepository.save(history3);
			blogHistoryRepository.save(history4);

			//保存comment
			commentRepository.save(comment1);
			commentRepository.save(comment2);
			commentRepository.save(comment3);
			commentRepository.save(comment4);
			commentRepository.save(comment5);

			//保存blogUser
			// userRepository.save(user1);
			// userRepository.save(user2);
			// userRepository.save(user3);

			//保存blog
			// blogRepository.save(blog1);
			// blogRepository.save(blog2);
		

		};
	}*/
}