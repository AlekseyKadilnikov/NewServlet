package ru.appline.newservlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.appline.newservlet.logic.Model;
import ru.appline.newservlet.logic.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/calc")
public class ServletCalc extends HttpServlet {

    Model model = Model.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JsonObject jsonObject = gson.fromJson(String.valueOf(jb), JsonObject.class);
        PrintWriter pw = response.getWriter();
        JsonObject jsonError = new JsonObject();
        JsonObject jsonResult = new JsonObject();

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        double a;
        double b;
        String math;
        try {
            a = jsonObject.get("a").getAsDouble();
            b = jsonObject.get("b").getAsDouble();
            math = jsonObject.get("math").getAsString();
        } catch (Exception e) {
            jsonError.addProperty("error", "Неверный формат параметра(-ов)");
            pw.print(jsonError);
            return;
        }

        double result;

        switch (math) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                if(b == 0) {
                    jsonError.addProperty("error", "It was at this moment he knew, he fucked up.");
                    pw.print(jsonError);
                    return;
                } else {
                    result = a / b;
                }
                break;
            default:
                jsonError.addProperty("error", "Это что за операция?");
                pw.print(jsonError);
                return;
        }

        jsonResult.addProperty("result", result);
        pw.print(jsonResult);
    }
}
