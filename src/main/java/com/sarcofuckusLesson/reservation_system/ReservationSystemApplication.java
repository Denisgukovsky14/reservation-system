package com.sarcofuckusLesson.reservation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*Данная аннотация обозначает, что этот класс - исполняющий, т.е
самый главный, корневой в нашем приложении*/

@SpringBootApplication
public class ReservationSystemApplication {

    /* Собственно, здесь мы и запускаем наше исполняющее приложение */
	public static void main(String[] args) {
		SpringApplication.run(ReservationSystemApplication.class, args);
	}

}
