package com.example.engspace.model;

import java.util.Date;

public class SetDetailResponse {
    private int id;
    private String image;
    private String term;
    private String definition;
    private String term_lang;
    private String definition_lang;
    private Date date_created;
    private Date date_updated;
    private int set;
    private SetDetailResponseError setDetailResponseError;
    private int type;

    public SetDetailResponse() {
    }

    public SetDetailResponse(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public SetDetailResponse(int id, String term, String definition) {
        this.id = id;
        this.term = term;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTerm_lang() {
        return term_lang;
    }

    public void setTerm_lang(String term_lang) {
        this.term_lang = term_lang;
    }

    public String getDefinition_lang() {
        return definition_lang;
    }

    public void setDefinition_lang(String definition_lang) {
        this.definition_lang = definition_lang;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public SetDetailResponseError getSetDetailResponseError() {
        return setDetailResponseError;
    }

    public void setSetDetailResponseError(SetDetailResponseError setDetailResponseError) {
        this.setDetailResponseError = setDetailResponseError;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
