package logger;

import Interface.SubCategoryServiceImpl;
import services.SubCategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Logging implements SubCategoryServiceImpl {
    private SubCategoryServiceImpl subCategoryService;

    public Logging(SubCategoryServiceImpl subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @Override
    public void getAllSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Request received for getAllSubCategory");
        subCategoryService.getAllSubCategory(req, resp);
        System.out.println("Response sent for getAllSubCategory");
    }

    @Override
    public void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Request received for addSubCategory");
        subCategoryService.addSubCategory(req, resp);
        System.out.println("Response sent for addSubCategory");
    }

    @Override
    public void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        System.out.println("Request received for updateSubCategory with id: " + id);
        subCategoryService.updateSubCategory(req, resp, id);
        System.out.println("Response sent for updateSubCategory");
    }

    @Override
    public void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        System.out.println("Request received for deleteSubCategory with id: " + id);
        subCategoryService.deleteSubCategory(req, resp, id);
        System.out.println("Response sent for deleteSubCategory");
    }
}
