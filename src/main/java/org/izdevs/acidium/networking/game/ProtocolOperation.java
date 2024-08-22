package org.izdevs.acidium.networking.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProtocolOperation<T> {
    OperationType type;
    String uuid;
    int revision = -1;
    T additionalPayload;

    String username;
    String password;
}
