package dev.pratishtha.project.CartService.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class DateRangeDTO {

    private LocalDate startDate;
    private LocalDate endDate;

}
