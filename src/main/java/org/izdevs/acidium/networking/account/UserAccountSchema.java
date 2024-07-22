package org.izdevs.acidium.networking.account;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAccountSchema {
    enum opType{
        REGISTER,
        LOGIN,
        DESTROY
    }
    opType type;
    String username;
    String password;
}
