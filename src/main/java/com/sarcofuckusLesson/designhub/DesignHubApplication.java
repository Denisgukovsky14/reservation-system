package com.sarcofuckusLesson.designhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*Данная аннотация обозначает, что этот класс - исполняющий, т.е
самый главный, корневой в нашем приложении*/

@SpringBootApplication
public class DesignHubApplication {

    /* Собственно, здесь мы и запускаем наше исполняющее приложение */
	public static void main(String[] args) {
		SpringApplication.run(DesignHubApplication.class, args);
	}

}
