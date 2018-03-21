package main;

import java.security.SecureRandom;

public class Tools {

    static private SecureRandom rnd = new SecureRandom();

    //generator of randomString
    public static  String keyIDCreator(int length) {

        StringBuilder sb = new StringBuilder( length );

        for( int i = 0; i < length; i++ ) {
            sb.append(Constants.AB.charAt(rnd.nextInt(Constants.AB.length())));
        }
        return sb.toString();
    }

    //generate int from Numbers double
    public static int toInt (Number num){
        return num.intValue();
    }
}
