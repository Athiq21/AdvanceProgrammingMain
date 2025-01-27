package servlet;

import com.google.gson.Gson;
import model.Item;
import model.Offer;
import services.OfferService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;

//@WebServlet("/api/offer")
public class OfferServlet extends HttpServlet {

    private OfferService offerService;

    @Override
    public void init() throws ServletException {
        super.init();
        offerService = new OfferService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            Part imagePart = request.getPart("image");


            System.out.println("Received form data:");

            System.out.println("Image Part: " + (imagePart != null ? imagePart.getSubmittedFileName() : "No image"));

            Offer offer = new Offer();

        if (imagePart != null) {
            String image = saveImage(imagePart);
            offer.setImage(image);
        }

        Offer saveOffer = offerService.saveOffer(offer);


            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(saveOffer));
        }


    private String saveImage(Part imagePart) throws IOException {
        String fileName = "offer_" + System.currentTimeMillis() + ".png";
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.startsWith("/images/")) {
            Imageganna(request, response, pathInfo);
            return;
        }

        List<Offer> offers = offerService.getAllOffers();
        String baseUrl = request.getRequestURL().toString();
        if (request.getPathInfo() != null) {
            baseUrl = baseUrl.replace(request.getPathInfo(), "");
        }

        for (Offer offer : offers) {
            offer.setImage(baseUrl + "/images/" + offer.getImage());
        }

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(offers));
    }

    private void Imageganna(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws IOException {
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
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/offers/")) {
            try {
                String idStr = pathInfo.substring("/offers/".length());
                long offerId = Long.parseLong(idStr);

                boolean success = offerService.deleteOfferById(offerId);

                if (success) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Offer deleted successfully.\"}");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Offer not found.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid offer ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Offer ID not specified.");
        }
    }

}
