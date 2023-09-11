package com.example.engspace.model;

import java.util.ArrayList;

public class SetDetailResponseError {
    private ArrayList<String> image;
    private ArrayList<String> term;
    private ArrayList<String> definition;
    private ArrayList<String> term_lang;
    private ArrayList<String> definition_lang;

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getTerm() {
        return term;
    }

    public void setTerm(ArrayList<String> term) {
        this.term = term;
    }

    public ArrayList<String> getDefinition() {
        return definition;
    }

    public void setDefinition(ArrayList<String> definition) {
        this.definition = definition;
    }

    public ArrayList<String> getTerm_lang() {
        return term_lang;
    }

    public void setTerm_lang(ArrayList<String> term_lang) {
        this.term_lang = term_lang;
    }

    public ArrayList<String> getDefinition_lang() {
        return definition_lang;
    }

    public void setDefinition_lang(ArrayList<String> definition_lang) {
        this.definition_lang = definition_lang;
    }
}
