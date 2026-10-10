package com.designhub.Legacy;

// Controller занимается обработкой HTTP запросов
// в Controller необходимо сделать инъекцию Service ( обработчика логики )

import com.designhub.TestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

// Пометка Rest перед Controller позволяет отправлять и получать HTTP запросы из main.http
// так как эта метка обеспечивает коннект с любым клиетом, в отличии Controller, ограниченным
// браузером

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService; /// инъекция Service

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/api/test")
    public ResponseEntity<?> handleTest(@RequestBody TestRequest request) {
        String receivedText = request.getText();
        System.out.println("Получено от клиента: " + receivedText);
        log.info("Получено от клиента: " + receivedText);
        return ResponseEntity.ok(Map.of(
                "message", "Сервер получил: " + receivedText
        ));
    }

    @GetMapping("/home")
    public String home() {
        return "forward:/pages/home/home.html";
    }

    // Метод возвращающий все бронирования
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservation(
    ){
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    // Метод возвращающий бронирования по id
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable("id") Long id
    ){
        log.info( "Called Reservation by id = " + id );
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(reservationService.getReservationById(id));
        }
        catch ( NoSuchElementException e){
            return ResponseEntity.status(404).build();
        }
    }

    // Метод создающий бронирование
    // Обёртка ResponseEntity позволяет получать подробную обратную связь по ответам HTTP запросов и менять их в ручную
    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservationToCreate
    ){

        log.info("Called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("test-header","123")
                .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("id") Long id, // Spring автоматически парсит id в переменную
            @RequestBody Reservation reservationToUpdate // Аналогично Spring и тут парсит body из запроса

    ){
        log.info("called Method UpdateReservation for id={}",id);
        try {
            var updated = reservationService.updateReservation(id, reservationToUpdate);
            return ResponseEntity.ok(updated);
        }
        catch ( NoSuchElementException e){
            return ResponseEntity.status(404).build();
            // build, потому что метод не выполнился, ничего не выдалось
        }


        // Почему иногда пишется build() после ResponseEntity, а иногда нет ?
        // .build нужен, когда метод не возвращает данные, например метод ниже возвращает Void
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable("id") Long id
    ){
        log.info("Called Reservation with id={}",id);

        try
        {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
        }
        catch ( NoSuchElementException e)
        {
            return ResponseEntity.status(500).build();
        }


    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approvedReservation(
            @PathVariable("id") Long id
    ){
        log.info("Called approvedReservation: id={}",id);
        var reservation = reservationService.approvedReservation(id);
        return ResponseEntity.ok(reservation);
    }

}
