package com.hubble.data.domain;

public class UTM {

    private final String url;
    private String uri;
    private final String source;
    private final String medium;
    private final String campaign;
    private final String term;
    private final String content;

    public String getUrl() {
        return url;
    }

    public String getUri() {
        return uri;
    }

    public void setLandingPageUri(String uri) {
        this.uri = uri;
    }

    public String getSource() {
        return source;
    }

    public String getMedium() {
        return medium;
    }

    public String getCampaign() {
        return campaign;
    }

    public String getTerm() {
        return term;
    }

    public String getContent() {
        return content;
    }

    public UTM(String url, String uri, String source, String medium, String campaign, String term, String content) {
        this.url = url;
        this.uri = uri;
        this.source = source;
        this.medium = medium;
        this.campaign = campaign;
        this.term = term;
        this.content = content;
    }

}
