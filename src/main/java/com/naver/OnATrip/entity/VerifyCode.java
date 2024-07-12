package com.naver.OnATrip.entity;

import com.naver.OnATrip.util.RandomGenerator;
import com.naver.OnATrip.web.dto.member.VerifyCodeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Getter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCode extends Timestamped {

    @Id
    @Column(name = "code")
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.naver.OnATrip.util.RandomGenerator")
    String code;

    @Column(name = "email", unique = true)
    String email;

    public VerifyCode(VerifyCodeDto codeDto){
        this.email = codeDto.getEmail();
    }
}
