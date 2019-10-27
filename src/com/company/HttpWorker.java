package com.company;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpWorker implements Runnable{

    private Socket socket;
    private List<Dog> dogs;

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
                dogAdder(data);
                data = "dogishere";
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
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\page2.html")));

        else if (data.contains("getresult")) {
            base = new String((Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\getresult.js"))));
            content = "text/javascript";
        }
        else if (data.contains("page3"))
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\page3.html")));

        else if (data.contains("styles")) {
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\styles.css")));
            content = "text/css";
        }
        else if (data.contains("tablework")) {
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\tablework.js")));
        }
        else if (data.contains("dogishere")) {
            //base = "Server side says: saving was successful КУСЬ ВАС";
            base = "OK";
        }
        else if (data.contains("deleted")) {
            base = "OK";
        }
        else if (data.contains("changed")) {
            base = "OK";

        }

        else
            base = new String(Files.readAllBytes(Paths.get("C:\\Users\\Анна\\IdeaProjects\\CSA-lab2\\src\\page1.html")));

        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2019-10-27\r\n" +
                "Content-Type: " + content + "\r\n" +
                "Content-Length: " + base.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + base;
        outputStream.write(result.getBytes());
        outputStream.flush();
        for (Dog dog: dogs) {
            System.out.println(dog.toString());
        }
        socket.close();
    }

    private void dogAdder(String data) {
        String[] subStr;
        String delimeter = "&";
        subStr = data.split(delimeter);
        Dog dog = new Dog(dogs.size(), subStr[0], subStr[1], subStr[2].charAt(0), Float.parseFloat(subStr[3]));
        for (String s : subStr) {
            System.out.println(s);
        }
        dogs.add(dog);
    }

    private void dogDelete(String data){
        System.out.println(dogs.size());
        int id = Integer.parseInt(data)-1;
        dogs.remove(id);
        System.out.println(dogs.size());
    }

    private void dogChange(String data){
        String[] subStr;
        String delimeter = "&";
        subStr = data.split(delimeter);
        int id = Integer.parseInt(subStr[0]);
        dogs.get(id).setName(subStr[1]);
        dogs.get(id).setBreed(subStr[2]);
        dogs.get(id).setSex(subStr[3].charAt(0));
        dogs.get(id).setAge(Float.parseFloat(subStr[4]));
    }
}
