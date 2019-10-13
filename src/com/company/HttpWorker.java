package com.company;

import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpWorker implements Runnable{

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    HttpWorker(Socket socket) {
        this.socket = socket;
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
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            serverRequest(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void serverRequest(InputStream inputStream, OutputStream outputStream) throws Exception {
        String line;
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bf.readLine()) != null) {
            System.out.println(line);
            if (line.length() <= 0)
                break;
            if (line.startsWith("GET")) {
                String data= line.split(" ")[1].substring(1);
                populateResponse(outputStream, data);
            }
        }
    }

    private void populateResponse(OutputStream outputStream, String data) throws IOException {
        Pattern pattern = Pattern.compile("([0-9]+,)+");
        Pattern pattern1 = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(data);
        Matcher matcher1 = pattern1.matcher(data);
        String answer = data;
        if (matcher.find() && !matcher1.find()) {
            MaxFinder maxFinder = new MaxFinder(data);
            answer = "Ищем наибольшее из " + data + "<br/>Ответ: " + maxFinder.getResult();
        }
        String base ="<html><body><h1>Работу выполняли: Олевская Анна Леонидовна" +
                "<br/>Номер группы: ИКБО-16-17<br/>Номер индивидуального задания: 1" +
                "<br/>Текст индивидуального задания: Найти максимальное значение среди " +
                "множества чисел</br>" + answer + "</h1></body></html>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + base.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + base;
        outputStream.write(result.getBytes());
        outputStream.flush();
    }

}
