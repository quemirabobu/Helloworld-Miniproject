package com.example.miniprogect1.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Visit_Count")
public class VisitCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long id;

    @Column(name = "total_visit_count")
    private Integer totalVisitCount;

    @Column(name = "today_visit_count")
    private Integer todayVisitCount;

    @Column(name = "visit_date")
    private LocalDate Date = LocalDate.now();

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate = LocalDate.now();// 추가된 필드 (마지막 방문)


    @OneToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
//////여기 더할것
    @JoinColumn(name = "userName", referencedColumnName = "userName")
    private User user;

    public Integer getTotalVisitCount() {
        if (totalVisitCount == null) {
            return 0; // 또는 기본 값을 반환할 수 있습니다.
        }
        return totalVisitCount;
    }

}
