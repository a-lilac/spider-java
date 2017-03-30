package com.carlos.utils;

import java.util.UUID;

/**
 * Created by Carlos on 2017/3/23.
 */
public class UUIDGeneratorUtils {
    public String uuidGenerate(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-","");
    }
}
