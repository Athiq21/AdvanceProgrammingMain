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
        int createdBy = Integer.parseInt(request.getParameter("created_by"));
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        int subcategoryId = Integer.parseInt(request.getParameter("subcategory_id"));

        System.out.println("Received form data:");
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Category ID: " + categoryId);
        System.out.println("Subcategory ID: " + subcategoryId);
        System.out.println("Created By: " + createdBy);
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
    
        if (imagePart != null) {
            String imageBlob = saveImage(imagePart);
            item.setImageBlob(imageBlob);
        }

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

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//
//        // Serve images
//        if (pathInfo != null && pathInfo.startsWith("/images/")) {
//            String filename = pathInfo.substring("/images/".length());
//            File imageFile = new File("/Users/athiq/Downloads/images", filename);
//
//            if (imageFile.exists()) {
//                response.setContentType("image/png");
//                try (InputStream inputStream = new FileInputStream(imageFile);
//                     OutputStream outputStream = response.getOutputStream()) {
//                    byte[] buffer = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//                }
//            } else {
//                System.err.println("Image not found: " + filename);
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
//            }
//        } else {
//            List<Item> items = itemService.getAllItems();
//
//            String baseUrl = request.getRequestURL().toString();
//            if (request.getPathInfo() != null) {
//                baseUrl = baseUrl.replace(request.getPathInfo(), "");
//            }
//
//            for (Item item : items) {
//                item.setImageBlob(baseUrl + "/images/" + item.getImageBlob());
//            }
//
//            response.setContentType("application/json");
//            response.getWriter().write(new Gson().toJson(items));
//        }
//    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//
//        if (pathInfo != null && pathInfo.startsWith("/subcategory/")) {
//            String subcategoryIdString = pathInfo.substring("/subcategory/".length());
//            try {
//                int subcategoryId = Integer.parseInt(subcategoryIdString);
//                List<Item> items = itemService.getItemsBySubcategory(subcategoryId);
//
//                // Prepare the response
//                String baseUrl = request.getRequestURL().toString();
//                if (request.getPathInfo() != null) {
//                    baseUrl = baseUrl.replace(request.getPathInfo(), "");
//                }
//
//                for (Item item : items) {
//                    item.setImageBlob(baseUrl + "/images/" + item.getImageBlob());
//                }
//
//                // Send the list of items as JSON response
//                response.setContentType("application/json");
//                response.getWriter().write(new Gson().toJson(items));
//
//            } catch (NumberFormatException e) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subcategory ID");
//            }
//        } else {
//            // Default behavior for fetching all items or serving images
//            List<Item> items = itemService.getAllItems();
//
//            String baseUrl = request.getRequestURL().toString();
//            if (request.getPathInfo() != null) {
//                baseUrl = baseUrl.replace(request.getPathInfo(), "");
//            }
//
//            for (Item item : items) {
//                item.setImageBlob(baseUrl + "/images/" + item.getImageBlob());
//            }
//
//            response.setContentType("application/json");
//            response.getWriter().write(new Gson().toJson(items));
//        }
//    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Serve images - separate endpoint for images
        if (pathInfo != null && pathInfo.startsWith("/images/")) {
            serveImage(request, response, pathInfo);
            return; // Ensure we stop further processing here if it's an image request
        }

        if (pathInfo != null && pathInfo.startsWith("/subcategory/")) {
            handleSubcategoryRequest(request, response, pathInfo);
            return;
        }

        List<Item> items = itemService.getAllItems();
        String baseUrl = request.getRequestURL().toString();
        if (request.getPathInfo() != null) {
            baseUrl = baseUrl.replace(request.getPathInfo(), "");
        }

        // Update imageBlob URL for each item
        for (Item item : items) {
            item.setImageBlob(baseUrl + "/images/" + item.getImageBlob()); // Correct image path
        }

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(items));
    }

    // Handle requests for a specific subcategory
    private void handleSubcategoryRequest(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws IOException {
        String subcategoryIdString = pathInfo.substring("/subcategory/".length());
        try {
            int subcategoryId = Integer.parseInt(subcategoryIdString);
            List<Item> items = itemService.getItemsBySubcategory(subcategoryId);


            String baseUrl = request.getRequestURL().toString();
            if (request.getPathInfo() != null) {
                baseUrl = baseUrl.replace(request.getPathInfo(), "");
            }

            for (Item item : items) {
                item.setImageBlob(baseUrl + "/images/" + item.getImageBlob()); // Correct image path
            }

            // Send the list of items for the subcategory as a JSON response
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(items));

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subcategory ID");
        }
    }

    private void serveImage(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws IOException {
        String filename = pathInfo.substring("/images/".length());
        String imagePath = "/Users/athiq/Downloads/images";

        File imageFile = new File(imagePath, filename);
        if (imageFile.exists()) {
            response.setContentType("image/png"); // Adjust if the image type is different (e.g., jpeg)
            try (InputStream inputStream = new FileInputStream(imageFile);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idStr = request.getPathInfo().substring(1);
            long itemId = Long.parseLong(idStr);
            ItemService itemService = new ItemService();
            boolean success = itemService.deleteItemById(itemId);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("ok");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "no1");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "no2");
        }
    }




}
