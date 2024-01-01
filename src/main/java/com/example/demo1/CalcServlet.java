package com.example.demo1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Stack;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet {
    String result;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Stack<Integer> stack = new Stack<>();
        Stack<String> ops = new Stack<>();
        resp.setContentType("text/html;charset=UTF-8");
        String exp = req.getParameter("exp");
        int[] ns = new int[exp.length()];
        try { //  올바른 표현식인지 확인 먼저함
            Integer.parseInt(String.valueOf(exp.charAt(exp.length() - 1)));
        } catch(Exception exception) {
            result = "올바른 표현식이 아닙니다.";
            req.setAttribute("result", result);
            req.getRequestDispatcher("index.jsp").forward(req,resp);
        }
        for(int i=0; i <= exp.length()-1; i++){
            try {
                int n = Integer.parseInt(String.valueOf(exp.charAt(i)));

//                ns[0] = n;
//                for(int j=1; true; j++){
//                    try {
//                        ns[j] =  Integer.parseInt(String.valueOf(exp.charAt(i+j)));
//                    }
//                    catch (Exception e){
//                        int num = (ns
//                        break; }
//                }

                stack.push(n);
            }
            catch (Exception e){
                if(exp.charAt(i) == 40) {
                    ops.push(String.valueOf(exp.charAt(i++)));
                    while(exp.charAt(i) != 41){
                        try {
                            int n = Integer.parseInt(String.valueOf(exp.charAt(i)));
                            stack.push(n);
                        } catch (Exception e2){
                            if((exp.charAt(i) == 42 || exp.charAt(i) == 47)){
                                String op = String.valueOf(exp.charAt(i));
                                int num2 = Integer.parseInt(String.valueOf(exp.charAt(++i)));
                                int num1 = stack.pop();
                                stack.push(opertae(num1, num2, op));
                            }else ops.push(String.valueOf(exp.charAt(i)));
                        }
                        i++;
                    }
                    while (!Objects.equals(ops.peek(), "(")) {
                            int num2 = stack.pop();
                            int num1 = stack.pop();
                            String op = ops.pop();
                            stack.push(opertae(num1, num2, op));
                    }
                    ops.pop();
                    continue;
                }
                if((exp.charAt(i) == 42 || exp.charAt(i) == 47) && exp.charAt(i+1) != 40){
                    String op = String.valueOf(exp.charAt(i));
                    int num2 = Integer.parseInt(String.valueOf(exp.charAt(++i)));
                    int num1 = stack.pop();
                    stack.push(opertae(num1, num2, op));
                } else if(!ops.isEmpty() && (ops.peek().equals("*") || ops.peek().equals("/"))){
                    String op = ops.pop();
                    int num2 = stack.pop();
                    int num1 = stack.pop();
                    stack.push(opertae(num1, num2, op));
                    ops.push(String.valueOf(exp.charAt(i)));
                } else ops.push(String.valueOf(exp.charAt(i)));
            }
        }
        while (stack.size() >= 2) {
            int num2 = stack.pop();
            int num1 = stack.pop();
            String op = ops.pop();
            stack.push(opertae(num1, num2, op));
        }
        result = String.valueOf(stack.pop());
        req.setAttribute("result", result);
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
    private int opertae(Integer num1, Integer num2, String op){
        try {
            switch (op) {
                case "+":
                    return num1 + num2;
                case "-":
                    return num1 - num2;
                case "*":
                    return num1 * num2;
                case "/":
                    return num1 / num2;
            }
        } catch (Exception e){
            result = "올바른 표현식이 아닙니다.";
        }
        return 0;
    }
}
