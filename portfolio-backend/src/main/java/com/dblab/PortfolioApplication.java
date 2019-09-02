package com.dblab;

import com.dblab.domain.Introduction;
import com.dblab.domain.User;
import com.dblab.dto.IntroductionDto;
import com.dblab.dto.UserDto;
import com.dblab.repository.IntroductionRepository;
import com.dblab.repository.UserRepository;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.IntroductionService;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

}
