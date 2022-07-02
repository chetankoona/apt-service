package com.apt.aptservice;

public class Sample {
    public static void main(String[] args) {
//        printKeyStringFromRemarks();
        runSample();
    }

    private static void runSample() {

    }

    private static void printKeyStringFromRemarks() {
        String str = "eTXN/By:207310100004322/Anil\\110\\2022";
        String value = null;
        if (str.startsWith("UPI")){
            System.out.println("UPI");
            String[] split = str.split("/");
            if (split.length>=5) {
                String s = split[5];
                if (s.contains("@")){
                    value = s.split("@")[0].trim();
                }else {
                    value = s.trim();
                }
            }
        } else if (str.startsWith("NEFT")) {
            System.out.println("NEFT");
            String[] split = str.split(":");
            if (split.length>=2) {
                String s = split[1];
                value = s.substring(0, s.lastIndexOf(" "));
            }
        } else if (str.startsWith("IMPS")) {
            System.out.println("IMPS");
            value = str.substring(str.lastIndexOf("/")+1);
        } else if (str.startsWith("eTXN")) {
            System.out.println("eTXN");
            value = str.substring(str.lastIndexOf("/")+1);
        }
        System.out.println("value = "+ value);
    }
}
