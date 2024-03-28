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
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(urlPatterns = "/put")
public class ServletPut extends HttpServlet {

    Model model = Model.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        int id = jsonObject.get("id").getAsInt();
        if(!model.getFromList().containsKey(id)) {
            jsonError.addProperty("error", "Такого пользователя нет :(");
            pw.print(jsonError);
        } else {
            String name = jsonObject.get("name").getAsString();
            String surname = jsonObject.get("surname").getAsString();
            double salary = jsonObject.get("salary").getAsDouble();

            User user = model.getFromList().get(id);
            user.setName(name);
            user.setSurname(surname);
            user.setSalary(salary);

            pw.print(gson.toJson(model.getFromList().get(id)));
        }
    }
}
