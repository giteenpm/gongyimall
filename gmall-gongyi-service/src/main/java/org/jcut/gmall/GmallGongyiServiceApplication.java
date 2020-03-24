package org.jcut.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages ="org.jcut.gmall.gongyi.mapper")
public class GmallGongyiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallGongyiServiceApplication.class, args);
	}

}
