package com.logansrings.booklibrary.util;

import java.util.UUID;

public class UniqueId {

     private static long current = System.currentTimeMillis();
//    private static UUID uuid = new UUID(current, current);

     public static synchronized long getId(){
           return current++;
     }

     public static synchronized long getIDAsLong(){
           return Long.valueOf(current++);
     }
     public static synchronized String getUUID(){
           return UUID.randomUUID().toString();
     }

     public static void main(String[] args) {
           System.out.println(getId());
           System.out.println(getId());
           System.out.println(getId());
           System.out.println(getId());
           System.out.println();
           System.out.println(getIDAsLong());
           System.out.println(getIDAsLong());
           System.out.println(getIDAsLong());
           System.out.println(getIDAsLong());
           System.out.println();
           System.out.println(getUUID());
           System.out.println(getUUID());
           System.out.println(getUUID());
           System.out.println(getUUID());
     }
}
