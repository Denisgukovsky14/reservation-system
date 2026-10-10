package com.designhub.Legacy;

import java.time.LocalDate;


public record Reservation(

        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status

) {

    public Reservation(Reservation existing, Long newId) {
        this(newId, existing.userId(), existing.roomId(), existing.startDate(), existing.endDate(), existing.status());
    }

}


