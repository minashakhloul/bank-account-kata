package com.bank.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Client {
    private final String id = UUID.randomUUID().toString();
    private String firstname;
    private String lastname;

}
