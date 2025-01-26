package servlet;

import com.google.gson.Gson;
import model.Item;
import services.ItemService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;

@WebServlet("/item/*")
public class ItemServlet extends HttpServlet {

    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        itemService = new ItemService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No path provided");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/save":
                saveItem(request, response);
                break;
//            case "/view":
//                getAllItems(request, response);
//                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + path);
        }
    }

    private void saveItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Handle multipart form data
        Part imagePart = request.getPart("image"); // Use "image" as the key
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String color = request.getParameter("color");
        String mileage = request.getParameter("mileage");
        String transmission = request.getParameter("transmission");
        String fueltype = request.getParameter("fueltype");
        String price = request.getParameter("price");
        String createdBy = request.getParameter("created_by");
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        int subcategoryId = Integer.parseInt(request.getParameter("subcategory_id"));

        // Debug the received form data
        System.out.println("Received form data:");
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Category ID: " + categoryId);
        System.out.println("Subcategory ID: " + subcategoryId);
        System.out.println("Image Part: " + (imagePart != null ? imagePart.getSubmittedFileName() : "No image"));

        // Prepare the Item object
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setColor(color);
        item.setMileage(mileage);
        item.setTransmission(transmission);
        item.setFuelType(fueltype);
        item.setPrice(price);
        item.setCreatedBy(createdBy);
        item.setCategoryId(categoryId);
        item.setSubcategoryId(subcategoryId);

        // Handle the image upload
        if (imagePart != null) {
            String imageBlob = saveImage(imagePart);
            item.setImageBlob(imageBlob);
        }

        // Save the item to the database
        Item savedItem = itemService.saveItem(item);

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(savedItem));
    }

    private String saveImage(Part imagePart) throws IOException {
        String fileName = "item_" + System.currentTimeMillis() + ".png";
        String folderPath = "/Users/athiq/Downloads/images";
        File outputFile = new File(folderPath, fileName);

        try (InputStream inputStream = imagePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        return fileName;
    }

//    private void getAllItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        List<Item> items = itemService.getAllItems();
//
//        if (items.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
//            response.getWriter().write("No items found");
//        } else {
//            response.setContentType("application/json");
//            response.getWriter().write(new Gson().toJson(items));
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Serve images
        if (pathInfo != null && pathInfo.startsWith("/images/")) {
            String filename = pathInfo.substring("/images/".length());
            File imageFile = new File("/Users/athiq/Downloads/images", filename);

            if (imageFile.exists()) {
                response.setContentType("image/png");
                try (InputStream inputStream = new FileInputStream(imageFile);
                     OutputStream outputStream = response.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                System.err.println("Image not found: " + filename);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            }
        } else {
            List<Item> items = itemService.getAllItems();

            String baseUrl = request.getRequestURL().toString();
            if (request.getPathInfo() != null) {
                baseUrl = baseUrl.replace(request.getPathInfo(), "");
            }

            for (Item item : items) {
                item.setImageBlob(baseUrl + "/images/" + item.getImageBlob());
            }

            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(items));
        }
    }



}
