package com.explorers.smartparking.user.web.controller;

import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.RegistrationService;
import com.explorers.smartparking.user.service.TokenEmailFacade;
import com.explorers.smartparking.user.service.TokenService;
import com.explorers.smartparking.user.service.event.OnRegistrationCompleteEvent;
import com.explorers.smartparking.user.web.dto.ForgotPasswordDto;
import com.explorers.smartparking.user.web.dto.RegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Mock
    private ApplicationListener<OnRegistrationCompleteEvent> listener;

    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private TokenEmailFacade tokenEmailFacade;
    @MockBean(name = "messageSource")
    private MessageSource messages;

    @BeforeEach
    void setUp() {
        configurableApplicationContext.addApplicationListener(listener);
    }

    @Test
    void registerUserAccount() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setFirstName("John");
        registrationDto.setLastName("Doe");
        registrationDto.setEmail("test@test.test");
        registrationDto.setPassword("test1234");
        registrationDto.setMatchingPassword("test1234");

        when(registrationService.registerNewUserAccount(registrationDto)).thenReturn(new User());
        when(messages.getMessage(eq("message.user.accountRegistered"), any(), any(Locale.class)))
                .thenReturn("Registration successful");

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("registrationDto", registrationDto)
                        .with(csrf())
                )
                .andExpect(status().isCreated())

                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("message"));

        verify(registrationService).registerNewUserAccount(registrationDto);
        verify(listener).onApplicationEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    void registerUserAccountWithInvalidData() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto();

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("registrationDto", registrationDto)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(view().name("registration"))
                .andDo(result -> {
                            BindingResult bindingResult = (BindingResult)
                                    Objects.requireNonNull(result.getModelAndView())
                                            .getModel()
                                            .get("org.springframework.validation.BindingResult.registrationDto");

                            assertNotNull(bindingResult, "BindingResult is null");
                            assertTrue(bindingResult.hasErrors(), "BindingResult has no errors");

                            HashMap<String, ArrayList<String>> errors = getErrorsMapFromBindingResult(bindingResult);

                            assertTrue(errors.get("firstName").contains("NotBlank"));
                            assertTrue(errors.get("lastName").contains("NotBlank"));
                            assertTrue(errors.get("email").contains("NotBlank"));
                            assertTrue(errors.get("password").contains("NotBlank"));
                            assertTrue(errors.get("matchingPassword").contains("NotBlank"));
                        }
                );

        registrationDto.setFirstName("1");
        registrationDto.setLastName("1");
        registrationDto.setEmail("test");
        registrationDto.setPassword("test");

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("registrationDto", registrationDto)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andDo(result -> {
                            BindingResult bindingResult = (BindingResult)
                                    Objects.requireNonNull(result.getModelAndView())
                                            .getModel()
                                            .get("org.springframework.validation.BindingResult.registrationDto");

                            assertNotNull(bindingResult, "BindingResult is null");
                            assertTrue(bindingResult.hasErrors(), "BindingResult has no errors");

                            HashMap<String, ArrayList<String>> errors = getErrorsMapFromBindingResult(bindingResult);

                            assertTrue(errors.get("firstName").contains("Size"));
                            assertTrue(errors.get("lastName").contains("Size"));
                            assertTrue(errors.get("email").contains("Pattern"));
                            assertTrue(errors.get("password").contains("ValidPassword"));
                            assertTrue(errors.get("registrationDto").contains("PasswordMatches"));
                        }
                );
    }

    @Test
    void confirmRegistration() throws Exception {
        when(messages.getMessage(eq("message.user.accountVerified"), any(), any(Locale.class)))
                .thenReturn("Account activated");

        mockMvc.perform(get("/registrationConfirm")
                        .param("token", "test")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("message", "Account activated"));

        verify(registrationService).enableUser("test");
    }

    @Test
    void resendRegistrationToken() throws Exception {
        when(messages.getMessage(eq("message.user.tokenResent"), any(), any(Locale.class)))
                .thenReturn("An activation email has been sent to your inbox");

        mockMvc.perform(get("/resendRegistrationToken")
                        .param("email", "test")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("message", "An activation email has been sent to your inbox"));

        verify(tokenEmailFacade).updateAndSendVerificationToken("test");
    }

    @Test
    void resetPassword() throws Exception {
        when(messages.getMessage(eq("message.user.resetPassEmailSent"), any(), any(Locale.class)))
                .thenReturn("A password reset email has been sent to your inbox");

        mockMvc.perform(post("/sendPassResetToken")
                        .param("email", "test")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("A password reset email has been sent to your inbox"));

        verify(tokenEmailFacade).createAndSendPasswordResetToken("test");
    }

    @Test
    void updateForgottenPassword() throws Exception {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto("test");
        forgotPasswordDto.setNewPassword("test1234");
        forgotPasswordDto.setMatchingPassword("test1234");

        when(messages.getMessage(eq("message.user.updatePasswordSuc"), any(), any(Locale.class)))
                .thenReturn("Password updated");

        mockMvc.perform(post("/updateForgottenPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("forgotPasswordDto", forgotPasswordDto)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Password updated"));

        verify(registrationService).changeForgottenPassword(forgotPasswordDto);
    }

    //TODO extract to separate util class
    private HashMap<String, ArrayList<String>> getErrorsMapFromBindingResult(BindingResult bindingResult) {
        HashMap<String, ArrayList<String>> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(error -> {
            String field = error.getField();
            String code = error.getCode();
            if (errors.containsKey(field)) {
                errors.get(field).add(code);
            } else {
                ArrayList<String> messages = new ArrayList<>();
                messages.add(code);
                errors.put(field, messages);
            }
        });
        bindingResult.getGlobalErrors().forEach(error -> {
            String field = error.getObjectName();
            String code = error.getCode();
            if (errors.containsKey(field)) {
                errors.get(field).add(code);
            } else {
                ArrayList<String> messages = new ArrayList<>();
                messages.add(code);
                errors.put(field, messages);
            }
        });
        return errors;
    }
}
