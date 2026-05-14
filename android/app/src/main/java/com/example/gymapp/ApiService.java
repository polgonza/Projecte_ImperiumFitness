package com.example.gymapp;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("api/productes")
    Call<List<ProducteDTO>> getProductes();

    @POST("api/vendes")
    Call<VendaDTO> crearVenda(@Body VendaDTO body);

    @GET("api/usuaris/perfil/{id}")
    Call<UsuariDTO> getPerfil(@Path("id") Long id);

    @GET("wp-json/wp/v2/noticia?_embed")
    Call<List<Noticia>> obtenerNoticias();
}