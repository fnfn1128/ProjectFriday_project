package com.example.ProjectFriday.dto;


import com.example.ProjectFriday.entity.Anniversary;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@AllArgsConstructor
public class AnniversaryForm {
    private Long id;
    private String title;
    private String date;
    private String memo;

    public Long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getMemo(){
        return memo;
    }


    public Anniversary toEntity() {
        return new Anniversary(id, title, LocalDate.parse(date), memo);
    }

}
