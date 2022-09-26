package com.explorers.smartparking.user.service.event;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.TokenEmailFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationListenerTest {


    private final TokenEmailFacade tokenEmailFacade = mock(TokenEmailFacade.class);
    private final RegistrationListener registrationListener = new RegistrationListener(tokenEmailFacade);

    @Test
    void onApplicationEvent() {
        OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(mock(User.class));

        registrationListener.onApplicationEvent(event);

        verify(tokenEmailFacade).createAndSendVerificationToken(event.getUser());
    }
}
