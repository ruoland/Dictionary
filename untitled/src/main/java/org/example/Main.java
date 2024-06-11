package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            Path path = Files.createDirectories(Path.of("D:\\Projects\\CustomClientForFabric\\run\\dictionary"));
            for(String str : path.toFile().list()){
                str = str.toUpperCase().replace(".JSON", "");;
                String NAME = "public static final String "+str + "_NAME = \"dictionary."+str.toLowerCase()+".name\";";
                String DICTIONARY ="public static final String "+str + "_DICTIONARY  =\"dictionary."+str.toLowerCase()+".dictionary\";";

                String NAME_IN_LANG = "\"dictionary."+str.toLowerCase()+".name\":\""+str+"\",";

                String DICTIONARY_IN_LANG ="\"dictionary."+str.toLowerCase()+".dictionary\":\""+str+"\",";
                System.out.println(NAME_IN_LANG);
                System.out.println(DICTIONARY_IN_LANG);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}