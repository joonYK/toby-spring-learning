package learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filepath) throws IOException {
        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
            @Override
            public int doSomethingWithReader(BufferedReader br) throws IOException {
                int sum = 0;
                String line = null;
                while((line = br.readLine()) != null)
                    sum += Integer.parseInt(line);
                return sum;
            }
        };

        return fileReadTemplate(filepath, sumCallback);
    }

    public int calcMultiply(String filepath) throws IOException {
        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
            @Override
            public int doSomethingWithReader(BufferedReader br) throws IOException {
                int multiply = 1;
                String line = null;
                while((line = br.readLine()) != null)
                    multiply *= Integer.parseInt(line);
                return multiply;
            }
        };

        return fileReadTemplate(filepath, multiplyCallback);
    }

    public int fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            return callback.doSomethingWithReader(br);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
