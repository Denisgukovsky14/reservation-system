package com.sarcofuckusLesson.designhub.Legacy;

// Service занимается обработкой логики ( вычисления, циклы, бдшки и т.д. )
// Рассмотрим на примере сайта для бронирования номеров в отеле

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    //private final Map<Long, Reservation> reservationMap ;

    //private final AtomicLong idCounter; // ССЫЛОЧНЫЙ ТИП!

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository){
        this.repository = repository;
        //reservationMap = new HashMap<>();
        //idCounter = new AtomicLong();
    }

    public List<Reservation> findAllReservations()
    {
        List<ReservationEntity> allEntities = repository.findAll();

        return allEntities.stream()
                .map(this::reservationConverter).toList();
                // упростили выражение до лямбда функции, которая сама вызовет метод с this в качестве аргумента

        //return reservationMap.values().stream().toList();
    }

    public Reservation getReservationById(Long id) {

        ReservationEntity reservationEntity = repository.findById(id)
                .orElseThrow( ()-> new EntityNotFoundException(
                        "Entity not found by ID " + id
                ))

        ;



        return reservationConverter(reservationEntity);
    }

    public Reservation createReservation( Reservation reservationToCreate ) {
        if (reservationToCreate.id() != null){

            throw new IllegalArgumentException("Id should be empty");

        }
        if (reservationToCreate.status() != null){

            throw new IllegalArgumentException("Status should be empty");

        }
        var entityToSave = new ReservationEntity(
                null, // именно null - отсутствие значения - База сама разберётся и проставит id
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING

        );

        // eTS с нуловым id пойдёт в наш репозиторий, там ему АВТОМАТИЧЕСКИ(через инкремент)
        // будет выдан id - следующий ожидаемый в таблице

        // Замечание - Автоматически сгенеренные методы в repository понимают, что
        // нам нужен именно ReservationEntity из - за правил работы - род. класса - JPA

        // Заметим, что здесь save работает на создание, а не обновление
        //мы отправляем запись без Long, он сначала выполняет Insert, ждёт пока база выдаст этот id, и только потом, репозиторий оттуда подтянет id
        var savedEntity = repository.save(entityToSave);
        return reservationConverter(savedEntity);

    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        // Reservation getreservation = new Reservation (id, reservation to update )

        var reservationEntity = repository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Not founded reservation to update by ID" + id) );

        //var oldreservation = getReservationById(id);
        if( reservationEntity.getStatus() != ReservationStatus.PENDING ){
            throw new IllegalStateException("Cannot modify Reservation with status "+ reservationEntity.getStatus());
        }
        var reservationToSave = new ReservationEntity(
                reservationEntity.GetId(),
                reservationToUpdate.userId(),
                reservationToUpdate.roomId(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );
            var updatedReservation = repository.save( reservationToSave );
            return reservationConverter(updatedReservation);
    }

    public void deleteReservation(Long id) {
        if ( !repository.existsById(id) ){
            throw new NoSuchElementException("не найден элемент с id = " + id);
        }
        System.out.println("OK");
        repository.deleteById(id);
    }

    public Reservation approvedReservation(Long id) {
        var reservationEntity = repository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Not founded reservation to update by ID" + id) );

        //var reservation = reservationMap.get(id);
        if(reservationEntity.getStatus() != ReservationStatus.PENDING ){
            throw new IllegalStateException("Cannot approved Reservation with status "+ reservationEntity.getStatus());
        }
        var isConflict = isReservationConflict(reservationEntity);
        if (isConflict){
            throw new IllegalStateException("conflict");
        }

        reservationEntity.setStatus( ReservationStatus.APPROVED);
        repository.save(reservationEntity);

        //reservationMap.put(reservation.id(), approvedReservation);
        return reservationConverter(reservationEntity) ;

    }

    private boolean isReservationConflict(
            ReservationEntity reservation
    ){

        var allReservations = repository.findAll();

        for(ReservationEntity existingReservation: allReservations  ){
            // Если это наша же заявка, ( только рассматриваемая ) сразу пропускаем
            if (reservation.GetId().equals(existingReservation.GetId())){
                continue; // завершает текущий проход по списку и сразу переходит к следующему элементу
            }
            // Если это другая комната, тоже сразу пропускаем, тут не будет проблем с датой
            if (!reservation.getRoomId().equals(existingReservation.getRoomId())){
                continue;
            }
            // Если это неподтверждённая бронь, ее тоже нет смысла проверять
            if ( !existingReservation.getStatus().equals(ReservationStatus.APPROVED) ){
                continue;
            }
            if ( reservation.getStartDate().isBefore(existingReservation.getEndDate())
            && existingReservation.getStartDate().isBefore(reservation.getEndDate()) ){
                return true;
            }
        }
        return false;
    }

    private Reservation reservationConverter(ReservationEntity it){
        return new Reservation(

                it.GetId(),
                it.getUserId(),
                it.getRoomId(),
                it.getStartDate(),
                it.getEndDate(),
                it.getStatus()
        );
    }
}
