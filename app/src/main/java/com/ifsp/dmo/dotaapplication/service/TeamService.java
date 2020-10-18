package com.ifsp.dmo.dotaapplication.service;

import com.ifsp.dmo.dotaapplication.model.BaseEntity;
import com.ifsp.dmo.dotaapplication.model.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author vinicius.montouro
 */
public interface TeamService {

    @GET("teams")
    Call<List<BaseEntity>> findTeams();
}
