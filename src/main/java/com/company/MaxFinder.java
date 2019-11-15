package com.company;

import java.util.List;

public class MaxFinder implements Result {

    private Double[] data;

    MaxFinder(String data){
        String[] items = data.split(",");
        this.data = new Double[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                this.data[i] = Double.parseDouble(items[i]);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public MaxFinder(Double[] data){
        this.data = data;
    }

    public MaxFinder(List<Double> data){
        this.data = new Double[data.size()];
        for(int i = 0; i < data.size(); i++) this.data[i] = data.get(i);
    }

    @Override
    public String getResult() {
        return Double.toString(count());
    }

    private double count(){
        double max = data[0];
        for (int i = 1; i < data.length; i++)
            if (data[i] > max)
                max = data[i];
        return max;
    }
}
