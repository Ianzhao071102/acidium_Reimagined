package org.izdevs.acidium.networking.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProtocolOperation {
    OperationType type;
    String uuid;
    int revision = -1;
    Object additionalPayload;
}
