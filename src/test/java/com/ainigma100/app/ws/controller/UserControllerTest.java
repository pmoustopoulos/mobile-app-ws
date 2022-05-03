package com.ainigma100.app.ws.controller;

import com.ainigma100.app.ws.entity.UserEntity;
import com.ainigma100.app.ws.service.AddressService;
import com.ainigma100.app.ws.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// @WebMvcTest annotation will load all the components required
// to test the UserController. It will not load the service or repository layer components
@WebMvcTest
class UserControllerTest {

    // we are going to use MockMvc to call REST APIs
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper is used to serialize and deserialize Java Objects
    @Autowired
    private ObjectMapper objectMapper;


    // @MockBean tell Spring to create a mock instance of UserService
    // and add it to application context so that it is injected into UserController
    @MockBean
    private UserService userService;

    @MockBean
    private AddressService addressService;


    private UserEntity userEntity1;

    @BeforeEach
    void setUp() {
        userEntity1 = UserEntity.builder()
                .id("123")
                .firstName("Marco")
                .lastName("Polo")
                .email("mpolo@gmail.com")
                .encryptedPassword("abc123")
                .emailVerificationToken("vertoken123")
                .emailVerificationStatus(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void givenId_whenDeleteUser_thenReturn200() throws Exception {

        // given - precondition or setup
        String id = "123";
        willDoNothing().given(userService).deleteUserById(id);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/users/{id}", id));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());

    }


//    @Test
//    public void givenUserSearchCriteria_whenGetUsersUsingPagination_thenReturnUserDetailsResponseModel() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenId_whenGetUserByUserId_thenReturnUserDetailsResponseModel() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//    @Test
//    public void givenUserDetailsRequestModel_whenCreateUser_thenReturnUserDetailsResponseModel() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//    @Test
//    public void givenIdAndUserDetailsRequestModel_whenUpdateUser_thenReturnUserDetailsResponseModel() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//
//
//
//    @Test
//    public void givenUserId_whenGetAddressesByUserId_thenReturnAddressResponseModelList() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenUserIdAndAddressId_whenGetUserAddressByAddressId_thenReturnAddressResponseModel() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenPasswordResetRequestModel_whenRequestPasswordReset_thenReturn200() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenPasswordResetModel_whenResetPassword_thenReturn200() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenNoInput_whenGetExcelReportAsEncodedValue_thenReturnFileResponse() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenNoInput_whenDownloadExcelReport_thenReturnInputStreamResource() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }
//
//
//    @Test
//    public void givenToken_whenVerifyEmailToken_thenReturn200() {
//
//        // given - precondition or setup
//
//
//        // when - action or behaviour that we are going to test
//
//
//        // then - verify the output
//
//
//    }

}