package com.kekguy.bitcoinprice;

public class Bitcoin {
    private float last;
    private float predicted;
    private String timestamp;

    public Bitcoin() {

    }

    public Bitcoin(float last, float predicted, String timestamp) {
        this.last = last;
        this.predicted = predicted;
        this.timestamp = timestamp;
    }

    public float getLast() {
        return last;
    }

    public void setLast(float last) {
        this.last = last;
    }

    public float getPredicted() {
        return predicted;
    }

    public void setPredicted(float predicted) {
        this.predicted = predicted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
