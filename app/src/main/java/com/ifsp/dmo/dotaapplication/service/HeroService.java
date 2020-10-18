package com.ifsp.dmo.dotaapplication.service;

import com.ifsp.dmo.dotaapplication.model.BaseEntity;
import com.ifsp.dmo.dotaapplication.model.Hero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author vinicius.montouro
 */
public interface HeroService {

    @GET("heroes")
    Call<List<BaseEntity>> findHeroes();
}
