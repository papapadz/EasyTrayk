package com.tukla.www.tukla;

public class Term {

    private String term;
    private String createdAt;
    private int ordder;

    public Term() {

    }

    public Term(String term, String createdAt, int order) {
        this.term = term;
        this.createdAt = createdAt;
        this.ordder = order;
    }

    public String getTerm() {
        return term;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getOrdder() {
        return ordder;
    }
}
