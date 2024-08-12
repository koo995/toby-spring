package spring.toby.calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {


    public Integer calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, sumCallback, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback multiplyCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, multiplyCallback, 1);
    }

//    public String concatenate(String filepath) throws IOException {
//        LineCallback<String> concatenateCallback = new LineCallback<String>() {
//            @Override
//            public String doSomethingWithLine(String line, String value) {
//                return value + line;
//            }
//        };
//        return lineReadTemplate(filepath, concatenateCallback, "");
//    }



    // 아하 여기에 있는 지네릭 메서드는 지역변수로서 사용되니까 이렇게 쓰는구나
    // 결국 중요한 것은 지역변수로 쓰이고 말것이냐 아니면 클래스레벨에서 쓰일것이냐 차이구나?
    public <T> T lineReadTemplate(String filepath, LineCallback callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                    throw e;
                }
            }
        }
    }
}
