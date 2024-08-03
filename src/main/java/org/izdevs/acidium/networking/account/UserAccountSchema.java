package org.izdevs.acidium.networking.account;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserAccountSchema {
    public enum opType{
        REGISTER,
        LOGIN,
        DESTROY
    }
    opType type;
    String username;
    String password;
}
