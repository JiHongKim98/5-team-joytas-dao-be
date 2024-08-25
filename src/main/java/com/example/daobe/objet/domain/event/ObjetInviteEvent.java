package com.example.daobe.objet.domain.event;

import com.example.daobe.common.domain.DomainEvent;
import com.example.daobe.objet.domain.ObjetSharer;

public class ObjetInviteEvent implements DomainEvent {

    private static final String OBJET_NOT_CREATED_EXCEPTION_MESSAGE = "아직 생성되지 않은 오브제 입니다";

    private final Long sendUserId;
    private final Long receiveUserId;

    public ObjetInviteEvent(Long sendUserId, ObjetSharer objetSharer) {
        validate(objetSharer);
        this.sendUserId = sendUserId;
        this.receiveUserId = objetSharer.getUser().getId();
    }

    private void validate(ObjetSharer objetSharer) {
        if (objetSharer.getId() == null) {
            throw new RuntimeException(OBJET_NOT_CREATED_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public Long getSendUserId() {
        return sendUserId;
    }

    @Override
    public Long getReceiveUserId() {
        return receiveUserId;
    }
}
