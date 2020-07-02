package kr.inhatc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

// 첨부파일과 관련된 자동 구성을 사용하지 않도록 설정
@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
public class MyProject3Application {

	public static void main(String[] args) {
		SpringApplication.run(MyProject3Application.class, args);
	}

}
