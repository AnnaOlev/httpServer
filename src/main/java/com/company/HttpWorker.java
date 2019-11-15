package com.company;

import models.Dog;
import services.DogService;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpWorker implements Runnable{

    private Socket socket;
    private List<Dog> dogs;
    private DogService dogService = new DogService();

    HttpWorker(Socket socket, List<Dog> dogs) {
        this.socket = socket;
        this.dogs = dogs;
    }

    @Override
    public void run() {
        try {
            handleRequest();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleRequest() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            serverRequest(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void serverRequest(InputStream inputStream, OutputStream outputStream) throws Exception {
        String line;
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        int contentLength = -1;
        boolean check = false;
        boolean dogCheck = false;
        boolean deleteCheck = false;
        boolean changeCheck = false;

        while ((line = bf.readLine())!=null) {
            System.out.println(line);
            if (line.startsWith("GET")) {
                String data = line.split(" ")[1].substring(1);
                populateResponse(outputStream, data);
            }
            if (line.startsWith("POST")){
                check=true;
                if (line.contains("newdog"))
                    dogCheck = true;
                if (line.contains("deletedog"))
                    deleteCheck = true;
                if (line.contains("changedog"))
                    changeCheck = true;

            }
            final String contentLengthStr = "Content-Length: ";
            if (line.startsWith(contentLengthStr)) {
                contentLength = Integer.parseInt(line.substring(contentLengthStr.length()));
            }

            if (line.length() == 0) {
                break;
            }
        }
        if (check) {
            final char[] content = new char[contentLength];
            int num = bf.read(content);
            String data = new String(content);

            System.out.println(data + " " + num);
            if (dogCheck) {
                int id = dogAdder(data);
                data = "dogishere" + id;
            }
            if (deleteCheck) {
                dogDelete(data);
                data = "deleted";
            }
            if (changeCheck){
                dogChange(data);
                data = "changed";
            }
            populateResponse(outputStream, data);
        }
    }

    private void populateResponse(OutputStream outputStream, String data) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]+,)+");
        Pattern pattern1 = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(data);
        Matcher matcher1 = pattern1.matcher(data);
        String base;
        String content = "text/html;charset=UTF-8";
        if (matcher.find() && !matcher1.find()) {
            MaxFinder maxFinder = new MaxFinder(data);
            base = maxFinder.getResult();
        }
        else if (data.contains("page2"))
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\page2.html")));

        else if (data.contains("getresult")) {
            base = new String((Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\getresult.js"))));
            content = "text/javascript";
        }
        else if (data.contains("page3"))
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\page3.html")));

        else if (data.contains("styles")) {
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\styles.css")));
            content = "text/css";
        }
        else if (data.contains("tablework")) {
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\tablework.js")));
        }
        else if (data.contains("dogishere")) {
            base = data.replaceAll("dogishere", "");
        }
        else if (data.contains("deleted")) {
            base = "OK";
        }
        else if (data.contains("changed")) {
            base = "OK";
        }
        else if (data.contains("givemedog")) {
            StringBuilder sb = new StringBuilder();
            for (Dog dog : dogs) {
                sb.append(dog.responseToString());
            }
            base = sb.toString();
            System.out.println(base);
        }
        else
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\main\\java\\page1.html")));

        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2019-10-27\r\n" +
                "Content-Type: " + content + "\r\n" +
                "Content-Length: " + base.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + base;
        if (data.contains("givemedog")) {
            outputStream.write(base.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        else {
            outputStream.write(result.getBytes());
            outputStream.close();
        }

        socket.close();
    }

    private int dogAdder(String data) {
        String[] subStr;
        String delimeter = "&";
        subStr = data.split(delimeter);
        String age= subStr[3].replaceAll("[^0-9]", "");
        Dog dog = new Dog(subStr[0], subStr[1], subStr[2].charAt(0), Integer.parseInt(age));
                for (String s : subStr) {
            System.out.println(s);
        }
        dogs.add(dog);
        dogService.saveDog(dog);
        return dog.getId();
    }

    private void dogDelete(String data){
        System.out.println(dogs.size());
        int id = Integer.parseInt(data);
        int index = 0;
        for (Dog d : dogs) {
            if (d.getId() == id) {
                index = dogs.indexOf(d);
                break;
            }
        }
        dogs.remove(index);
        dogService.deleteDog(dogService.findDog(id));
        System.out.println(dogs.size());
    }

    private void dogChange(String data){
        String[] subStr;
        String delimeter = "&";
        subStr = data.split(delimeter);
        int id = Integer.parseInt(subStr[0]);
        int index = 0;
        for (Dog d : dogs) {
            if (d.getId() == id) {
                index = dogs.indexOf(d);
                break;
            }
        }
        Dog dog = dogs.get(index);
        dog.setName(subStr[1]);
        dog.setBreed(subStr[2]);
        dog.setSex(subStr[3].charAt(0));
        String age= subStr[4].replaceAll("[^0-9]", "");
        System.out.println(age);
        dog.setAge(Integer.parseInt(age));
        dogService.updateDog(dog);
    }
}
