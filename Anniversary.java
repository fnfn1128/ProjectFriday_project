package com.example.ProjectFriday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Anniversary {
    @Id
    @GeneratedValue
    private  Long id;
    @Column
    private  String title;
    @Column
    private LocalDate date;
    @Column
    private String memo;

    public long getDay(){
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(today, this.date);
    }
    public String getDayString(){
        long day = getDay();
        if (day == 0){
            return "D-day!";
        }
        else if (day > 0){
            return "D-"+ day;
        }
        else {
            return "D+" + Math.abs(day);
        }
    }
}
