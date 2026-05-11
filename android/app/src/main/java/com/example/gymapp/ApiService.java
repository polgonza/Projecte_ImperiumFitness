package com.example.gymapp;

import java.util.Map;
import retrofit2.Call;
import java.util.List;
import retrofit2.http.*;

public interface ApiService {
    @POST("api/auth/registre")
    Call<LoginResponse> registre(@Body Map<String, String> body);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body Map<String, String> body);

    @GET("api/classes")
    Call<List<ClasseDTO>> getClasses();

    @POST("api/reserves")
    Call<ReservaDTO> crearReserva(@Body ReservaDTO body);

    @GET("api/reserves/usuari/{usuariId}")
    Call<List<ReservaDTO>> getReservesUsuari(@Path("usuariId") Long usuariId);


}