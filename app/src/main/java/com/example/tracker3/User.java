package com.example.tracker3;

import java.util.Map;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String cpf;
    private String jwtToken;
    private Map<UUID, String> researchesIds;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Map<UUID, String> getResearchesIds() {
        return researchesIds;
    }

    public void setResearchesIds(Map<UUID, String> researchesIds) {
        this.researchesIds = researchesIds;
    }
}
