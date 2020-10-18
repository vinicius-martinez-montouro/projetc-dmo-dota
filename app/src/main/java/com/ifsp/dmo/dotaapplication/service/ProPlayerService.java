package com.ifsp.dmo.dotaapplication.service;

import com.ifsp.dmo.dotaapplication.model.BaseEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author vinicius.montouro
 */
public interface ProPlayerService {

    @GET("proPlayers")
    Call<List<BaseEntity>> findProPlayers();
}
