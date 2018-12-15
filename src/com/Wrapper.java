package com;

import java.io.Serializable;
import java.util.ArrayList;

public class Wrapper implements Serializable {


        ArrayList<String> list;
        public ArrayList getList() {
            return list;
        }

        public void setList(ArrayList<String> list) {
            this.list = list;
        }

}
