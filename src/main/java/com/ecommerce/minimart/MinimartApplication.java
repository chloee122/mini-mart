package com.ecommerce.minimart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.ecommerce.minimart.domain.User;
import com.ecommerce.minimart.domain.UserRepository;

@SpringBootApplication
@EnableJpaAuditing
public class MinimartApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinimartApplication.class, args);
	}

	@Bean
	public CommandLineRunner minimartDemo(UserRepository urepository) {
		return (args) -> {
			// Create users: admin/admin123 user/user
			User existingUser1 = urepository.findByUsername("user");
			User existingUser2 = urepository.findByUsername("admin");

			if (existingUser1 == null && existingUser2 == null) {

				User user1 = new User("user",
						"$2a$10$yRU8kLR7BHLbgVO52ptyNeTtz0QpG3OQxo3Tot7qDkcUHsRJw2VmG", "USER");
				User user2 = new User("admin",
						"$2a$10$YV5IZp/H9.iz8YO9NYAoEOa1Rl8h2O.0odFFYCsCMt4aU3alpKJz2", "ADMIN");

				urepository.save(user1);
				urepository.save(user2);
			}
		};
	}

}
