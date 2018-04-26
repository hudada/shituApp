package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/4/27.
 */

public class ResultBean {

    private ArrayList<Bean> result;
    private int result_num;

    public ArrayList<Bean> getResult() {
        return result;
    }

    public void setResult(ArrayList<Bean> result) {
        this.result = result;
    }

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public class Bean{
        private String uid;
        private double[] scores;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public double[] getScores() {
            return scores;
        }

        public void setScores(double[] scores) {
            this.scores = scores;
        }
    }
}
